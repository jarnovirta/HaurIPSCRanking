package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.Ranking;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.filter.FileFilterUtils;
import haur_ranking.gui.filter.PdfFileFilter;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.RankingPanelDataService;
import haur_ranking.service.RankingService;
import haur_ranking.utils.DateFormatUtils;

public class ControlsPanel extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static JButton choosePreviousRankingsToDeleteButton;
	private static JButton deleteRankingsButton;
	private static JButton cancelDeleteButton;
	private static JButton pdfButton;

	private ButtonClickListener buttonClickListener = new ButtonClickListener();

	private static String lastRankingPdfFileLocation;

	public ControlsPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 20));
		add(getGeneratePdfPanel(), BorderLayout.NORTH);
		add(getDeleteOldRankingsInstructionPanel(), BorderLayout.SOUTH);
		DataEventService.addDataEventListener(this);

	}

	private JPanel getGeneratePdfPanel() {
		JPanel generatePdfPanel = new JPanel();
		generatePdfPanel.setLayout(new BoxLayout(generatePdfPanel, BoxLayout.Y_AXIS));

		JLabel firstLine = new JLabel("Muodosta pdf");
		firstLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		generatePdfPanel.add(firstLine);
		generatePdfPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel secondLine = new JLabel(
				"<html>Valitse vertailu-ranking.\nTulosparannukset näkyvät lihavoituina.</html>");
		secondLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		secondLine.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		secondLine.setFont(new Font(secondLine.getFont().getName(), Font.PLAIN, secondLine.getFont().getSize()));
		generatePdfPanel.add(secondLine);

		generatePdfPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		pdfButton = new JButton("Tallenna Pdf");
		pdfButton.setActionCommand(RankingPanelButtonCommands.GENERATE_RANKING_PDF.toString());
		pdfButton.addActionListener(buttonClickListener);
		generatePdfPanel.add(pdfButton);
		setPdfButtonEnabled();

		return generatePdfPanel;
	}

	private JPanel getDeleteOldRankingsInstructionPanel() {
		JPanel deleteOldRankingsPanel = new JPanel();
		deleteOldRankingsPanel.setLayout(new BoxLayout(deleteOldRankingsPanel, BoxLayout.Y_AXIS));

		JLabel firstLine = new JLabel("<html>Poista vanhoja ranking-\ntietoja</html>");
		firstLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		deleteOldRankingsPanel.add(firstLine);
		firstLine.setFont(new Font(firstLine.getFont().getName(), Font.BOLD, firstLine.getFont().getSize()));

		deleteOldRankingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		choosePreviousRankingsToDeleteButton = new JButton("Valitse");
		choosePreviousRankingsToDeleteButton
				.setActionCommand(RankingPanelButtonCommands.CHOOSE_RANKINGS_TO_DELETE.toString());
		choosePreviousRankingsToDeleteButton.addActionListener(buttonClickListener);
		choosePreviousRankingsToDeleteButton.setEnabled(false);
		buttonsPanel.add(choosePreviousRankingsToDeleteButton, BorderLayout.WEST);
		setChoosePreviousRankingsToDeleteButtonEnabled();

		JPanel deleteCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		deleteRankingsButton = new JButton("Poista");
		deleteRankingsButton.setActionCommand(RankingPanelButtonCommands.DELETE_RANKINGS.toString());
		deleteRankingsButton.addActionListener(buttonClickListener);
		deleteRankingsButton.setEnabled(false);

		deleteCancelButtonsPanel.add(deleteRankingsButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand(RankingPanelButtonCommands.CANCEL_DELETE_RANKINGS.toString());
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);

		deleteCancelButtonsPanel.add(cancelDeleteButton);

		buttonsPanel.add(deleteCancelButtonsPanel, BorderLayout.EAST);

		deleteOldRankingsPanel.add(buttonsPanel);
		return deleteOldRankingsPanel;
	}

	private void chooseRankingsToDeleteCommandHandler() {
		choosePreviousRankingsToDeleteButton.setEnabled(false);
		deleteRankingsButton.setEnabled(true);
		cancelDeleteButton.setEnabled(true);
		setPdfButtonEnabled();

	}

	private void cancelDeleteCommandHandler() {
		setChoosePreviousRankingsToDeleteButtonEnabled();
		deleteRankingsButton.setEnabled(false);
		cancelDeleteButton.setEnabled(false);
		setPdfButtonEnabled();
	}

	private void deleteRankingsCommandHandler() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Delete previous rankin(s)?", "Confirm", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			RankingPanelDataService.deletePreviousRankings();
		}
		deleteRankingsButton.setEnabled(false);
		cancelDeleteButton.setEnabled(false);
		setChoosePreviousRankingsToDeleteButtonEnabled();
		setPdfButtonEnabled();
	}

	private void generatePdfCommandHandler() {
		pdfButton.setEnabled(false);
		JFileChooser fileChooser;
		if (lastRankingPdfFileLocation != null)
			fileChooser = new JFileChooser(lastRankingPdfFileLocation);
		else
			fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new PdfFileFilter());
		fileChooser.setPreferredSize(new Dimension(800, 600));
		String dateString = DateFormatUtils.calendarToDateString(Calendar.getInstance()).replace('.', '_');
		fileChooser.setSelectedFile(new File("HaurRanking_" + dateString + ".pdf"));
		fileChooser.setApproveButtonText("Save");

		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String absoluteFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			String fileExtension = FileFilterUtils.getExtension(new File(absoluteFilePath));
			if (fileExtension == null)
				absoluteFilePath += ".pdf";

			lastRankingPdfFileLocation = Paths.get(absoluteFilePath).getParent().toString();
			RankingService.createPdfRankingFile(RankingPanelDataService.getRanking(),
					RankingPanelDataService.getPreviousRankingsTableSelectedRanking(), absoluteFilePath);
		} else {
			if (returnVal != JFileChooser.CANCEL_OPTION)
				generatePdfCommandHandler();
		}
		setPdfButtonEnabled();

	}

	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand().equals(RankingPanelButtonCommands.CHOOSE_RANKINGS_TO_DELETE.toString())) {
				chooseRankingsToDeleteCommandHandler();
			}
			if (event.getActionCommand().equals(RankingPanelButtonCommands.CANCEL_DELETE_RANKINGS.toString())) {
				cancelDeleteCommandHandler();
			}
			if (event.getActionCommand().equals(RankingPanelButtonCommands.DELETE_RANKINGS.toString())) {
				deleteRankingsCommandHandler();
			}
			if (event.getActionCommand().equals(RankingPanelButtonCommands.GENERATE_RANKING_PDF.toString())) {
				generatePdfCommandHandler();
			}
		}
	}

	public void addButtonClickListener(ActionListener listener) {
		choosePreviousRankingsToDeleteButton.addActionListener(listener);
		deleteRankingsButton.addActionListener(listener);
		cancelDeleteButton.addActionListener(listener);
		pdfButton.addActionListener(listener);

	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.PREVIOUS_RANKINGS_TABLE_UPDATE) {
			setChoosePreviousRankingsToDeleteButtonEnabled();
			deleteRankingsButton.setEnabled(false);
			cancelDeleteButton.setEnabled(false);

		}
		if (event.getEventType() == GUIDataEventType.RANKING_TABLE_UPDATE) {
			setPdfButtonEnabled();
		}
	}

	private void setChoosePreviousRankingsToDeleteButtonEnabled() {
		if (RankingPanelDataService.getPreviousRankingsTableData() != null
				&& RankingPanelDataService.getPreviousRankingsTableData().size() > 0) {
			choosePreviousRankingsToDeleteButton.setEnabled(true);
		} else
			choosePreviousRankingsToDeleteButton.setEnabled(false);

	}

	private void setPdfButtonEnabled() {
		Ranking ranking = RankingPanelDataService.getRanking();
		boolean enabled = false;

		if (ranking != null && ranking.getDivisionRankings() != null && ranking.getDivisionRankings().size() > 0) {
			for (DivisionRanking divRanking : ranking.getDivisionRankings()) {
				if (divRanking.getDivisionRankingRows() != null && divRanking.getDivisionRankingRows().size() > 0) {
					enabled = true;
				}
			}
		}
		pdfButton.setEnabled(enabled);
	}
}
