package haur_ranking.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import haur_ranking.Event.ImportProgressEvent;
import haur_ranking.Event.ImportProgressEventListener;
import haur_ranking.service.MatchService;

public class ImportProgressBarPanel extends JPanel implements ImportProgressEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;

	public ImportProgressBarPanel(JFrame frame, JButton importButton) {
		super(new BorderLayout());
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);
		this.add(panel, BorderLayout.PAGE_START);
		MatchService.addImportProgressEventListener(this);
	}

	@Override
	public void setProgress(ImportProgressEvent event) {
		progressBar.setValue(event.getProgressPercent());
	}
}
