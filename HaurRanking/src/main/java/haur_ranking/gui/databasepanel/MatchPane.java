package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class MatchPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MatchPane() {
		setLayout(new BorderLayout());
		add(new MatchStatisticsAndControlsPanel(), BorderLayout.WEST);
		add(new MatchDataPanel());
	}
}
