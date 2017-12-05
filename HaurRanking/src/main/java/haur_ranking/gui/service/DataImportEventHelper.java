package haur_ranking.gui.service;

import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEventListener;

// Helper class used to register to MatchService as event listener and
// pass data to GUIDataService. GUIDataService is static and cannot be registered
public class DataImportEventHelper implements DataImportEventListener {
	@Override
	public void processData(DataImportEvent event) {
		GUIDataService.processData(event);
	}
}
