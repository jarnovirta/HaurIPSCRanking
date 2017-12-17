package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.DataImportEventListener;
import haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class WinMSSDataImportService {

	private static List<DataImportEventListener> importProgressEventListeners = new ArrayList<DataImportEventListener>();

	private static int progressCounterTotalSteps;
	private static double progressCounterCompletedSteps;
	private static int progressPercentage;

	private static WinMSSMatchRepository winMSSMatchRepository;
	private static WinMSSStageRepository winMSSStageRepository;
	private static WinMSSStageScoreSheetRepository winMSSStageScoreSheetRepository;

	private static int oldScoreSheetsRemovedCount;
	private static int newMatchesCount;
	private static int newScoreSheetsCount;
	private static int newStagesCount;
	private static int newCompetitorsCount;

	private static List<StageScoreSheet> newStageScoreSheets;

	public static void init(WinMSSMatchRepository winMSSMatchRepo, WinMSSStageRepository WinMSSStageRepo,
			WinMSSStageScoreSheetRepository winMSSStageScoreSheetRepo) {
		winMSSMatchRepository = winMSSMatchRepo;
		winMSSStageRepository = WinMSSStageRepo;
		winMSSStageScoreSheetRepository = winMSSStageScoreSheetRepo;
	}

	// Find stages with new results, from which user selects stage results to
	// be imported
	public static List<Match> findNewResultsInWinMSSDatabase(String winMssDbLocation) {
		setImportProgressStatus(ImportStatus.LOADING_FROM_WINMSS);
		List<Match> winMSSMatches = winMSSMatchRepository.findAll(winMssDbLocation);
		for (Match match : winMSSMatches) {
			match.setStages(winMSSStageRepository.findStagesForMatch(match));
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName())) {
					ClassifierStage classifier = ClassifierStage.parseString(stage.getName());
					stage.setSaveAsClassifierStage(classifier);
					stage.setClassifierStage(classifier);
				}

				if (StageService.find(stage) == null) {
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
		return winMSSMatches;
	}

	// Fetch stage score sheets for stages selected by user and save to Ranking
	// Database
	public static void importSelectedResults(List<Match> matches) {

		initializeImportProgressVariables(matches);

		// List<Stage> invalidClassifiers = new ArrayList<Stage>();
		setImportProgressStatus(ImportStatus.SAVING_TO_HAUR_RANKING_DB);
		for (Match match : matches) {
			setStageScoreSheetsFromWinMSS(match);
			int scoreSheetsCount = getScoreSheetsCount(match);
			prepareMatchForSave(match);
			if (match.getStages().size() > 0) {
				MatchService.save(match);
			}
			addImportProgress(scoreSheetsCount);
			newScoreSheetsCount += getScoreSheetsCount(match);
		}

		oldScoreSheetsRemovedCount = StageScoreSheetService.removeExtraStageScoreSheets(newStageScoreSheets);

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

	private static void prepareMatchForSave(Match match) {
		newCompetitorsCount += CompetitorService.mergeCompetitorsInMatch(match);
		filterOutStagesWithNoNewResults(match);
		newStagesCount += match.getStages().size();
		for (Stage stage : match.getStages()) {
			newStageScoreSheets.addAll(stage.getStageScoreSheets());
		}
	}

	private static void setStageScoreSheetsFromWinMSS(Match match) {
		for (Stage stage : match.getStages()) {
			List<StageScoreSheet> sheets = winMSSStageScoreSheetRepository.find(stage.getMatch(), stage);
			if (sheets == null)
				return;
			stage.setStageScoreSheets(sheets);
			for (StageScoreSheet sheet : stage.getStageScoreSheets())
				sheet.setStage(stage);
		}
	}

	private static int getScoreSheetsCount(Match match) {

		int stageScoreSheetCount = 0;
		for (Stage stage : match.getStages()) {
			if (stage.getStageScoreSheets() != null) {
				stageScoreSheetCount += stage.getStageScoreSheets().size();
			}
		}
		return stageScoreSheetCount;
	}

	private static void filterOutStagesWithNoNewResults(Match match) {

		List<Stage> stagesWithNewResults = new ArrayList<Stage>();
		for (Stage stage : match.getStages()) {
			if (stage.isNewStage()) {
				if (stage.getSaveAsClassifierStage() != null) {
					stage.setClassifierStage(stage.getSaveAsClassifierStage());
					if (!StageService.isValidClassifier(stage)) {
						continue;
					}

					if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
						stagesWithNewResults.add(stage);
					}
				}
			}
		}
		match.setStages(stagesWithNewResults);
	}

	private static void initializeImportProgressVariables(List<Match> matches) {
		progressCounterTotalSteps = 0;
		progressCounterCompletedSteps = 0;
		progressPercentage = 0;
		oldScoreSheetsRemovedCount = 0;
		newMatchesCount = 0;
		newScoreSheetsCount = 0;
		newStagesCount = 0;
		newCompetitorsCount = 0;

		newStageScoreSheets = new ArrayList<StageScoreSheet>();

		for (Match match : matches) {
			for (Stage stage : match.getStages()) {
				progressCounterTotalSteps += winMSSStageScoreSheetRepository.getScoreSheetCountForStage(match, stage);
			}
		}
	}

	private static void addImportProgress(double progressStepsCount) {
		if (progressCounterTotalSteps == 0)
			return;
		progressCounterCompletedSteps += progressStepsCount;
		progressPercentage = Math
				.toIntExact(Math.round(progressCounterCompletedSteps / progressCounterTotalSteps * 100));
		emitImportProgressEvent();

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

	private static void setImportProgressStatus(ImportStatus newStatus) {
		DataImportEvent event = new DataImportEvent(DataImportEventType.IMPORT_STATUS_CHANGE);
		event.setImportStatus(newStatus);
		emitDataImportEvent(event);
	}

	public static void addImportProgressEventListener(DataImportEventListener listener) {
		importProgressEventListeners.add(listener);
	}
}
