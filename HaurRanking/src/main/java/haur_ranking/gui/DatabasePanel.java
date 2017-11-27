package haur_ranking.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class DatabasePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	DatabasePanelLeftPane leftPane;
	DatabasePanelRightPane rightPane;

	public DatabasePanel() {

		gridBagLayout = new GridBagLayout();

		gridBagLayout.columnWidths = new int[] { MainWindow.WIDTH / 4, MainWindow.WIDTH / 4 * 3 };
		gridBagLayout.rowHeights = new int[] { MainWindow.HEIGHT };
		gridBagLayout.columnWeights = new double[] { 1, 1 };
		gridBagLayout.rowWeights = new double[] { 1 };

		gridBagConstraints = new GridBagConstraints();
		this.setLayout(gridBagLayout);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;

		// Add Panels
		leftPane = new DatabasePanelLeftPane();

		this.addPanels(0, 0, 1, 1, leftPane); // row, col, height, width
												// component

		rightPane = new DatabasePanelRightPane();
		this.addPanels(0, 1, 1, 2, rightPane);
	}

	private void addPanels(int row, int col, int height, int width, Component component) {
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = row;
		gridBagConstraints.gridheight = height;
		gridBagConstraints.gridwidth = width;
		gridBagLayout.setConstraints(component, gridBagConstraints);
		this.add(component);
	}
	//
	// public DatabasePanel() {
	// setLayout(new GridLayout(1, 2));
	// add(new DatabasePanelLeftPane());
	// add(new DatabasePanelRightPane());
	// }
}
