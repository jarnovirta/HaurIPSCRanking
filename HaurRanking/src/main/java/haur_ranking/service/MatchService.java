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
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.DataImportEventListener;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class MatchService {
	private static EntityManager entityManager;

	private static int progressCounterTotalSteps;
	private static double progressCounterCompletedSteps;
	private static int progressPercentage = 0;
	private static List<DataImportEventListener> importProgressEventListeners = new ArrayList<DataImportEventListener>();

	private static int newScoreSheetsCount;
	private static int oldScoreSheetsRemovedCount;
	private static int newStagesCount;
	private static int newMatchesCount;
	private static int newCompetitorsCount;

	public static Match find(Match match, EntityManager entityManager) {
		return MatchRepository.find(match, entityManager);
	}

	public static void delete(Match match) {
		try {
			EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
			boolean commitTransaction = false;
			if (!entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().begin();
				commitTransaction = true;
			}
			if (!entityManager.contains(match))
				match = entityManager.merge(match);
			MatchRepository.delete(match, entityManager);
			if (commitTransaction) {
				entityManager.getTransaction().commit();
				entityManager.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Match> getMatchTableData(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> matches = MatchRepository.getMatchListPage(page, pageSize, entityManager);
		entityManager.close();
		return matches;

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

	}

	public static int getTotalMatchCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		int count = MatchRepository.getMatchCount(entityManager);
		entityManager.close();
		return count;
	}

	// Find stages with new results, from which user selects stage results to
	// be imported
	public static List<Match> findNewResultsInWinMSSDatabase(String winMssDbLocation) {

		setImportProgressStatus(ImportStatus.LOADING_FROM_WINMSS);
		entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> winMSSMatches = WinMSSMatchRepository.findAll(winMssDbLocation);
		for (Match match : winMSSMatches) {
			match.setStages(WinMSSStageRepository.findStagesForMatch(match));
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName())) {
					stage.setClassifierStage(ClassifierStage.parseString(stage.getName()));
					stage.setSaveAsClassifierStage(stage.getClassifierStage());
				}
				if (StageService.find(stage, entityManager) == null) {
					stage.setNewStage(true);
				} else {
					stage.setNewStage(false);
				}
			}
		}
		DataImportEvent loadFromWinMSSDoneEvent = new DataImportEvent(DataImportEventType.IMPORT_STATUS_CHANGE);
		loadFromWinMSSDoneEvent.setImportStatus(ImportStatus.LOAD_FROM_WINMSS_DONE);
		loadFromWinMSSDoneEvent.setWinMSSMatches(winMSSMatches);
		emitDataImportEvent(loadFromWinMSSDoneEvent);
		entityManager.close();
		return winMSSMatches;
	}

	// Fetch stage score sheets for stages selected by user and save to Ranking
	// Database
	public static void importSelectedResults(List<Match> matches) {

		List<Stage> invalidClassifiers = new ArrayList<Stage>();
		setImportProgressStatus(ImportStatus.SAVING_TO_HAUR_RANKING_DB);
		// Initialize progress counter variables to be queried by GUI for
		// progress bar
		initializeImportVariables(matches);
		entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		List<IPSCDivision> divisionsWithNewResults = new ArrayList<IPSCDivision>();
		List<Match> matchesWithNewResults = new ArrayList<Match>();

		for (Match match : matches) {
			List<Stage> stagesWithNewResults = new ArrayList<Stage>();
			for (Stage stage : match.getStages()) {
				if (stage.isNewStage()) {
					if (stage.getSaveAsClassifierStage() != null) {
						stage.setClassifierStage(stage.getSaveAsClassifierStage());
						if (!StageService.isValidClassifier(stage)) {
							invalidClassifiers.add(stage);
							continue;
						}
						findWinMSSStageScoreSheets(stage);
						if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
							stagesWithNewResults.add(stage);
							for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
								if (!divisionsWithNewResults.contains(sheet.getIpscDivision()))
									divisionsWithNewResults.add(sheet.getIpscDivision());
							}
						}
					}
					if (stage.getStageScoreSheets() != null)
						addImportProgress(stage.getStageScoreSheets().size());
				}
			}
			if (stagesWithNewResults.size() > 0) {
				match.setStages(stagesWithNewResults);
				matchesWithNewResults.add(match);
			}
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		if (matchesWithNewResults.size() > 0) {
			save(matchesWithNewResults);
		}
		setImportProgressStatus(ImportStatus.GENERATING_RANKING);
		RankingService.generateRanking();

		DataImportEvent event = new DataImportEvent(DataImportEventType.IMPORT_STATUS_CHANGE);
		event.setImportStatus(ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE);
		event.setNewScoreSheetsCount(newScoreSheetsCount);
		event.setNewCompetitorsCount(newCompetitorsCount);
		event.setNewMatchesCount(newMatchesCount);
		event.setNewStagesCount(newStagesCount);
		event.setOldScoreSheetsRemovedCount(oldScoreSheetsRemovedCount);
		emitDataImportEvent(event);

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
					newStagesCount++;
					newStageScoreSheets.addAll(stage.getStageScoreSheets());
					for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
						Competitor existingCompetitor = CompetitorService.find(sheet.getCompetitor().getFirstName(),
								sheet.getCompetitor().getLastName(), sheet.getCompetitor().getWinMSSComment(),
								entityManager);
						if (existingCompetitor != null) {
							sheet.setCompetitor(existingCompetitor);
						} else {
							newCompetitorsCount++;
						}
					}
					stageScoreSheetCount += stage.getStageScoreSheets().size();
					if (stage.getStageScoreSheets() != null)
						addImportProgress(stage.getStageScoreSheets().size() / 2.0);
				}
			}
			newScoreSheetsCount += stageScoreSheetCount;
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
		oldScoreSheetsRemovedCount = StageScoreSheetService.removeExtraStageScoreSheets(newStageScoreSheets);

	}

	public static Match findLatestMatch() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		Match match = MatchRepository.findLatestMatch(entityManager);
		entityManager.close();
		return match;
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
				ClassifierStage classifier = match.getStages().get(i).getClassifierStage();
				if (classifier != null)
					rowData[3] = classifier.toString();
				tableRows.add(rowData);
			}
		}

		return tableRows;
	}

	private static void initializeImportVariables(List<Match> matches) {

		newScoreSheetsCount = 0;
		newStagesCount = 0;
		newMatchesCount = 0;
		newCompetitorsCount = 0;

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
		DataImportEvent event = new DataImportEvent(DataImportEventType.IMPORT_STATUS_CHANGE);
		event.setImportStatus(newStatus);
		emitDataImportEvent(event);
	}

	private static void emitImportProgressEvent() {
		DataImportEvent event = new DataImportEvent(DataImportEventType.IMPORT_PROGRESS);
		event.setProgressPercent(progressPercentage);
		emitDataImportEvent(event);
	}

	private static void emitDataImportEvent(DataImportEvent event) {
		for (DataImportEventListener listener : importProgressEventListeners) {
			listener.processData(event);
		}
	}

	public static void addImportProgressEventListener(DataImportEventListener listener) {
		importProgressEventListeners.add(listener);
	}
}
