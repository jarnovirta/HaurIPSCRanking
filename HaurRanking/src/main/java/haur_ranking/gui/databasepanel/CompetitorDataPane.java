package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class CompetitorDataPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CompetitorDataPane() {
		setLayout(new BorderLayout());
		add(new CompetitorDataControlsPanel(), BorderLayout.WEST);
		add(new DatabaseCompetitorInfoTable());
	}
}
