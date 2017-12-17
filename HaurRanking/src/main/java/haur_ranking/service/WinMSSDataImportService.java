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
	private static int progressPercentage = 0;

	// Find stages with new results, from which user selects stage results to
	// be imported
	public static List<Match> findNewResultsInWinMSSDatabase(String winMssDbLocation) {
		setImportProgressStatus(ImportStatus.LOADING_FROM_WINMSS);
		List<Match> winMSSMatches = WinMSSMatchRepository.findAll(winMssDbLocation);
		for (Match match : winMSSMatches) {
			match.setStages(WinMSSStageRepository.findStagesForMatch(match));
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName())) {
					ClassifierStage classifier = ClassifierStage.parseString(stage.getName());
					stage.setSaveAsClassifierStage(classifier);
					stage.setClassifierStage(classifier);
				}

				stage.setNewStage(true);

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
		int oldScoreSheetsRemovedCount = 0;
		int newMatchesCount = 0;
		int newScoreSheetsCount = 0;
		int newStagesCount = 0;
		int newCompetitorsCount = 0;

		List<StageScoreSheet> newStageScoreSheets = new ArrayList<StageScoreSheet>();
		initializeImportProgressVariables(matches);

		// List<Stage> invalidClassifiers = new ArrayList<Stage>();
		setImportProgressStatus(ImportStatus.SAVING_TO_HAUR_RANKING_DB);

		for (Match match : matches) {
			setStageScoreSheetsFromWinMSS(match);
			newCompetitorsCount += CompetitorService.mergeCompetitorsInMatch(match);
			int scoreSheetsCount = getScoreSheetsCount(match);
			filterOutStagesWithNoNewResults(match);
			newStagesCount += match.getStages().size();
			if (match.getStages().size() == 0)
				continue;
			for (Stage stage : match.getStages()) {
				newStageScoreSheets.addAll(stage.getStageScoreSheets());
			}
			MatchService.save(match);
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

	private static void setStageScoreSheetsFromWinMSS(Match match) {
		for (Stage stage : match.getStages()) {
			stage.setStageScoreSheets(WinMSSStageScoreSheetRepository.find(stage.getMatch(), stage));
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
		progressCounterCompletedSteps = 0;
		progressCounterTotalSteps = 0;
		for (Match match : matches) {
			for (Stage stage : match.getStages()) {
				progressCounterTotalSteps += WinMSSStageScoreSheetRepository.getScoreSheetCountForStage(match, stage);
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
