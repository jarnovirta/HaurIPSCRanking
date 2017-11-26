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

public class DatabasePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String lastMSSDbFileLocation = null;
	private JButton importResultsButton;
	private JFrame progressBarFrame;

	public DatabasePanel() {
		progressBarFrame = new JFrame("Tulosten tuonti");
		progressBarFrame.setLocationRelativeTo(this);
		progressBarFrame.setPreferredSize(new Dimension(550, 150));
		progressBarFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel databasePanel = new JPanel();
		databasePanel.setLayout(new BoxLayout(databasePanel, BoxLayout.PAGE_AXIS));
		importResultsButton = new JButton("Tuo kilpailuja");
		importResultsButton.setActionCommand("importCompetitions");
		importResultsButton.addActionListener(new ButtonClickListener());
		this.add(importResultsButton);
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
			ImportResultsPanel importWinMSSDataTask = new ImportResultsPanel(progressBarFrame, absoluteFilePath,
					importResultsButton);
			importWinMSSDataTask.setOpaque(true);
			progressBarFrame.setContentPane((importWinMSSDataTask));
			progressBarFrame.pack();
			progressBarFrame.setVisible(true);
		}
	}

}
