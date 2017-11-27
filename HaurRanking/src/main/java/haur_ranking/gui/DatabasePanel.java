package haur_ranking.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class DatabasePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DatabasePanel() {
		setLayout(new GridLayout(1, 2));
		add(new DatabasePanelLeftPane());
		add(new DatabasePanelRightPane());
	}
}
