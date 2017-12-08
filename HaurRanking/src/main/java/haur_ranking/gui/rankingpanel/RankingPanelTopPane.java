package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
		setLayout(new BorderLayout());

		JPanel emptyLeftPanel = new JPanel();
		int verticalSpacingBetweenPanes = 60;

		emptyLeftPanel.setPreferredSize(new Dimension(500, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		add(emptyLeftPanel, BorderLayout.WEST);
		add(new RankingTable());

		// gridBagLayout = new GridBagLayout();
		//
		// int verticalSpacingBetweenPanes = 60;
		// gridBagLayout.columnWidths = new int[] { ((Long)
		// Math.round(MainWindow.WIDTH * 0.35)).intValue(),
		// ((Long) Math.round(MainWindow.WIDTH * .75)).intValue() };
		// gridBagLayout.rowHeights = new int[] { (MainWindow.HEIGHT -
		// verticalSpacingBetweenPanes) / 2 };
		// gridBagLayout.columnWeights = new double[] { 1, 1 };
		// gridBagLayout.rowWeights = new double[] { 1 };
		//
		// gridBagConstraints = new GridBagConstraints();
		// this.setLayout(gridBagLayout);
		//
		// gridBagConstraints.fill = GridBagConstraints.BOTH;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.weighty = 1.0;
		//
		// JPanel emptyLeftPane = new JPanel();
		//
		// this.addPanels(0, 0, 1, 1, emptyLeftPane); // row, col, height, width
		// // component
		//
		// rankingTablePane = new RankingTable();
		// this.addPanels(0, 1, 1, 2, rankingTablePane);
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
