package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.Event.ImportProgressEvent;
import haur_ranking.Event.ImportProgressEventListener;
import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class MatchService {
	private static EntityManager entityManager;

	// Variables queried by GUI for progress bar
	public enum ImportStatus {
		READY, IMPORTING, GENERATING_RANKING, DONE
	};

	private static ImportStatus importStatus = ImportStatus.READY;
	private static int progressCounterTotalSteps;
	private static double progressCounterCompletedSteps;
	private static int progressPercentage = 0;
	private static List<ImportProgressEventListener> importProgressEventListeners = new ArrayList<ImportProgressEventListener>();

	public static Match find(Match match, EntityManager entityManager) {
		return MatchRepository.find(match, entityManager);
	}

	private static void findNewWinMssStageScoreSheets(Stage stage) {
		stage.setStageScoreSheets(WinMSSStageScoreSheetRepository.find(stage.getMatch(), stage));
		for (StageScoreSheet sheet : stage.getStageScoreSheets())
			sheet.setStage(stage);

		// Handle new stage score sheets
		if (stage.getStageScoreSheets().size() > 0) {
			StageScoreSheetService.setCompetitorsToStageScoreSheets(stage.getStageScoreSheets(), entityManager);
		}
	}

	public static int getTotalMatchCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		int count = MatchRepository.getTotalMatchCount(entityManager);
		entityManager.close();
		return count;
	}

	// Find stages with new results, from which user selects stage results to
	// be imported
	public static List<Match> findNewResultsInWinMSSDatabase(String winMssDbLocation) {
		entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> matchesWithNewResults = new ArrayList<Match>();
		List<Match> winMssMatches = WinMSSMatchRepository.findAll(winMssDbLocation);
		for (Match match : winMssMatches) {
			List<Stage> stagesWithNewResults = new ArrayList<Stage>();
			match.setStages(WinMSSStageRepository.findStagesForMatch(match));
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName()))
					stage.setClassifierStage(ClassifierStage.parseString(stage.getName()));
				if (StageService.find(stage, entityManager) == null)
					stagesWithNewResults.add(stage);
			}
			if (stagesWithNewResults.size() > 0) {
				match.setStages(stagesWithNewResults);
				matchesWithNewResults.add(match);
			}
		}
		return matchesWithNewResults;
	}

	// Fetch stage score sheets for stages selected by user and save to Ranking
	// Database
	public static void importSelectedResultsFromWinMSSDatabase(List<Match> matches) {
		// Initialize progress counter variables to be queried by GUI for
		// progress bar
		initializeProgressCounterVariables(matches);
		entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		List<IPSCDivision> divisionsWithNewResults = new ArrayList<IPSCDivision>();
		List<Match> matchesWithNewResults = new ArrayList<Match>();

		for (Match match : matches) {
			List<Stage> stagesWithNewResults = new ArrayList<Stage>();
			for (Stage stage : match.getStages()) {
				findNewWinMssStageScoreSheets(stage);
				if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
					stagesWithNewResults.add(stage);
					for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
						if (!divisionsWithNewResults.contains(sheet.getIpscDivision()))
							divisionsWithNewResults.add(sheet.getIpscDivision());
					}
				}
				addImportProgress(stage.getStageScoreSheets().size());
			}
			if (stagesWithNewResults.size() > 0)
				matchesWithNewResults.add(match);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		if (matchesWithNewResults.size() > 0)
			save(matchesWithNewResults);
		setImportProgressStatus(ImportStatus.GENERATING_RANKING);
		RankingService.generateRanking();
		setImportProgressStatus(ImportStatus.DONE);
	}

	public static void save(List<Match> matches) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		List<StageScoreSheet> newStageScoreSheets = new ArrayList<StageScoreSheet>();

		// Persist matches with new results. Check for existing matches.
		// Existing stage score sheets have already been
		// filtered out and therefore there is no need to check them or for
		// existing stages (all score sheets for a stage
		// are always treated together).
		for (Match newMatch : matches) {
			int stageScoreSheetCount = 0;
			for (Stage stage : newMatch.getStages()) {
				if (stage.getStageScoreSheets() != null) {
					newStageScoreSheets.addAll(stage.getStageScoreSheets());
					for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
						Competitor existingCompetitor = CompetitorService.find(sheet.getCompetitor().getFirstName(),
								sheet.getCompetitor().getLastName(), entityManager);
						if (existingCompetitor != null) {
							sheet.setCompetitor(existingCompetitor);
						}
					}
					stageScoreSheetCount += stage.getStageScoreSheets().size();
					addImportProgress(stage.getStageScoreSheets().size() / 2.0);
				}
			}
			Match existingMatch = find(newMatch, entityManager);
			if (existingMatch != null) {
				for (Stage stage : newMatch.getStages()) {
					stage.setMatch(existingMatch);
				}
				existingMatch.getStages().addAll(newMatch.getStages());
			} else {
				MatchRepository.save(newMatch, entityManager);

			}
			entityManager.flush();
			addImportProgress(stageScoreSheetCount / 2.0);

		}
		entityManager.getTransaction().commit();
		entityManager.close();
		StageScoreSheetService.removeExtraStageScoreSheets(newStageScoreSheets);

	}

	private static void initializeProgressCounterVariables(List<Match> matches) {
		setImportProgressStatus(ImportStatus.IMPORTING);
		progressCounterCompletedSteps = 0;
		progressCounterTotalSteps = 0;
		for (Match match : matches) {
			for (Stage stage : match.getStages()) {
				progressCounterTotalSteps += WinMSSStageScoreSheetRepository.getScoreSheetCountForStage(match, stage);
			}
		}
		progressCounterTotalSteps = progressCounterTotalSteps * 2;
	}

	private static void addImportProgress(double progressStepsCount) {
		if (progressCounterTotalSteps == 0)
			return;
		progressCounterCompletedSteps += progressStepsCount;
		progressPercentage = Math
				.toIntExact(Math.round(progressCounterCompletedSteps / progressCounterTotalSteps * 100));
		emitImportProgressEvent();

	}

	private static void setImportProgressStatus(ImportStatus newStatus) {
		importStatus = newStatus;
		emitImportProgressEvent();
	}

	private static void emitImportProgressEvent() {
		for (ImportProgressEventListener listener : importProgressEventListeners) {
			listener.setProgress(new ImportProgressEvent(importStatus, progressPercentage));
		}
	}

	public static void addImportProgressEventListener(ImportProgressEventListener listener) {
		importProgressEventListeners.add(listener);
	}
}
