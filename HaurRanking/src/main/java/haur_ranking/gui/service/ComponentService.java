package haur_ranking.gui.service;

import haur_ranking.gui.databasepanel.DatabaseControlsPanel;

public class ComponentService {
	private static DatabaseControlsPanel databaseControlsPanel;

	public static DatabaseControlsPanel getDatabaseControlsPanel() {
		return databaseControlsPanel;
	}

	public static void setDatabaseControlsPanel(DatabaseControlsPanel databaseControlsPanel) {
		ComponentService.databaseControlsPanel = databaseControlsPanel;
	}

}
