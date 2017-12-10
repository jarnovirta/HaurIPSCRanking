package haur_ranking.gui.databasepanel;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import haur_ranking.gui.MainWindow;

public class DatabasePanelLeftPane extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DatabasePanelLeftPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(MainWindow.LEFT_PANE_WIDTH, MainWindow.HEIGHT));
		setBorder(new EmptyBorder(10, 40, 0, 0));
		add(new StatisticsAndControlsPanel());
		add(new DatabaseControlsPanel());
	}
}
