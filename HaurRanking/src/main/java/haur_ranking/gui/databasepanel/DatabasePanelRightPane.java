package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

	public DatabasePanelRightPane() {
		setLayout(new BorderLayout());
		databaseMatchInfoTable = getDatabaseMatchInfoTable();
		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		add(scrollPane);
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
	}

	@Override
	public void processData(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.NEW_HAUR_RANKING_DB_DATA) {
			if (event.getImportedMatchesTableData() != null && event.getImportedMatchesTableData().size() > 0) {
				updateDatabaseMatchInfoTable(event.getImportedMatchesTableData());
			}
		}
	}
}
