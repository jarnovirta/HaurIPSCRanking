package haur_ranking.gui.rankingpanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class RankingPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RankingPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new CurrentRankingPane());
		add(new JSeparator());
		add(new PreviousRankingsPane());
	}
}
