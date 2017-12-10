package haur_ranking.gui.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RankingPanelActionEventService {
	private static List<ActionListener> buttonClickListeners = new ArrayList<ActionListener>();

	public static enum RankingPanelButtonCommands {
		CHOOSE_RANKINGS_TO_DELETE, DELETE_RANKINGS, CANCEL_DELETE_RANKINGS, GENERATE_RANKING_PDF;
	}

	public static void addButtonClickListener(ActionListener listener) {
		buttonClickListeners.add(listener);
	}

	public static void emit(ActionEvent event) {
		for (ActionListener listener : buttonClickListeners)
			listener.actionPerformed(event);
	}
}
