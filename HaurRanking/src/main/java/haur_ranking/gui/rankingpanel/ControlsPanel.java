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
import javax.swing.JPanel;

import haur_ranking.gui.MainWindow;
import haur_ranking.gui.filter.FileFilterUtils;
import haur_ranking.gui.filter.PdfFileFilter;
import haur_ranking.gui.service.DataService;
import haur_ranking.gui.service.RankingPanelActionEventService;
import haur_ranking.gui.service.RankingPanelActionEventService.RankingPanelButtonCommands;
import haur_ranking.service.RankingService;
import haur_ranking.utils.DateFormatUtils;

public class ControlsPanel extends JPanel {

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
		setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 20));
		add(getGeneratePdfPanel(), BorderLayout.NORTH);
		add(getDeleteOldRankingsInstructionPanel(), BorderLayout.SOUTH);

	}

	private JPanel getGeneratePdfPanel() {
		JPanel generatePdfPanel = new JPanel();
		generatePdfPanel.setLayout(new BoxLayout(generatePdfPanel, BoxLayout.Y_AXIS));

		JLabel firstLine = new JLabel("Valitse vertailu-ranking");
		firstLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		generatePdfPanel.add(firstLine);
		firstLine.setFont(new Font(firstLine.getFont().getName(), Font.BOLD, firstLine.getFont().getSize()));
		generatePdfPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel secondLine = new JLabel("Tulosparannukset näkyvät pdf:ssä");
		secondLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		secondLine.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		secondLine.setFont(new Font(secondLine.getFont().getName(), Font.PLAIN, secondLine.getFont().getSize()));
		generatePdfPanel.add(secondLine);

		JLabel thirdLine = new JLabel("lihavoituina.");
		thirdLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		thirdLine.setFont(new Font(thirdLine.getFont().getName(), Font.PLAIN, thirdLine.getFont().getSize()));
		generatePdfPanel.add(thirdLine);

		generatePdfPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		pdfButton = new JButton("Tallenna Pdf");
		pdfButton.setActionCommand(RankingPanelButtonCommands.GENERATE_RANKING_PDF.toString());
		pdfButton.addActionListener(buttonClickListener);
		generatePdfPanel.add(pdfButton);

		return generatePdfPanel;
	}

	private JPanel getDeleteOldRankingsInstructionPanel() {
		JPanel deleteOldRankingsPanel = new JPanel();
		deleteOldRankingsPanel.setLayout(new BoxLayout(deleteOldRankingsPanel, BoxLayout.Y_AXIS));

		JLabel firstLine = new JLabel("Poista vanhoja ranking-");
		firstLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		deleteOldRankingsPanel.add(firstLine);
		firstLine.setFont(new Font(firstLine.getFont().getName(), Font.BOLD, firstLine.getFont().getSize()));

		JLabel secondLine = new JLabel("tietoja");
		secondLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		secondLine.setFont(new Font(secondLine.getFont().getName(), Font.BOLD, secondLine.getFont().getSize()));
		deleteOldRankingsPanel.add(secondLine);

		deleteOldRankingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		choosePreviousRankingsToDeleteButton = new JButton("Valitse");
		choosePreviousRankingsToDeleteButton
				.setActionCommand(RankingPanelButtonCommands.CHOOSE_RANKINGS_TO_DELETE.toString());
		choosePreviousRankingsToDeleteButton.addActionListener(buttonClickListener);
		buttonsPanel.add(choosePreviousRankingsToDeleteButton, BorderLayout.WEST);

		JPanel deleteCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		deleteRankingsButton = new JButton("Poista");
		deleteRankingsButton.setActionCommand(RankingPanelButtonCommands.DELETE_RANKINGS.toString());
		deleteRankingsButton.addActionListener(buttonClickListener);
		deleteRankingsButton.setEnabled(false);

		deleteCancelButtonsPanel.add(deleteRankingsButton, BorderLayout.EAST);

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
		pdfButton.setEnabled(false);

	}

	private void cancelDeleteCommandHandler() {
		choosePreviousRankingsToDeleteButton.setEnabled(true);
		deleteRankingsButton.setEnabled(false);
		cancelDeleteButton.setEnabled(false);
		pdfButton.setEnabled(true);
	}

	private void deleteRankingsCommandHandler() {
		deleteRankingsButton.setEnabled(false);
		cancelDeleteButton.setEnabled(false);

		DataService.deletePreviousRankings();

		choosePreviousRankingsToDeleteButton.setEnabled(true);
		pdfButton.setEnabled(true);
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
			RankingService.createPdfRankingFile(DataService.getRanking(),
					DataService.getPreviousRankingsTableSelectedRanking(), absoluteFilePath);
		} else {
			if (returnVal != JFileChooser.CANCEL_OPTION)
				generatePdfCommandHandler();
		}
		pdfButton.setEnabled(true);

	}

	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			RankingPanelActionEventService.emit(event);
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
}
