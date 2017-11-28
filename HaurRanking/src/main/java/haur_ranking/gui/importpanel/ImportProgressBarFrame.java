package haur_ranking.gui.importpanel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import haur_ranking.Event.GUIDataEvent;
import haur_ranking.Event.GUIDataEvent.GUIDataEventType;
import haur_ranking.Event.GUIDataEventListener;
import haur_ranking.service.MatchService;

public class ImportProgressBarFrame extends JFrame implements GUIDataEventListener {
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
	public void processData(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.DATA_IMPORT_PROGRESS) {
			progressBar.setValue(event.getProgressPercent());
		}
	}
}
