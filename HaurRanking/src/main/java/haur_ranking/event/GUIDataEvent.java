package haur_ranking.event;

import java.util.List;

import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Ranking;
import haur_ranking.service.MatchService.ImportStatus;

public class GUIDataEvent {
	public enum GUIDataEventType {
		NEW_HAUR_RANKING_DB_DATA, NEW_WINMSS_DB_DATA, DATA_IMPORT_PROGRESS
	}

	private GUIDataEventType eventType;
	private Ranking ranking;
	private DatabaseStatistics databaseStatistics;
	private List<String[]> importedMatchesTableData;
	private int progressPercent;
	private ImportStatus importStatus;

	public GUIDataEvent() {
	}

	public GUIDataEvent(GUIDataEventType guiDataEventType) {
		this.eventType = guiDataEventType;
	}

	public GUIDataEventType getEventType() {
		return eventType;
	}

	public void setEventType(GUIDataEventType eventType) {
		this.eventType = eventType;
	}

	public Ranking getRanking() {
		return ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	public DatabaseStatistics getDatabaseStatistics() {
		return databaseStatistics;
	}

	public void setDatabaseStatistics(DatabaseStatistics databaseStatistics) {
		this.databaseStatistics = databaseStatistics;
	}

	public List<String[]> getImportedMatchesTableData() {
		return importedMatchesTableData;
	}

	public void setImportedMatchesTableData(List<String[]> importedMatchesTableData) {
		this.importedMatchesTableData = importedMatchesTableData;
	}

	public int getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}

	public ImportStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(ImportStatus importStatus) {
		this.importStatus = importStatus;
	}

}
