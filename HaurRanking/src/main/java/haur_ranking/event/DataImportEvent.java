package haur_ranking.event;

import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;

public class DataImportEvent {

	public enum DataImportEventType {
		IMPORT_STATUS_CHANGE, IMPORT_PROGRESS
	}

	public enum ImportStatus {
		LOADING_FROM_WINMSS, SAVING_TO_HAUR_RANKING_DB, GENERATING_RANKING, SAVE_TO_HAUR_RANKING_DB_DONE, LOAD_FROM_WINMSS_DONE, ERROR;
	};

	private DataImportEventType dataImportEventType;
	private ImportStatus importStatus;
	private int progressPercent;
	private List<Match> winMSSMatches;
	private int newScoreSheetsCount;
	private int newStagesCount;
	private int newMatchesCount;
	private int newCompetitorsCount;
	private int oldScoreSheetsRemovedCount;

	private List<Stage> invalidClassifiers;

	private List<Stage> stagesWithNoScoreSheets;

	public DataImportEvent(DataImportEventType eventType) {
		this.dataImportEventType = eventType;
	}

	public ImportStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(ImportStatus importStatus) {
		this.importStatus = importStatus;
	}

	public int getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}

	public DataImportEventType getDataImportEventType() {
		return dataImportEventType;
	}

	public void setDataImportEventType(DataImportEventType dataImportEventType) {
		this.dataImportEventType = dataImportEventType;
	}

	public List<Match> getWinMSSMatches() {
		return winMSSMatches;
	}

	public void setWinMSSMatches(List<Match> winMSSMatches) {
		this.winMSSMatches = winMSSMatches;
	}

	public int getNewScoreSheetsCount() {
		return newScoreSheetsCount;
	}

	public void setNewScoreSheetsCount(int newScoreSheetsCount) {
		this.newScoreSheetsCount = newScoreSheetsCount;
	}

	public int getNewStagesCount() {
		return newStagesCount;
	}

	public void setNewStagesCount(int newStagesCount) {
		this.newStagesCount = newStagesCount;
	}

	public int getNewMatchesCount() {
		return newMatchesCount;
	}

	public void setNewMatchesCount(int newMatchesCount) {
		this.newMatchesCount = newMatchesCount;
	}

	public int getNewCompetitorsCount() {
		return newCompetitorsCount;
	}

	public void setNewCompetitorsCount(int newCompetitorsCount) {
		this.newCompetitorsCount = newCompetitorsCount;
	}

	public int getOldScoreSheetsRemovedCount() {
		return oldScoreSheetsRemovedCount;
	}

	public void setOldScoreSheetsRemovedCount(int oldScoreSheetsRemovedCount) {
		this.oldScoreSheetsRemovedCount = oldScoreSheetsRemovedCount;
	}

	public List<Stage> getInvalidClassifiers() {
		return invalidClassifiers;
	}

	public void setInvalidClassifiers(List<Stage> invalidClassifiers) {
		this.invalidClassifiers = invalidClassifiers;
	}

	public List<Stage> getStagesWithNoScoreSheets() {
		return stagesWithNoScoreSheets;
	}

	public void setStagesWithNoScoreSheets(List<Stage> stagesWithNoScoreSheets) {
		this.stagesWithNoScoreSheets = stagesWithNoScoreSheets;
	}

}
