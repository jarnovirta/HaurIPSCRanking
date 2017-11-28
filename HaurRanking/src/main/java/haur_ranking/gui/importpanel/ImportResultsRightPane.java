package haur_ranking.gui.importpanel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.Event.GUIDataEvent;
import haur_ranking.Event.GUIDataEvent.GUIDataEventType;
import haur_ranking.Event.GUIDataEventListener;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.gui.GUIDataService;

public class ImportResultsRightPane extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTable databaseMatchInfoTable;

	public ImportResultsRightPane() {
		setLayout(new BorderLayout());
		databaseMatchInfoTable = getDatabaseMatchInfoTable();
		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		add(scrollPane);
		GUIDataService.addRankingDataUpdatedEventListener(this);
	}

	private JTable getDatabaseMatchInfoTable() {
		JTable table = new JTable(0, 6) {
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
		TableColumn stageColumn = table.getColumnModel().getColumn(3);
		TableColumn classifierColumn = table.getColumnModel().getColumn(4);
		TableColumn importStageColumn = table.getColumnModel().getColumn(5);

		matchCountColumn.setHeaderValue("");
		matchCountColumn.setPreferredWidth(50);

		matchNameColumn.setHeaderValue("Kilpailu");
		matchNameColumn.setPreferredWidth(400);

		matchDateColumn.setHeaderValue("Pvm");
		matchDateColumn.setPreferredWidth(200);

		stageColumn.setHeaderValue("Asema");
		stageColumn.setPreferredWidth(300);

		classifierColumn.setHeaderValue("Luokitteluohjelma");
		classifierColumn.setPreferredWidth(300);

		importStageColumn.setHeaderValue("Tuo tulokset");
		classifierColumn.setPreferredWidth(300);

		return table;
	}

	public void updateDatabaseMatchInfoTable(List<Match> winMSSMatches) {
		DefaultTableModel tableModel = (DefaultTableModel) databaseMatchInfoTable.getModel();
		tableModel.setRowCount(0);
		if (winMSSMatches == null) {
			return;
		}

		int matchCounter = 1;
		for (Match match : winMSSMatches) {
			Object[] rowData = new Object[6];
			rowData[0] = (matchCounter++) + ".";
			rowData[1] = match.getName();
			rowData[2] = match.getDateString();
			if (match.getStages() == null || match.getStages().size() == 0)
				continue;
			Stage stage = match.getStages().get(0);
			rowData[3] = stage.getName();
			if (match.getStages().get(0).getClassifierStage() != null)
				rowData[4] = stage.getClassifierStage().toString();
			rowData[5] = stage.isNewStage();
			tableModel.addRow(rowData);
			if (match.getStages().size() == 1)
				continue;
			for (int i = 1; i < match.getStages().size(); i++) {
				stage = match.getStages().get(i);
				rowData = new Object[6];
				rowData[0] = "";
				rowData[1] = "";
				rowData[2] = "";
				rowData[3] = stage.getName();
				if (stage.getClassifierStage() != null)
					rowData[4] = stage.getClassifierStage();
				rowData[5] = stage.isNewStage();
				tableModel.addRow(rowData);
			}
		}
		tableModel.fireTableDataChanged();
	}

	@Override
	public void processData(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.NEW_WINMSS_DB_DATA) {
			updateDatabaseMatchInfoTable(event.getWinMSSNewMatches());
		}
		if (event.getEventType() == GUIDataEventType.NEW_HAUR_RANKING_DB_DATA) {
			updateDatabaseMatchInfoTable(null);
		}
	}
}