package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.Ranking;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.gui.service.GUIDataService;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.utils.DateFormatUtils;

public class RankingPanelRightPane extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private enum PreviousRankingsTableStatus {
		NO_DATA, HAS_DATA
	};

	private CardLayout cardLayout;
	private JTable databaseMatchInfoTable;
	private JPanel previousRankingsTable;
	private JTable rankingTable;

	public RankingPanelRightPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel paneHeader = new JPanel();
		paneHeader.add(Box.createRigidArea(new Dimension(0, 25)));
		add(paneHeader);
		previousRankingsTable = new JPanel();
		cardLayout = new CardLayout();
		previousRankingsTable.setLayout(cardLayout);
		databaseMatchInfoTable = getPreviousRankingsTable();
		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		previousRankingsTable.add(scrollPane, PreviousRankingsTableStatus.HAS_DATA.toString());
		previousRankingsTable.add(getNoTableDataPanel(), PreviousRankingsTableStatus.NO_DATA.toString());
		cardLayout.show(previousRankingsTable, PreviousRankingsTableStatus.NO_DATA.toString());
		add(previousRankingsTable);
		JPanel previousRankingsPaneControlPanel = new JPanel();
		previousRankingsPaneControlPanel.add(Box.createRigidArea(new Dimension(0, 160)));
		add(previousRankingsPaneControlPanel);
		GUIDataService.addRankingDataUpdatedEventListener(this);

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

		rankingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				int selectedRowIndex = rankingTable.getSelectedRow();
				if (selectedRowIndex >= 0) {
					GUIDataService.setPreviousRankingsTableSelectedRanking(
							GUIDataService.getPreviousRankingsTableData().get(selectedRowIndex));
				}
			}
		});

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
		rankingDateColumn.setPreferredWidth(100);

		lastMatchColumn.setCellRenderer(leftRenderer);
		lastMatchColumn.setHeaderValue("Viimeisin kisa");
		lastMatchColumn.setPreferredWidth(300);

		lastMatchDateColumn.setCellRenderer(leftRenderer);
		lastMatchDateColumn.setHeaderValue("Kisan pvm");
		lastMatchDateColumn.setPreferredWidth(100);

		return rankingTable;
	}

	public void updateDatabaseMatchInfoTable(List<Ranking> previousRankings) {
		DefaultTableModel tableModel = (DefaultTableModel) databaseMatchInfoTable.getModel();
		tableModel.setRowCount(0);
		if (previousRankings == null) {
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
		cardLayout.show(previousRankingsTable, PreviousRankingsTableStatus.HAS_DATA.toString());
	}

	@Override
	public void processData(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			updateDatabaseMatchInfoTable(GUIDataService.getPreviousRankingsTableData());
		}
	}
}
