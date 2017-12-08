package haur_ranking.gui.rankingpanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import haur_ranking.gui.MainWindow;

public class RankingPanelBottomPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	PreviousRankingsControlsPanel controlsPane;
	PreviousRankingsTable previousRankingsTablePane;

	RankingPanelBottomPane() {
		gridBagLayout = new GridBagLayout();

		gridBagLayout.columnWidths = new int[] { MainWindow.WIDTH / 4, MainWindow.WIDTH / 4 * 3 };
		int verticalSpacingBetweenPanes = 60;
		gridBagLayout.rowHeights = new int[] { (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2 };
		gridBagLayout.columnWeights = new double[] { 1, 1 };
		gridBagLayout.rowWeights = new double[] { 1 };

		gridBagConstraints = new GridBagConstraints();
		this.setLayout(gridBagLayout);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;

		controlsPane = new PreviousRankingsControlsPanel();

		this.addPanels(0, 0, 1, 1, controlsPane); // row, col, height, width
													// component

		previousRankingsTablePane = new PreviousRankingsTable();
		this.addPanels(0, 1, 1, 2, previousRankingsTablePane);
	}

	private void addPanels(int row, int col, int height, int width, Component component) {
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = row;
		gridBagConstraints.gridheight = height;
		gridBagConstraints.gridwidth = width;
		gridBagLayout.setConstraints(component, gridBagConstraints);
		this.add(component);
	}
}
