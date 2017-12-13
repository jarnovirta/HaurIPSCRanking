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
		CompetitorDataPanel table = new CompetitorDataPanel();

		add(controls, BorderLayout.WEST);
		controls.addButtonClickListener(table);
		add(table);
	}
}
