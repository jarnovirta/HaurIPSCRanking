package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import haur_ranking.gui.filter.FileFilterUtils;
import haur_ranking.gui.filter.PdfFileFilter;
import haur_ranking.gui.service.DataService;
import haur_ranking.service.RankingService;
import haur_ranking.utils.DateFormatUtils;

public class PreviousRankingsControlsPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JButton choosePreviousRankingsToDeleteButton;
	private JButton deleteRankingsButton;
	private JButton cancelDeleteButton;

	private ButtonClickListener buttonClickListener = new ButtonClickListener();

	private String lastRankingPdfFileLocation;

	public PreviousRankingsControlsPanel() {
		setLayout(new BorderLayout());

		// JPanel instructions = new JPanel();
		// instructions.setLayout(new BoxLayout(instructions,
		// BoxLayout.Y_AXIS));
		// instructions.setBorder(BorderFactory.createEmptyBorder(40, 15, 0,
		// 0));
		// JLabel firstLine = new JLabel("Valitse vertailu-ranking:");
		// instructions.add(firstLine);
		// firstLine.setFont(new Font(firstLine.getFont().getName(), Font.BOLD,
		// firstLine.getFont().getSize()));
		// instructions.add(Box.createRigidArea(new Dimension(0, 20)));
		//
		// JLabel secondLine = new JLabel("Tulosparannukset lihavoituina.");
		// secondLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		// secondLine.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		// secondLine.setFont(new Font(secondLine.getFont().getName(),
		// Font.PLAIN, secondLine.getFont().getSize()));
		// instructions.add(secondLine);

		// add(instructions, BorderLayout.NORTH);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

		JPanel buttonsTopLine = new JPanel(new BorderLayout());

		choosePreviousRankingsToDeleteButton = new JButton("Valitse poistettavat");
		choosePreviousRankingsToDeleteButton.setActionCommand("chooseRankingsToDelete");
		choosePreviousRankingsToDeleteButton.addActionListener(buttonClickListener);

		buttonsTopLine.add(choosePreviousRankingsToDeleteButton, BorderLayout.EAST);
		buttonsPanel.add(buttonsTopLine);
		JPanel buttonsBottomLine = new JPanel(new BorderLayout());

		JButton pdfButton = new JButton("Tallenna Pdf");
		pdfButton.setActionCommand("generatePdf");
		pdfButton.addActionListener(new ButtonClickListener());
		buttonsBottomLine.add(pdfButton, BorderLayout.WEST);

		JPanel deleteCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		deleteRankingsButton = new JButton("Poista");
		deleteRankingsButton.setActionCommand("deleteRankings");
		deleteRankingsButton.addActionListener(buttonClickListener);
		deleteRankingsButton.setEnabled(false);

		deleteCancelButtonsPanel.add(deleteRankingsButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand("cancenDelete");
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);

		deleteCancelButtonsPanel.add(cancelDeleteButton);

		buttonsBottomLine.add(deleteCancelButtonsPanel);

		buttonsPanel.add(buttonsBottomLine);

		add(buttonsPanel, BorderLayout.SOUTH);
	}

	private void generatePdfCommandHandler() {
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

	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "chooseRankingsToDelete":
				break;
			case "cancenDelete":
				break;

			case "deleteRankings":
				break;

			case "generatePdf":
				generatePdfCommandHandler();
				break;
			}
		}
	}
}
