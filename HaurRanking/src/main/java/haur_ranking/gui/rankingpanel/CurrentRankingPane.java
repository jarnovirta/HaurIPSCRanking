package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import haur_ranking.gui.MainWindow;

public class CurrentRankingPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;

	public CurrentRankingPane() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		JPanel emptyLeftPanel = new JPanel();
		int verticalSpacingBetweenPanes = 60;
		emptyLeftPanel.setPreferredSize(new Dimension(500, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		add(new CurrentRankingInfoPanel(), BorderLayout.WEST);
		add(new RankingTable());

	}

}
