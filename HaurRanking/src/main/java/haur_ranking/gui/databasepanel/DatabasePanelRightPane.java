package haur_ranking.gui.databasepanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DatabasePanelRightPane extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DatabasePanelRightPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new DatabaseMatchInfoTable());
		add(new DatabaseCompetitorInfoTable());
	}
}
