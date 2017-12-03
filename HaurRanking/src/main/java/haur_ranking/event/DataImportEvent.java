package haur_ranking.event;

import java.util.List;

import haur_ranking.domain.Match;

public class DataImportEvent {

	public enum DataImportEventType {
		IMPORT_STATUS_CHANGE, IMPORT_PROGRESS
	}

	public enum ImportStatus {
		LOADING_FROM_WINMSS, SAVING_TO_HAUR_RANKING_DB, GENERATING_RANKING, SAVE_TO_HAUR_RANKING_DB_DONE, LOAD_FROM_WINMSS_DONE
	};

	private DataImportEventType dataImportEventType;
	private ImportStatus importStatus;
	private int progressPercent;
	private List<Match> winMSSMatches;

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

}
