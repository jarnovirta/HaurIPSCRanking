package haur_ranking.gui.rankingpanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import haur_ranking.gui.MainWindow;

public class RankingPanelTopPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;

	RankingTable rankingTablePane;

	public RankingPanelTopPane() {
		gridBagLayout = new GridBagLayout();

		int verticalSpacingBetweenPanes = 60;
		gridBagLayout.columnWidths = new int[] { MainWindow.WIDTH / 4, MainWindow.WIDTH / 4 * 3 };
		gridBagLayout.rowHeights = new int[] { (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2 };
		gridBagLayout.columnWeights = new double[] { 1, 1 };
		gridBagLayout.rowWeights = new double[] { 1 };

		gridBagConstraints = new GridBagConstraints();
		this.setLayout(gridBagLayout);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;

		JPanel emptyLeftPane = new JPanel();

		this.addPanels(0, 0, 1, 1, emptyLeftPane); // row, col, height, width
													// component

		rankingTablePane = new RankingTable();
		this.addPanels(0, 1, 1, 2, rankingTablePane);
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
