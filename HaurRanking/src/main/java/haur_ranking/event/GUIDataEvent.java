package haur_ranking.event;

import java.util.List;

import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Ranking;

public class GUIDataEvent {
	public enum GUIDataEventType {
		GUI_DATA_UPDATE, WINMSS_DATA_IMPORT_EVENT
	}

	private GUIDataEventType eventType;
	private DataImportEvent dataImportEvent;
	private Ranking ranking;
	private DatabaseStatistics databaseStatistics;
	private List<String[]> importedMatchesTableData;
	private List<String[]> previousRankingsTableData;

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

	public DataImportEvent getDataImportEvent() {
		return dataImportEvent;
	}

	public void setDataImportEvent(DataImportEvent dataImportEvent) {
		this.dataImportEvent = dataImportEvent;
	}

	public List<String[]> getPreviousRankingsTableData() {
		return previousRankingsTableData;
	}

	public void setPreviousRankingsTableData(List<String[]> previousRankingsTableData) {
		this.previousRankingsTableData = previousRankingsTableData;
	}

}
