package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class MatchService {
	private static EntityManager entityManager;

	// Variables queried by GUI for progress bar
	public enum ImportStatus {
		READY, LOADING_FROM_WINMSS, SAVING_TO_HAUR_RANKING_DB, GENERATING_RANKING, SAVE_TO_HAUR_RANKING_DB_DONE, LOAD_FROM_WINMSS_DONE
	};

	private static ImportStatus importStatus = ImportStatus.READY;
	private static int progressCounterTotalSteps;
	private static double progressCounterCompletedSteps;
	private static int progressPercentage = 0;
	private static List<GUIDataEventListener> importProgressEventListeners = new ArrayList<GUIDataEventListener>();

	public static Match find(Match match, EntityManager entityManager) {
		return MatchRepository.find(match, entityManager);
	}

	public static List<Match> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> matches = null;
		matches = MatchRepository.findAll(entityManager);
		entityManager.close();
		return matches;
	}

	private static void findWinMSSStageScoreSheets(Stage stage) {
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
		setImportProgressStatus(ImportStatus.LOADING_FROM_WINMSS);
		entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> winMssMatches = WinMSSMatchRepository.findAll(winMssDbLocation);
		for (Match match : winMssMatches) {
			match.setStages(WinMSSStageRepository.findStagesForMatch(match));
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName()))
					stage.setClassifierStage(ClassifierStage.parseString(stage.getName()));
				if (StageService.find(stage, entityManager) == null) {
					stage.setNewStage(true);
				} else {
					stage.setNewStage(false);
				}
			}
		}
		setImportProgressStatus(ImportStatus.LOAD_FROM_WINMSS_DONE);
		return winMssMatches;
	}

	// Fetch stage score sheets for stages selected by user and save to Ranking
	// Database
	public static void importSelectedResults(List<Match> matches) {
		setImportProgressStatus(ImportStatus.SAVING_TO_HAUR_RANKING_DB);
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
				if (stage.isNewStage()) {
					findWinMSSStageScoreSheets(stage);
					if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
						stagesWithNewResults.add(stage);
						for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
							if (!divisionsWithNewResults.contains(sheet.getIpscDivision()))
								divisionsWithNewResults.add(sheet.getIpscDivision());
						}
					}
					addImportProgress(stage.getStageScoreSheets().size());
				}
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
		setImportProgressStatus(ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE);
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

	public static List<String[]> getGUIImportedMatchesTableData() {
		List<String[]> tableRows = new ArrayList<String[]>();
		List<Match> matches = findAll();
		int matchCounter = 1;
		for (Match match : matches) {
			String[] rowData = new String[4];
			rowData[0] = (matchCounter++) + ".";
			rowData[1] = match.getName();
			rowData[2] = match.getDateString();
			if (match.getStages() == null || match.getStages().size() == 0)
				continue;
			rowData[3] = match.getStages().get(0).getName();
			tableRows.add(rowData);
			if (match.getStages().size() == 1)
				continue;
			for (int i = 1; i < match.getStages().size(); i++) {
				rowData = new String[4];
				rowData[0] = "";
				rowData[1] = "";
				rowData[2] = "";
				rowData[3] = match.getStages().get(i).getName();
				tableRows.add(rowData);
			}
		}

		return tableRows;
	}

	private static void initializeProgressCounterVariables(List<Match> matches) {
		setImportProgressStatus(ImportStatus.LOADING_FROM_WINMSS);
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
		GUIDataEvent event = new GUIDataEvent(GUIDataEventType.DATA_IMPORT_PROGRESS);
		event.setImportStatus(importStatus);
		event.setProgressPercent(progressPercentage);
		for (GUIDataEventListener listener : importProgressEventListeners) {
			listener.processData(event);
		}
	}

	public static void addImportProgressEventListener(GUIDataEventListener listener) {
		importProgressEventListeners.add(listener);
	}
}
