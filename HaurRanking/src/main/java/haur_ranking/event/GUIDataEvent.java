package haur_ranking.event;

public class GUIDataEvent {
	public enum GUIDataEventType {
		GUI_DATA_UPDATE, WINMSS_DATA_IMPORT_EVENT
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
