package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ResultsDataPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ResultsDataPane() {
		setLayout(new BorderLayout());
		add(new StatisticsAndControlsPanel(), BorderLayout.WEST);
		add(new DatabaseMatchInfoTable());
	}
}
