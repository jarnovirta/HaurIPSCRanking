package haur_ranking.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import haur_ranking.gui.filters.WinMSSFileFilter;
import haur_ranking.service.ImportWinMSSResultsTask;
import haur_ranking.service.MatchService;
import haur_ranking.service.MatchService.ImportStatus;

public class DatabasePanel extends JPanel implements ImportProgressEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String lastMSSDbFileLocation = null;
	private JButton importResultsButton;
	private JFrame progressBarFrame;

	public DatabasePanel() {
		JPanel databasePanel = new JPanel();
		databasePanel.setLayout(new BoxLayout(databasePanel, BoxLayout.PAGE_AXIS));
		importResultsButton = new JButton("Tuo kilpailuja");
		importResultsButton.setActionCommand("importCompetitions");
		importResultsButton.addActionListener(new ButtonClickListener());
		this.add(importResultsButton);

		progressBarFrame = new JFrame("Tulosten tuonti");
		progressBarFrame.setLocationRelativeTo(this);
		progressBarFrame.setPreferredSize(new Dimension(550, 150));
		progressBarFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ImportProgressBarPanel progressBarPanel = new ImportProgressBarPanel(progressBarFrame, importResultsButton);
		progressBarPanel.setOpaque(true);
		progressBarFrame.setContentPane(progressBarPanel);
		progressBarFrame.pack();

		MatchService.addImportProgressEventListener(this);

	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("importCompetitions")) {
				importCompetitionsCommandHandler();

			}
		}
	}

	private void importCompetitionsCommandHandler() {
		JFileChooser fileChooser;
		if (lastMSSDbFileLocation != null)
			fileChooser = new JFileChooser(lastMSSDbFileLocation);
		else
			fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new WinMSSFileFilter());
		fileChooser.setPreferredSize(new Dimension(800, 600));
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String absoluteFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			lastMSSDbFileLocation = Paths.get(absoluteFilePath).getParent().toString();

			importResultsButton.setEnabled(false);
			// Import results in a separate thread.
			ImportWinMSSResultsTask importResultsTask = new ImportWinMSSResultsTask(
					MatchService.findNewResultsInWinMSSDatabase(absoluteFilePath));
			Thread importTaskThread = new Thread(importResultsTask);
			importTaskThread.start();
			progressBarFrame.setVisible(true);
		}
	}

	@Override
	public void setProgress(ImportProgressEvent event) {
		if (event.getImportStatus() == ImportStatus.DONE) {
			importResultsButton.setEnabled(true);
			progressBarFrame.setVisible(false);
			RankingDataService.updateRankingData();
		}
	}
}
