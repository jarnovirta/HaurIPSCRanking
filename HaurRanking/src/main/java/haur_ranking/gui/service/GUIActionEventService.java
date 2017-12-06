package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.event.GUIActionEvent;
import haur_ranking.event.GUIActionEventListener;

public class GUIActionEventService {
	private static List<GUIActionEventListener> actionEventListeners = new ArrayList<GUIActionEventListener>();

	public static void addGUIActionEventListener(GUIActionEventListener eventListener) {
		actionEventListeners.add(eventListener);
	}

	public static void emit(GUIActionEvent event) {
		for (GUIActionEventListener listener : actionEventListeners) {
			listener.process(event);
		}
	}
}
