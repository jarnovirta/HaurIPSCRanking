package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MatchPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MatchPane() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		MatchStatisticsAndControlsPanel controls = new MatchStatisticsAndControlsPanel();
		MatchDataPanel dataPanel = new MatchDataPanel();
		controls.addButtonClickListener(dataPanel);
		add(controls, BorderLayout.WEST);
		add(dataPanel);
	}
}
