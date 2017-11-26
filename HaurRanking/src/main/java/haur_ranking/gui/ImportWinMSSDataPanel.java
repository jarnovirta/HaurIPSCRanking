package haur_ranking.gui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import haur_ranking.service.ImportWinMSSResultsTask;
import haur_ranking.service.MatchService;

public class ImportWinMSSDataPanel extends JPanel implements PropertyChangeListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JButton importButton;
	private Thread importTaskThread;

	private JFrame progressBarFrame;

	public ImportWinMSSDataPanel(JFrame frame, String winMSSFilePath, JButton importButton) {
		super(new BorderLayout());
		this.progressBarFrame = frame;
		this.importButton = importButton;
		importButton.setEnabled(false);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);
		this.add(panel, BorderLayout.PAGE_START);

		// Import results in a separate thread.
		ImportWinMSSResultsTask importResultsTask = new ImportWinMSSResultsTask(
				MatchService.findNewResultsInWinMSSDatabase(winMSSFilePath));
		importTaskThread = new Thread(importResultsTask);
		importTaskThread.start();

		// Query MatchService for progress of results import and show progress
		// with progress bar.
		ImportProgressQueryingTask progressQueryTask = new ImportProgressQueryingTask(progressBarFrame);
		progressQueryTask.addPropertyChangeListener(this);
		progressQueryTask.execute();
	}

	class ImportProgressQueryingTask extends SwingWorker<Void, Void> {
		private JFrame progressBarFrame;

		public ImportProgressQueryingTask(JFrame progressBarFrame) {

			this.progressBarFrame = progressBarFrame;
		}

		@Override
		public Void doInBackground() {
			while ((MatchService.getImportStatus() == MatchService.ImportStatus.READY)
					|| MatchService.getImportStatus() == MatchService.ImportStatus.IMPORTING) {
				try {
					setProgress(MatchService.getProgressPercentage());
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MatchService.setImportStatus(MatchService.ImportStatus.READY);
			RankingDataService.updateRankingData();
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			importButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			progressBarFrame.setVisible(false);
		}
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
}
