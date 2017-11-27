package haur_ranking.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import haur_ranking.Event.ImportProgressEvent;
import haur_ranking.Event.ImportProgressEventListener;
import haur_ranking.service.MatchService;

public class ImportProgressBarFrame extends JFrame implements ImportProgressEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JProgressBar progressBar;

	public ImportProgressBarFrame() {
		super("Tulosten tuonti");
		setPreferredSize(new Dimension(550, 150));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JPanel progressBarPanel = new JPanel();
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		progressBarPanel.add(progressBar);
		progressBarPanel.setOpaque(true);
		setContentPane(progressBarPanel);
		pack();
		MatchService.addImportProgressEventListener(this);

	}

	@Override
	public void setProgress(ImportProgressEvent event) {
		progressBar.setValue(event.getProgressPercent());
	}
}
