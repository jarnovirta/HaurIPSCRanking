package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class PreviousRankingsPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	ControlsPanel controlsPane;
	PreviousRankingsTable previousRankingsTablePane;

	PreviousRankingsPane() {
		setLayout(new BorderLayout());
		ControlsPanel controlsPanel = new ControlsPanel();
		add(new ControlsPanel(), BorderLayout.WEST);
		PreviousRankingsTable previousRankingsTable = new PreviousRankingsTable();
		controlsPanel.addButtonClickListener(previousRankingsTable);
		add(previousRankingsTable);
	}
}
