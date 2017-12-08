package haur_ranking.gui.rankingpanel;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class RankingPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RankingPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new RankingPanelTopPane());
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(new RankingPanelBottomPane());
		add(Box.createRigidArea(new Dimension(0, 30)));
	}
}
