package haur_ranking.gui.databasepanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class DatabasePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;

	public DatabasePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new MatchPane());
		add(new JSeparator(SwingConstants.HORIZONTAL));
		add(new CompetitorPane());
	}

}
