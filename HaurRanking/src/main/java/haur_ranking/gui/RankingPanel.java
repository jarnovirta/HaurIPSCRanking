package haur_ranking.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Ranking;
import haur_ranking.gui.Event.RankingDataUpdatedEventListener;
import haur_ranking.gui.filters.FileFilterUtils;
import haur_ranking.gui.filters.PdfFileFilter;
import haur_ranking.pdf.PdfGenerator;
import haur_ranking.utils.DataFormatUtils;

public class RankingPanel extends JPanel implements RankingDataUpdatedEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Map<IPSCDivision, JTable> divisionRankingTables;
	private List<JPanel> rankingTablePanes;
	private String lastRankingPdfFileLocation;

	private enum rankingTableStatus {
		TABLE_EMPTY, TABLE_NOT_EMPTY
	};

	public RankingPanel() {
		this.setLayout(new BorderLayout());
		divisionRankingTables = new HashMap<IPSCDivision, JTable>();
		rankingTablePanes = new ArrayList<JPanel>();

		JPanel rankingTabViewPanel = new JPanel();
		rankingTabViewPanel.setLayout(new BoxLayout(rankingTabViewPanel, BoxLayout.PAGE_AXIS));

		JTabbedPane tabbedRankingTablesPane = new JTabbedPane();

		for (IPSCDivision division : IPSCDivision.values()) {

			JTable rankingTable = getDivisionRankingTable();
			JScrollPane rankingTableScrollPane = new JScrollPane(rankingTable);

			JPanel rankingTableCardsPanel = new JPanel(new CardLayout());
			rankingTableCardsPanel.add(rankingTableScrollPane, rankingTableStatus.TABLE_NOT_EMPTY.toString());
			rankingTableCardsPanel.add(getNoResultsDataPanel(), rankingTableStatus.TABLE_EMPTY.toString());

			divisionRankingTables.put(division, rankingTable);
			rankingTablePanes.add(rankingTableCardsPanel);

			tabbedRankingTablesPane.add(division.toString(), rankingTableCardsPanel);
		}
		rankingTabViewPanel.add(tabbedRankingTablesPane);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton pdfButton = new JButton("Tallenna Pdf");
		pdfButton.setActionCommand("generatePdf");
		pdfButton.addActionListener(new ButtonClickListener());

		buttonsPanel.add(pdfButton);
		rankingTabViewPanel.add(buttonsPanel);
		this.add(rankingTabViewPanel);
		RankingDataService.addRankingDataUpdatedEventListener(this);
	}

	private JTable getDivisionRankingTable() {
		JTable rankingTable = new JTable(0, 5) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};

		};
		rankingTable.setRowHeight(35);

		TableColumn positionColumn = rankingTable.getColumnModel().getColumn(0);
		TableColumn competitorNameColumn = rankingTable.getColumnModel().getColumn(1);
		TableColumn resultPercentageColumn = rankingTable.getColumnModel().getColumn(2);
		TableColumn hitFactorAverageColumn = rankingTable.getColumnModel().getColumn(3);
		TableColumn resultCountColumn = rankingTable.getColumnModel().getColumn(4);

		DefaultTableCellRenderer leftRenderer;
		DefaultTableCellRenderer rightRenderer;
		DefaultTableCellRenderer centerRenderer;

		leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		positionColumn.setCellRenderer(rightRenderer);
		positionColumn.setHeaderValue("Sija");
		positionColumn.setPreferredWidth(50);

		competitorNameColumn.setCellRenderer(leftRenderer);
		competitorNameColumn.setHeaderValue("Nimi");
		competitorNameColumn.setPreferredWidth(300);

		resultPercentageColumn.setCellRenderer(centerRenderer);
		resultPercentageColumn.setHeaderValue("%");
		resultPercentageColumn.setPreferredWidth(50);

		hitFactorAverageColumn.setCellRenderer(centerRenderer);
		hitFactorAverageColumn.setHeaderValue("HF-keskiarvo");
		hitFactorAverageColumn.setPreferredWidth(50);

		resultCountColumn.setCellRenderer(centerRenderer);
		resultCountColumn.setHeaderValue("Tuloksia");
		resultCountColumn.setPreferredWidth(50);

		rankingTable.getTableHeader().setReorderingAllowed(false);
		rankingTable.getTableHeader().setResizingAllowed(false);

		return rankingTable;
	}

	private JPanel getNoResultsDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei tulostietoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	public void updateRankingTablesData(Ranking ranking) {
		if (ranking == null)
			ranking = new Ranking();
		for (DivisionRanking divisionRanking : ranking.getDivisionRankings()) {
			JTable divisionTable = divisionRankingTables.get(divisionRanking.getDivision());
			DefaultTableModel divisionTableModel = (DefaultTableModel) divisionTable.getModel();
			divisionTableModel.setRowCount(0);
			addRowsToDivisionRankingTable(divisionTable, divisionRanking.getDivisionRankingRows());
		}

		for (IPSCDivision division : divisionRankingTables.keySet()) {
			int rankingTablePanesIndex = division.ordinal();
			JPanel rankingTablePane = rankingTablePanes.get(rankingTablePanesIndex);
			CardLayout cardLayout = (CardLayout) rankingTablePane.getLayout();
			JTable divisionTable = divisionRankingTables.get(division);
			if (divisionTable.getRowCount() == 0) {
				cardLayout.show(rankingTablePane, rankingTableStatus.TABLE_EMPTY.toString());
			} else {
				cardLayout.show(rankingTablePane, rankingTableStatus.TABLE_NOT_EMPTY.toString());
			}
			rankingTablePane.revalidate();
		}

	}

	private void addRowsToDivisionRankingTable(JTable rankingTable, List<DivisionRankingRow> rows) {
		DefaultTableModel divisionTableModel = (DefaultTableModel) rankingTable.getModel();
		divisionTableModel.setRowCount(0);
		int i = 1;
		for (DivisionRankingRow line : rows) {
			Object[] rowData = new Object[5];
			rowData[1] = line.getCompetitor().getFirstName() + " " + line.getCompetitor().getLastName();
			rowData[4] = String.valueOf(line.getResultsCount());
			if (line.isRankedCompetitor()) {
				rowData[0] = String.valueOf(i++) + ". ";
				rowData[2] = String
						.valueOf(DataFormatUtils
								.formatTwoDecimalNumberToString(DataFormatUtils.round(line.getResultPercentage(), 2)))
						+ " %";
				rowData[3] = String.valueOf(DataFormatUtils
						.formatTwoDecimalNumberToString(DataFormatUtils.round(line.getHitFactorAverage(), 2)));

			} else {
				rowData[0] = "--  ";
				rowData[2] = "--";
				rowData[3] = "--";
			}
			divisionTableModel.addRow(rowData);
		}
		if (rows.size() > 0) {
			for (int y = 0; y < (12 - i); y++)
				divisionTableModel.addRow(new Object[] { "", "", "", "", "" });
		}
		divisionTableModel.fireTableDataChanged();
	}

	private void generatePdfCommandHandler() {
		JFileChooser fileChooser;
		if (lastRankingPdfFileLocation != null)
			fileChooser = new JFileChooser(lastRankingPdfFileLocation);
		else
			fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new PdfFileFilter());
		fileChooser.setPreferredSize(new Dimension(800, 600));
		fileChooser.setSelectedFile(
				new File("HaurRanking_" + RankingDataService.getRanking().getDateString().replace('.', '_') + ".pdf"));
		fileChooser.setApproveButtonText("Save");

		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String absoluteFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			String fileExtension = FileFilterUtils.getExtension(new File(absoluteFilePath));
			if (fileExtension == null)
				absoluteFilePath += ".pdf";

			lastRankingPdfFileLocation = Paths.get(absoluteFilePath).getParent().toString();
			PdfGenerator.createPdfRankingFile(RankingDataService.getRanking(), absoluteFilePath);
		} else {
			if (returnVal != JFileChooser.CANCEL_OPTION)
				generatePdfCommandHandler();
		}

	}

	@Override
	public void rankingDataUpdate(Ranking ranking) {
		updateRankingTablesData(ranking);
	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("generatePdf")) {
				generatePdfCommandHandler();
			}
		}
	}

}
