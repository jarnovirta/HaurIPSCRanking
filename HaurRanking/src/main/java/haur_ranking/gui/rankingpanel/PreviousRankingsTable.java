package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.Ranking;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.event.PaginationEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.paginationpanel.PaginationPanel;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.RankingPanelDataService;
import haur_ranking.utils.DateFormatUtils;

public class PreviousRankingsTable extends JPanel
		implements ActionListener, GUIDataEventListener, PaginationEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private enum PreviousRankingsTableStatus {
		NO_DATA, HAS_DATA
	};

	private CardLayout cardLayout;
	private JTable previousRankingsTable;
	private JTable rankingTable;
	private PaginationPanel paginationPanel;
	private JScrollPane previousRankingsTableScrollPane;

	public PreviousRankingsTable() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.RIGHT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		previousRankingsTable = getPreviousRankingsTable();
		setTableToSingeRowSelection();
		JPanel previousRankingsPanel = getPreviousRankingsPanel(previousRankingsTable);
		add(previousRankingsPanel, PreviousRankingsTableStatus.HAS_DATA.toString());
		add(getNoTableDataPanel(), PreviousRankingsTableStatus.NO_DATA.toString());
		cardLayout.show(this, PreviousRankingsTableStatus.NO_DATA.toString());
		DataEventService.addDataEventListener(this);

	}

	private JPanel getPreviousRankingsPanel(JTable previousRankingsTable) {
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		previousRankingsTableScrollPane = new JScrollPane(previousRankingsTable);
		previousRankingsTableScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		tablePanel.add(previousRankingsTableScrollPane);
		paginationPanel = new PaginationPanel(this);
		tablePanel.add(paginationPanel);
		return tablePanel;
	}

	private JPanel getNoTableDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei vanhoja ranking-listoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JTable getPreviousRankingsTable() {
		rankingTable = new JTable(0, 4) {
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
		rankingTable.setRowSelectionAllowed(true);

		TableColumn orderNumberColumn = rankingTable.getColumnModel().getColumn(0);
		TableColumn rankingDateColumn = rankingTable.getColumnModel().getColumn(1);
		TableColumn lastMatchColumn = rankingTable.getColumnModel().getColumn(2);
		TableColumn lastMatchDateColumn = rankingTable.getColumnModel().getColumn(3);

		DefaultTableCellRenderer leftRenderer;
		DefaultTableCellRenderer rightRenderer;
		DefaultTableCellRenderer centerRenderer;

		leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		orderNumberColumn.setCellRenderer(rightRenderer);
		orderNumberColumn.setHeaderValue("");
		orderNumberColumn.setPreferredWidth(150);

		rankingDateColumn.setCellRenderer(centerRenderer);
		rankingDateColumn.setHeaderValue("Rankingin pvm");
		rankingDateColumn.setPreferredWidth(200);

		lastMatchColumn.setCellRenderer(leftRenderer);
		lastMatchColumn.setHeaderValue("Viimeinen huomioitu kisa");
		lastMatchColumn.setPreferredWidth(650);

		lastMatchDateColumn.setCellRenderer(centerRenderer);
		lastMatchDateColumn.setHeaderValue("Kisan pvm");
		lastMatchDateColumn.setPreferredWidth(200);

		return rankingTable;
	}

	public void updateDatabaseMatchInfoTable(List<Ranking> previousRankings) {
		DefaultTableModel tableModel = (DefaultTableModel) previousRankingsTable.getModel();
		tableModel.setRowCount(0);
		if (previousRankings == null || previousRankings.size() == 0) {
			cardLayout.show(this, PreviousRankingsTableStatus.NO_DATA.toString());
			return;
		}
		int rowCounter = 0;
		int firstRowOrderNumber = (RankingPanelDataService.getCurrentPreviousRankingsListPage() - 1)
				* RankingPanelDataService.getPreviousrankingslistpagesize() + 1;
		for (Ranking previousRanking : previousRankings) {
			Object[] rowData = new Object[4];
			rowData[0] = firstRowOrderNumber + rowCounter + ". ";
			rowData[1] = DateFormatUtils.calendarToDateString(previousRanking.getDate());
			rowData[2] = previousRanking.getLatestIncludedMatchName();
			String dateString = DateFormatUtils.calendarToDateString(previousRanking.getLatestIncludedMatchDate());
			if (dateString == null || dateString.equals(""))
				dateString = "--";
			rowData[3] = dateString;
			tableModel.addRow(rowData);
			rowCounter++;
		}
		tableModel.fireTableDataChanged();
		if (rankingTable.getRowCount() > 0) {
			rankingTable.setRowSelectionInterval(0, 0);
		}
		cardLayout.show(this, PreviousRankingsTableStatus.HAS_DATA.toString());
		previousRankingsTableScrollPane.getVerticalScrollBar().setValue(0);

	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.PREVIOUS_RANKINGS_TABLE_UPDATE) {
			updateDatabaseMatchInfoTable(RankingPanelDataService.getPreviousRankingsTableData());
			setTableToSingeRowSelection();
			paginationPanel.setCurrentPage(RankingPanelDataService.getCurrentPreviousRankingsListPage());
			paginationPanel.setTotalPages(RankingPanelDataService.getTotalPreviousRankingsListPages());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals(RankingPanelButtonCommands.CHOOSE_RANKINGS_TO_DELETE.toString())) {
			setTableToMultiRowSelection();
		}

		if (event.getActionCommand().equals(RankingPanelButtonCommands.CANCEL_DELETE_RANKINGS.toString())) {
			setTableToSingeRowSelection();
		}
	}

	private void setRankingsToDelete() {
		RankingPanelDataService.clearPreviousRankingsToDelete();
		int[] selectedRowIndexes = previousRankingsTable.getSelectedRows();
		if (selectedRowIndexes.length > 0 && RankingPanelDataService.getPreviousRankingsTableData() != null
				&& RankingPanelDataService.getPreviousRankingsTableData().size() > 0)
			RankingPanelDataService.setPreviousRankingsTableSelectedRanking(
					RankingPanelDataService.getPreviousRankingsTableData().get(selectedRowIndexes[0]));
		for (int index : selectedRowIndexes) {
			RankingPanelDataService
					.addPreviousRankingToDelete(RankingPanelDataService.getPreviousRankingsTableData().get(index));
		}
	}

	private void setTableToMultiRowSelection() {
		previousRankingsTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		previousRankingsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				setRankingsToDelete();
			}
		});
		if (previousRankingsTable.getRowCount() > 0) {
			previousRankingsTable.setRowSelectionInterval(0, 0);
		}
	}

	private void setTableToSingeRowSelection() {
		previousRankingsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		previousRankingsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				setRankingsToDelete();
			}
		});
		if (previousRankingsTable.getRowCount() > 0) {
			previousRankingsTable.setRowSelectionInterval(0, 0);
		}
	}

	@Override
	public void changePage(int newPage) {
		RankingPanelDataService.loadPreviousRankingsTableData(newPage);
		paginationPanel.setCurrentPage(newPage);
	}
}