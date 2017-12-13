package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.service.DataService;
import haur_ranking.gui.utils.JTableUtils;

public class CompetitorDataPanel extends JPanel implements ActionListener, GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JTable databaseCompetitorInfoTable;
	private CardLayout cardLayout;

	private enum DatabaseDataTableStatus {
		NO_DATA, HAS_DATA
	};

	public CompetitorDataPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		databaseCompetitorInfoTable = getDatabaseMatchInfoTable();
		JScrollPane scrollPane = new JScrollPane(databaseCompetitorInfoTable);
		add(scrollPane, DatabaseDataTableStatus.HAS_DATA.toString());
		add(getNoDataPanel(), DatabaseDataTableStatus.NO_DATA.toString());
		cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
		DataService.addDataEventListener(this);

	}

	private JPanel getNoDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei kilpailijatietoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JTable getDatabaseMatchInfoTable() {
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

		competitorNameColumn.setHeaderValue("Kilpailija");
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
		for (Competitor competitor : databaseCompetitorTableData) {
			String[] row = new String[3];
			row[0] = rowCounter + 1 + ". ";
			String middleInitial = "";
			if (competitor.getWinMSSComment() != null)
				middleInitial = competitor.getWinMSSComment();
			row[1] = competitor.getFirstName() + " " + middleInitial + competitor.getLastName();
			row[2] = String.valueOf(competitor.getResultCount());
			tableModel.addRow(row);
			rowCounter++;
		}

		tableModel.fireTableDataChanged();
		if (databaseCompetitorInfoTable.getRowCount() > 0) {
			databaseCompetitorInfoTable.setRowSelectionInterval(0, 0);

		}
		cardLayout.show(this, DatabaseDataTableStatus.HAS_DATA.toString());
	}

	private void setSelectedCompetitorsToDelete() {
		DataService.clearCompetitorsToDelete();
		int[] selectedRowIndexes = databaseCompetitorInfoTable.getSelectedRows();
		for (int index : selectedRowIndexes) {
			if (DataService.getDatabaseCompetitorInfoTableData() != null
					&& DataService.getDatabaseCompetitorInfoTableData().size() > 0) {
				DataService.addCompetitorToDelete(DataService.getDatabaseCompetitorInfoTableData().get(index));
			}
		}
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			if (DataService.getDatabaseCompetitorInfoTableData() != null) {
				updateDatabaseCompetitorInfoTable(DataService.getDatabaseCompetitorInfoTableData());
				databaseCompetitorInfoTable.setRowSelectionAllowed(false);
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

}
