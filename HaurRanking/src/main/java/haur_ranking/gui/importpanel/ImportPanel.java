package haur_ranking.gui.importpanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class ImportPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	ControlsPanel controlsPanel;
	WinMSSDatabasePane rightPane;

	public ImportPanel() {

		setLayout(new BorderLayout());
		add(new ControlsPanel(), BorderLayout.WEST);
		add(new WinMSSDatabasePane());

	}
}
