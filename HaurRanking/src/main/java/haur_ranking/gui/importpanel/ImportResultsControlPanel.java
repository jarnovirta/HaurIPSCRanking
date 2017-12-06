package haur_ranking.gui.importpanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.filter.WinMSSFileFilter;
import haur_ranking.gui.service.GUIDataService;

public class ImportResultsControlPanel extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String lastMSSDbFileLocation = null;
	private JButton importResultsButton;
	private JButton loadWinMSSDataButton;
	private JButton clearImportAsClassifierSelectionsButton;
	private JFrame progressBarFrame;
	ButtonClickListener buttonClickListener = new ButtonClickListener();

	public ImportResultsControlPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0, 20)));
		loadWinMSSDataButton = new JButton("Avaa WinMSS tiedosto");
		loadWinMSSDataButton.setActionCommand("loadWinMSSData");
		loadWinMSSDataButton.addActionListener(buttonClickListener);
		this.add(loadWinMSSDataButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		clearImportAsClassifierSelectionsButton = new JButton("Poista tallennusvalinnat");
		clearImportAsClassifierSelectionsButton.setActionCommand("clearImportAsClassifierSelections");
		clearImportAsClassifierSelectionsButton.addActionListener(buttonClickListener);
		clearImportAsClassifierSelectionsButton.setEnabled(false);
		this.add(clearImportAsClassifierSelectionsButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		importResultsButton = new JButton("Tuo tulokset");
		importResultsButton.setActionCommand("importResults");
		importResultsButton.addActionListener(buttonClickListener);
		importResultsButton.setEnabled(false);
		this.add(importResultsButton);
		progressBarFrame = new ImportProgressBarFrame();
		progressBarFrame.setLocationRelativeTo(this);
		GUIDataService.addDataEventListener(this);

	}

	private void loadWinMSSDataCommandHandler() {
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
			loadWinMSSDataButton.setEnabled(false);
			progressBarFrame.setVisible(true);
			GUIDataService.loadDataFromWinMSS(absoluteFilePath);
		}
	}

	private void importResultsCommandHandler() {
		progressBarFrame.setVisible(true);
		GUIDataService.saveResultsToHaurRankingDatabase();
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT
				&& event.getDataImportEvent().getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE) {
			if (event.getDataImportEvent().getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
				loadWinMSSDataButton.setEnabled(true);
				clearImportAsClassifierSelectionsButton.setEnabled(false);
				importResultsButton.setEnabled(false);
				progressBarFrame.setVisible(false);
			}
			if (event.getDataImportEvent().getImportStatus() == ImportStatus.LOAD_FROM_WINMSS_DONE) {
				loadWinMSSDataButton.setEnabled(true);
				importResultsButton.setEnabled(true);
				clearImportAsClassifierSelectionsButton.setEnabled(true);
				progressBarFrame.setVisible(false);
			}
		}

	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("loadWinMSSData")) {
				loadWinMSSDataCommandHandler();
			}
			if (command.equals("importResults")) {
				importResultsCommandHandler();
			}
			if (command.equals("clearImportAsClassifierSelections")) {
				GUIDataService.clearImportAsClassifierSelections();
			}
		}
	}
}
