package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class CompetitorPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CompetitorPane() {
		setLayout(new BorderLayout());
		CompetitorControlsPanel controls = new CompetitorControlsPanel();
		add(new CompetitorControlsPanel(), BorderLayout.WEST);

		CompetitorDataPanel table = new CompetitorDataPanel();
		controls.addButtonClickListener(table);
		add(new CompetitorDataPanel());
	}
}
