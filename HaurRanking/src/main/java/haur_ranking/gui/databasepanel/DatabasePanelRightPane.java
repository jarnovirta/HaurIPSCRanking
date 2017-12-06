package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.Stage;
import haur_ranking.event.GUIActionEvent;
import haur_ranking.event.GUIActionEventListener;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.GUIActionEventService;
import haur_ranking.gui.service.GUIDataService;

public class DatabasePanelRightPane extends JPanel implements GUIDataEventListener, GUIActionEventListener {
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
		GUIDataService.addDataEventListener(this);
		GUIActionEventService.addGUIActionEventListener(this);
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
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				GUIDataService.setDatabaseMatchInfoTableStagesToDelete(new ArrayList<Stage>());
				int[] selectedRowIndexes = table.getSelectedRows();
				for (int index : selectedRowIndexes) {
					GUIDataService.getDatabaseMatchInfoTableStagesToDelete()
							.add(GUIDataService.getDatabaseMatchInfoTableStages().get(index));
				}
			}
		});

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
		if (databaseMatchInfoTable.getRowCount() > 0) {
			databaseMatchInfoTable.setRowSelectionInterval(0, 0);
		}
		cardLayout.show(this, DatabaseDataTableStatus.HAS_DATA.toString());
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			if (event.getImportedMatchesTableData() != null && event.getImportedMatchesTableData().size() > 0) {
				updateDatabaseMatchInfoTable(event.getImportedMatchesTableData());
			}
		}
	}

	@Override
	public void process(GUIActionEvent event) {
		switch (event.getEventType()) {
		case CHOOSE_STAGES_TO_DELETE_BUTTON_CLICKED:
			databaseMatchInfoTable.setRowSelectionAllowed(true);
			break;
		case DELETE_STAGES_BUTTON_CLICKED:
			break;
		case CANCEL_DELETE_STAGES_BUTTON_CLICKED:
			databaseMatchInfoTable.setRowSelectionAllowed(false);
			break;
		}
	}
}
