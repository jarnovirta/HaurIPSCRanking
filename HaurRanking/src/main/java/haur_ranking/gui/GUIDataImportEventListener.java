package haur_ranking.gui;

import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEventListener;

// Helper class used to register to MatchService as event listener and
// pass data to GUIDataService. GUIDataService is static and cannot be registered
public class GUIDataImportEventListener implements DataImportEventListener {
	@Override
	public void processData(DataImportEvent event) {
		GUIDataService.processData(event);
	}
}
