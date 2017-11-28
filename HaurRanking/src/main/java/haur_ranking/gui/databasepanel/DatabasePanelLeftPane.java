package haur_ranking.gui.databasepanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DatabasePanelLeftPane extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DatabasePanelLeftPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(10, 40, 0, 0));
		add(new DatabaseStatisticsPanel());
	}
}
