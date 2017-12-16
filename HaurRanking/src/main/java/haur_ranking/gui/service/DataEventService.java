package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.service.MatchService;

public class DataEventService {
	private static List<GUIDataEventListener> dataEventListeners = new ArrayList<GUIDataEventListener>();

	public static void init() {
		MatchService.addImportProgressEventListener(new DataImportEventHelper());
	}

	public static void addDataEventListener(GUIDataEventListener listener) {

		dataEventListeners.add(listener);
	}

	public static void emit(GUIDataEvent event) {
		RankingPanelDataService.process(event);
		DatabasePanelDataService.process(event);
		ImportPanelDataService.process(event);
		for (GUIDataEventListener listener : dataEventListeners) {
			listener.process(event);
		}
	}
}
