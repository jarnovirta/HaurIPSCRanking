package haur_ranking.gui.databasepanel;

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
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.Competitor;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.event.PaginationEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.paginationpanel.PaginationPanel;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.DatabasePanelDataService;
import haur_ranking.gui.utils.JTableUtils;

public class CompetitorDataPanel extends JPanel
		implements ActionListener, GUIDataEventListener, PaginationEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JTable databaseCompetitorInfoTable;
	private PaginationPanel paginationPanel;
	private CardLayout cardLayout;
	private JScrollPane competitorDataTableScrollPane;

	private enum DatabaseDataTableStatus {
		NO_DATA, HAS_DATA
	};

	public CompetitorDataPanel() {

		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.RIGHT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		databaseCompetitorInfoTable = getCompetitorInfoTable();
		JPanel databaseMatchInfoPanel = getCompetitorInfoPanel(databaseCompetitorInfoTable);
		add(databaseMatchInfoPanel, DatabaseDataTableStatus.HAS_DATA.toString());
		add(getNoDataPanel(), DatabaseDataTableStatus.NO_DATA.toString());
		cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
		DataEventService.addDataEventListener(this);

	}

	private JPanel getNoDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei kilpailijatietoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JPanel getCompetitorInfoPanel(JTable competitorInfoTable) {
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		competitorDataTableScrollPane = new JScrollPane(competitorInfoTable);
		competitorDataTableScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		tablePanel.add(competitorDataTableScrollPane);
		paginationPanel = new PaginationPanel(this);
		tablePanel.add(paginationPanel);
		return tablePanel;
	}

	private JTable getCompetitorInfoTable() {
		JTable table = new JTable(0, 3) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				setSelectedCompetitorsToDelete();
			}
		});

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowHeight(35);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);

		TableColumn competitorCountColumn = table.getColumnModel().getColumn(0);
		TableColumn competitorNameColumn = table.getColumnModel().getColumn(1);
		TableColumn resultsCount = table.getColumnModel().getColumn(2);

		DefaultTableCellRenderer leftRenderer = JTableUtils.getLeftRenderer();
		DefaultTableCellRenderer rightRenderer = JTableUtils.getRightRenderer();
		DefaultTableCellRenderer centerRenderer = JTableUtils.getCenterRenderer();

		competitorCountColumn.setHeaderValue("");
		competitorCountColumn.setCellRenderer(rightRenderer);
		competitorCountColumn.setPreferredWidth(150);

		competitorNameColumn.setHeaderValue("Nimi");
		competitorNameColumn.setCellRenderer(leftRenderer);
		competitorNameColumn.setPreferredWidth(800);

		resultsCount.setHeaderValue("Tuloksia");
		resultsCount.setCellRenderer(centerRenderer);
		resultsCount.setPreferredWidth(200);

		return table;
	}

	private void updateDatabaseCompetitorInfoTable(List<Competitor> databaseCompetitorTableData) {
		if (databaseCompetitorTableData == null || databaseCompetitorTableData.size() == 0) {
			cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
			return;
		}

		DefaultTableModel tableModel = (DefaultTableModel) databaseCompetitorInfoTable.getModel();
		tableModel.setRowCount(0);

		int rowCounter = 0;
		int firstRowOrderNumber = (DatabasePanelDataService.getCurrentCompetitorListPage() - 1)
				* DatabasePanelDataService.getCompetitorlistpagesize() + 1;
		for (Competitor competitor : databaseCompetitorTableData) {
			String[] row = new String[3];
			row[0] = firstRowOrderNumber + rowCounter + ". ";
			row[1] = competitor.getFirstName() + " " + competitor.getLastName();
			row[2] = String.valueOf(competitor.getResultCount());
			tableModel.addRow(row);
			rowCounter++;
		}

		tableModel.fireTableDataChanged();
		if (databaseCompetitorInfoTable.getRowCount() > 0) {
			databaseCompetitorInfoTable.setRowSelectionInterval(0, 0);

		}
		cardLayout.show(this, DatabaseDataTableStatus.HAS_DATA.toString());
		competitorDataTableScrollPane.getVerticalScrollBar().setValue(0);
	}

	private void setSelectedCompetitorsToDelete() {
		DatabasePanelDataService.clearCompetitorsToDelete();
		int[] selectedRowIndexes = databaseCompetitorInfoTable.getSelectedRows();
		for (int index : selectedRowIndexes) {
			if (DatabasePanelDataService.getDatabaseCompetitorInfoTableData() != null
					&& DatabasePanelDataService.getDatabaseCompetitorInfoTableData().size() > 0) {
				DatabasePanelDataService.addCompetitorToDelete(
						DatabasePanelDataService.getDatabaseCompetitorInfoTableData().get(index));
			}
		}
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.DATABASE_COMPETITOR_TABLE_UPDATE) {
			if (DatabasePanelDataService.getDatabaseCompetitorInfoTableData() != null) {
				updateDatabaseCompetitorInfoTable(DatabasePanelDataService.getDatabaseCompetitorInfoTableData());
				databaseCompetitorInfoTable.setRowSelectionAllowed(false);
				paginationPanel.setCurrentPage(DatabasePanelDataService.getCurrentCompetitorListPage());
				paginationPanel.setTotalPages(DatabasePanelDataService.getTotalCompetitorListPages());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand()
				.equals(CompetitorDataPanelButtonCommands.CHOOSE_COMPETITORS_TO_DELETE.toString())) {
			databaseCompetitorInfoTable.setRowSelectionAllowed(true);
			if (databaseCompetitorInfoTable.getRowCount() > 0) {
				databaseCompetitorInfoTable.setRowSelectionInterval(0, 0);
			}
		}

		if (event.getActionCommand().equals(CompetitorDataPanelButtonCommands.CANCEL_DELETE_COMPETITORS.toString())) {
			databaseCompetitorInfoTable.setRowSelectionAllowed(false);
		}
	}

	@Override
	public void changePage(int newPage) {
		DatabasePanelDataService.loadCompetitorTableData(newPage);
		paginationPanel.setCurrentPage(newPage);
	}

}
