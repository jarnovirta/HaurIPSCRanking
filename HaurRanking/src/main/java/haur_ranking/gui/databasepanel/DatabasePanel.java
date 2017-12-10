package haur_ranking.gui.databasepanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new ResultsDataPane());
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(new CompetitorDataPane());
		add(Box.createRigidArea(new Dimension(0, 30)));
	}

}
