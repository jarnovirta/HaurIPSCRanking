package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CompetitorPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CompetitorPane() {
		setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));
		setLayout(new BorderLayout());
		CompetitorControlsPanel controls = new CompetitorControlsPanel();
		CompetitorDataPanel table = new CompetitorDataPanel();
		add(controls, BorderLayout.WEST);
		controls.addButtonClickListener(table);
		add(table);
	}
}
