package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.GUIDataService;

public class DatabasePanelRightPane extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTable databaseMatchInfoTable;
	private CardLayout cardLayout;

	private enum DatabaseDataTableStatus {
		NO_DATA, HAS_DATA
	};

	public DatabasePanelRightPane() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		databaseMatchInfoTable = getDatabaseMatchInfoTable();
		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		add(scrollPane, DatabaseDataTableStatus.HAS_DATA.toString());
		add(getNoDataPanel(), DatabaseDataTableStatus.NO_DATA.toString());
		cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
	}

	private JPanel getNoDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei tulostietoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JTable getDatabaseMatchInfoTable() {
		JTable table = new JTable(0, 4) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowHeight(35);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);

		TableColumn matchCountColumn = table.getColumnModel().getColumn(0);
		TableColumn matchNameColumn = table.getColumnModel().getColumn(1);
		TableColumn matchDateColumn = table.getColumnModel().getColumn(2);
		TableColumn classifiersColumn = table.getColumnModel().getColumn(3);

		matchCountColumn.setHeaderValue("");
		matchCountColumn.setPreferredWidth(50);

		matchNameColumn.setHeaderValue("Kilpailu");
		matchNameColumn.setPreferredWidth(400);

		matchDateColumn.setHeaderValue("Pvm");
		matchDateColumn.setPreferredWidth(100);

		classifiersColumn.setHeaderValue("Luokitteluohj.");
		classifiersColumn.setPreferredWidth(100);

		GUIDataService.addRankingDataUpdatedEventListener(this);
		return table;
	}

	private void updateDatabaseMatchInfoTable(List<String[]> databaseMatchTableData) {
		if (databaseMatchTableData == null) {
			return;
		}

		DefaultTableModel tableModel = (DefaultTableModel) databaseMatchInfoTable.getModel();
		tableModel.setRowCount(0);
		int rowCounter = 0;
		for (String[] row : databaseMatchTableData) {
			tableModel.addRow(row);
			rowCounter++;
		}
		if (rowCounter > 0) {
			for (int i = 0; i < (17 - rowCounter); i++)
				tableModel.addRow(new String[] { "", "", "", "" });
		}
		tableModel.fireTableDataChanged();
		cardLayout.show(this, DatabaseDataTableStatus.HAS_DATA.toString());
	}

	@Override
	public void processData(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			if (event.getImportedMatchesTableData() != null && event.getImportedMatchesTableData().size() > 0) {
				updateDatabaseMatchInfoTable(event.getImportedMatchesTableData());
			}
		}
	}
}
