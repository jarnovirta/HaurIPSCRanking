package haur_ranking.gui.service;

import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEventListener;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;

// Helper class used to register to MatchService as event listener and
// pass data to GUIDataService. GUIDataService is static and cannot be registered
public class DataImportEventHelper implements DataImportEventListener {
	@Override
	public void processData(DataImportEvent event) {
		GUIDataEvent guiEvent = new GUIDataEvent();
		guiEvent.setEventType(GUIDataEventType.WINMSS_DATA_IMPORT_EVENT);
		guiEvent.setDataImportEvent(event);
		DataEventService.emit(guiEvent);
	}
}
