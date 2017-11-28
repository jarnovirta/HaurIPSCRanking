package haur_ranking.gui.importpanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImportResultsLeftPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ImportResultsLeftPane() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, 20, 0, 0));
		add(new ImportResultsControlPanel());
	}
}
