package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class RankingPanelBottomPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	ControlsPanel controlsPane;
	PreviousRankingsTable previousRankingsTablePane;

	RankingPanelBottomPane() {
		setLayout(new BorderLayout());
		add(new ControlsPanel(), BorderLayout.WEST);
		add(new PreviousRankingsTable());
	}
}
