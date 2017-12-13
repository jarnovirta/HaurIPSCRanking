package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import haur_ranking.gui.service.DataService;
import haur_ranking.utils.DateFormatUtils;

public class PreviousRankingsTable extends JPanel implements ActionListener, GUIDataEventListener {

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
	private JScrollPane scrollPane;

	int test = 1;

	public PreviousRankingsTable() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		previousRankingsTable = getPreviousRankingsTable();
		setTableToSingeRowSelection();
		scrollPane = new JScrollPane(previousRankingsTable);
		add(scrollPane, PreviousRankingsTableStatus.HAS_DATA.toString());
		add(getNoTableDataPanel(), PreviousRankingsTableStatus.NO_DATA.toString());
		cardLayout.show(this, PreviousRankingsTableStatus.NO_DATA.toString());
		DataService.addDataEventListener(this);

	}

	private JPanel getNoTableDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei vanhoja ranking-listoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JTable getPreviousRankingsTable() {
		rankingTable = new JTable(0, 3) {
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

		TableColumn rankingDateColumn = rankingTable.getColumnModel().getColumn(0);
		TableColumn lastMatchColumn = rankingTable.getColumnModel().getColumn(1);
		TableColumn lastMatchDateColumn = rankingTable.getColumnModel().getColumn(2);

		DefaultTableCellRenderer leftRenderer;
		DefaultTableCellRenderer rightRenderer;
		DefaultTableCellRenderer centerRenderer;

		leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		rankingDateColumn.setCellRenderer(centerRenderer);
		rankingDateColumn.setHeaderValue("Rankingin pvm");
		rankingDateColumn.setPreferredWidth(200);

		lastMatchColumn.setCellRenderer(leftRenderer);
		lastMatchColumn.setHeaderValue("Viimeinen huomioitu kisa");
		lastMatchColumn.setPreferredWidth(800);

		lastMatchDateColumn.setCellRenderer(centerRenderer);
		lastMatchDateColumn.setHeaderValue("Kisan pvm");
		lastMatchDateColumn.setPreferredWidth(200);

		return rankingTable;
	}

	public void updateDatabaseMatchInfoTable(List<Ranking> previousRankings) {
		DefaultTableModel tableModel = (DefaultTableModel) previousRankingsTable.getModel();
		tableModel.setRowCount(0);
		if (previousRankings == null) {
			cardLayout.show(this, PreviousRankingsTableStatus.NO_DATA.toString());
			return;
		}
		for (Ranking previousRanking : previousRankings) {
			Object[] rowData = new Object[3];
			rowData[0] = DateFormatUtils.calendarToDateString(previousRanking.getDate());
			rowData[1] = previousRanking.getLatestIncludedMatchName();
			rowData[2] = DateFormatUtils.calendarToDateString(previousRanking.getLatestIncludedMatchDate());
			tableModel.addRow(rowData);
		}
		tableModel.fireTableDataChanged();
		if (rankingTable.getRowCount() > 0) {
			rankingTable.setRowSelectionInterval(0, 0);
		}
		cardLayout.show(this, PreviousRankingsTableStatus.HAS_DATA.toString());

	}

	@Override
	public void process(GUIDataEvent event) {

		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			updateDatabaseMatchInfoTable(DataService.getPreviousRankingsTableData());
			setTableToSingeRowSelection();

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
		DataService.getPreviousRankingsToDelete().clear();
		int[] selectedRowIndexes = previousRankingsTable.getSelectedRows();
		for (int index : selectedRowIndexes) {
			DataService.addPreviousRankingToDelete(DataService.getPreviousRankingsTableData().get(index));
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
}