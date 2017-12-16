package haur_ranking.event;

public class GUIDataEvent {
	public enum GUIDataEventType {
		WINMSS_DATA_IMPORT_EVENT, NEW_RANKING_GENERATED, RANKING_TABLE_UPDATE, PREVIOUS_RANKINGS_TABLE_UPDATE, DATABASE_MATCH_TABLE_UPDATE, DATABASE_COMPETITOR_TABLE_UPDATE, DATABASE_STATISTICS_TABLE_UPDATE, IMPORT_RESULTS_TABLE_UPDATE;
	}

	private GUIDataEventType eventType;
	private DataImportEvent dataImportEvent;

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

	public DataImportEvent getDataImportEvent() {
		return dataImportEvent;
	}

	public void setDataImportEvent(DataImportEvent dataImportEvent) {
		this.dataImportEvent = dataImportEvent;
	}

}
