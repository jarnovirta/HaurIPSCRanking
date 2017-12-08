package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
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
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.DataService;
import haur_ranking.utils.DataFormatUtils;

public class RankingTable extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Map<IPSCDivision, JTable> divisionRankingTables;
	private List<JPanel> rankingTablePanes;

	private enum RankingTableStatus {
		TABLE_EMPTY, TABLE_NOT_EMPTY
	};

	public RankingTable() {
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
			rankingTableCardsPanel.add(rankingTableScrollPane, RankingTableStatus.TABLE_NOT_EMPTY.toString());
			rankingTableCardsPanel.add(getNoResultsDataPanel(), RankingTableStatus.TABLE_EMPTY.toString());

			divisionRankingTables.put(division, rankingTable);
			rankingTablePanes.add(rankingTableCardsPanel);

			tabbedRankingTablesPane.add(division.toString(), rankingTableCardsPanel);
		}
		rankingTabViewPanel.add(tabbedRankingTablesPane);

		this.add(rankingTabViewPanel);
		DataService.addDataEventListener(this);
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
		rankingTable.getTableHeader().setReorderingAllowed(false);
		rankingTable.getTableHeader().setResizingAllowed(true);
		rankingTable.setCellSelectionEnabled(false);
		rankingTable.setColumnSelectionAllowed(false);
		rankingTable.setRowSelectionAllowed(false);

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
		// Clear tables
		for (IPSCDivision division : IPSCDivision.values()) {
			JTable divisionTable = divisionRankingTables.get(division);
			DefaultTableModel divisionTableModel = (DefaultTableModel) divisionTable.getModel();
			divisionTableModel.setRowCount(0);
			divisionTableModel.fireTableDataChanged();
		}
		for (DivisionRanking divisionRanking : ranking.getDivisionRankings()) {
			JTable divisionTable = divisionRankingTables.get(divisionRanking.getDivision());
			addRowsToDivisionRankingTable(divisionTable, divisionRanking.getDivisionRankingRows());
		}

		for (IPSCDivision division : divisionRankingTables.keySet()) {
			int rankingTablePanesIndex = division.ordinal();
			JPanel rankingTablePane = rankingTablePanes.get(rankingTablePanesIndex);
			CardLayout cardLayout = (CardLayout) rankingTablePane.getLayout();
			JTable divisionTable = divisionRankingTables.get(division);
			if (divisionTable.getRowCount() == 0) {
				cardLayout.show(rankingTablePane, RankingTableStatus.TABLE_EMPTY.toString());
			} else {
				cardLayout.show(rankingTablePane, RankingTableStatus.TABLE_NOT_EMPTY.toString());
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
				rowData[0] = String.valueOf(i) + ". ";
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
			i++;
		}
		if (rows.size() > 0) {
			for (int y = 0; y < (17 - i); y++)
				divisionTableModel.addRow(new Object[] { "", "", "", "", "" });
		}
		divisionTableModel.fireTableDataChanged();
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE && DataService.getRanking() != null) {
			updateRankingTablesData(DataService.getRanking());
		}
	}

}
