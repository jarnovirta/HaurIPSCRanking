package haur_ranking.gui.importpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.ImportPanelDataService;
import haur_ranking.gui.utils.JTableUtils;
import haur_ranking.utils.DateFormatUtils;

public class WinMSSDatabasePane extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel cardLayoutPanel;
	private JTable databaseMatchInfoTable;
	private Map<String, Boolean> editableCellsMap = new HashMap<String, Boolean>();
	private Map<Integer, Stage> TableRowToStageMap = new HashMap<Integer, Stage>();
	private CardLayout cardLayout;

	private final String DO_NOT_SAVE_AS_CLASSIFIER = "Ei tallenneta";
	private final String ALREADY_IN_DATABASE = "Tallennettu";

	private enum ImportTableStatus {
		NO_WINMSS_DB_SELECTED, SHOW_WINMSS_DATA, IMPORT_RESULT
	};

	public WinMSSDatabasePane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		cardLayoutPanel = new JPanel();

		cardLayout = new CardLayout();
		cardLayoutPanel.setLayout(cardLayout);
		databaseMatchInfoTable = getDatabaseMatchInfoTable();
		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		cardLayoutPanel.add(scrollPane, ImportTableStatus.SHOW_WINMSS_DATA.toString());
		cardLayoutPanel.add(getNoTableDataPanel(), ImportTableStatus.NO_WINMSS_DB_SELECTED.toString());
		cardLayoutPanel.add(new ImportResultPanel(), ImportTableStatus.IMPORT_RESULT.toString());

		add(cardLayoutPanel);
		add(Box.createRigidArea(new Dimension(0, 30)));
		cardLayout.show(cardLayoutPanel, ImportTableStatus.NO_WINMSS_DB_SELECTED.toString());
		DataEventService.addDataEventListener(this);
	}

	private JPanel getNoTableDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Avaa WinMSS-tietokanta", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JTable getDatabaseMatchInfoTable() {
		JTable table = new JTable(0, 5) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return Boolean.FALSE != editableCellsMap.get(row + ":" + column);
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
		TableColumn stageColumn = table.getColumnModel().getColumn(3);
		TableColumn importStageColumn = table.getColumnModel().getColumn(4);

		DefaultTableCellRenderer leftRenderer = JTableUtils.getLeftRenderer();
		DefaultTableCellRenderer rightRenderer = JTableUtils.getRightRenderer();
		DefaultTableCellRenderer centerRenderer = JTableUtils.getCenterRenderer();

		matchCountColumn.setHeaderValue("");
		matchCountColumn.setCellRenderer(rightRenderer);
		matchCountColumn.setPreferredWidth(150);

		matchNameColumn.setHeaderValue("Kilpailu");
		matchNameColumn.setCellRenderer(leftRenderer);
		matchNameColumn.setPreferredWidth(400);

		matchDateColumn.setHeaderValue("Pvm");
		matchDateColumn.setCellRenderer(centerRenderer);
		matchDateColumn.setPreferredWidth(200);

		stageColumn.setHeaderValue("Asema");
		stageColumn.setCellRenderer(centerRenderer);
		stageColumn.setPreferredWidth(300);

		importStageColumn.setHeaderValue("Tallenna");
		importStageColumn.setCellRenderer(centerRenderer);
		importStageColumn.setPreferredWidth(200);

		JComboBox<Object> importAsClassifierComboBox = new JComboBox<Object>();
		ComboBoxCellRenderer renderer = new ComboBoxCellRenderer();
		DefaultComboBoxModel<Object> comboBoxCellEditorModel = new DefaultComboBoxModel<Object>();
		DefaultComboBoxModel<Object> comboBoxCellRendererModel = new DefaultComboBoxModel<Object>();
		comboBoxCellEditorModel.addElement(DO_NOT_SAVE_AS_CLASSIFIER);
		comboBoxCellRendererModel.addElement(DO_NOT_SAVE_AS_CLASSIFIER);
		comboBoxCellRendererModel.addElement(ALREADY_IN_DATABASE);

		for (ClassifierStage classifier : ClassifierStage.values()) {
			comboBoxCellEditorModel.addElement(classifier);
			comboBoxCellRendererModel.addElement(classifier);
		}
		importAsClassifierComboBox.setModel(comboBoxCellEditorModel);
		importAsClassifierComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for (int row = 0; row < databaseMatchInfoTable.getRowCount(); row++) {
					Object importAsClassifierCellValue = databaseMatchInfoTable.getModel().getValueAt(row, 4);
					Stage stage = TableRowToStageMap.get(row);
					if (importAsClassifierCellValue instanceof ClassifierStage) {
						stage.setSaveAsClassifierStage((ClassifierStage) importAsClassifierCellValue);
					} else
						stage.setSaveAsClassifierStage(null);
				}
			}
		});
		importStageColumn.setCellEditor(new ImportTableCellEditor());
		renderer.setModel(comboBoxCellRendererModel);

		importStageColumn.setCellRenderer(renderer);

		return table;

	}

	private void setEditableCells(DefaultTableModel tableModel, List<Match> matches) {
		editableCellsMap.clear();
		int rowCount = 0;
		for (Match match : matches) {
			for (Stage stage : match.getStages()) {
				for (int columnCount = 0; columnCount < 4; columnCount++) {
					editableCellsMap.put(rowCount + ":" + columnCount, false);
				}
				boolean importColumnEditable = stage.isNewStage();
				if (stage.isNewStage())
					importColumnEditable = true;
				else
					importColumnEditable = false;
				editableCellsMap.put(rowCount + ":" + 4, importColumnEditable);
				rowCount++;
			}
		}
	}

	public void updateDatabaseMatchInfoTable(List<Match> winMSSMatches) {
		DefaultTableModel tableModel = (DefaultTableModel) databaseMatchInfoTable.getModel();
		tableModel.setRowCount(0);
		if (winMSSMatches == null || winMSSMatches.size() == 0) {
			return;
		}

		int matchCounter = 1;
		int rowCounter = 0;
		for (Match match : winMSSMatches) {
			Object[] rowData = new Object[5];
			rowData[0] = (matchCounter++) + ". ";
			rowData[1] = match.getName();
			rowData[2] = DateFormatUtils.calendarToDateString(match.getDate());
			if (match.getStages() == null || match.getStages().size() == 0)
				continue;
			Stage stage = match.getStages().get(0);
			rowData[3] = stage.getName();
			rowData[4] = getImportCell(stage);
			tableModel.addRow(rowData);
			TableRowToStageMap.put(rowCounter, stage);
			rowCounter++;
			if (match.getStages().size() == 1)
				continue;
			for (int i = 1; i < match.getStages().size(); i++) {
				stage = match.getStages().get(i);
				rowData = new Object[6];
				rowData[0] = "";
				rowData[1] = "";
				rowData[2] = "";
				rowData[3] = stage.getName();
				rowData[4] = getImportCell(stage);
				tableModel.addRow(rowData);
				TableRowToStageMap.put(rowCounter, stage);
				rowCounter++;
			}
		}
		setEditableCells(tableModel, winMSSMatches);
		tableModel.fireTableDataChanged();
		cardLayout.show(cardLayoutPanel, ImportTableStatus.SHOW_WINMSS_DATA.toString());
	}

	private Object getImportCell(Stage stage) {
		if (!stage.isNewStage()) {
			return ALREADY_IN_DATABASE;
		} else {
			if (stage.getSaveAsClassifierStage() != null)
				return stage.getSaveAsClassifierStage();
		}
		return DO_NOT_SAVE_AS_CLASSIFIER;
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT
				&& event.getDataImportEvent().getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE) {
			if (event.getDataImportEvent().getImportStatus() == ImportStatus.LOAD_FROM_WINMSS_DONE) {
				updateDatabaseMatchInfoTable(ImportPanelDataService.getImportResultsPanelMatchList());
			}
			if (event.getDataImportEvent().getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
				cardLayout.show(cardLayoutPanel, ImportTableStatus.IMPORT_RESULT.toString());
			}
		}
		if (event.getEventType() == GUIDataEventType.IMPORT_RESULTS_TABLE_UPDATE) {
			updateDatabaseMatchInfoTable(ImportPanelDataService.getImportResultsPanelMatchList());
		}
	}

	private class ImportTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private Object saveAsClassifierValue;

		@Override
		public Object getCellEditorValue() {
			return this.saveAsClassifierValue;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.saveAsClassifierValue = value;

			JComboBox<Object> comboBox = new JComboBox<Object>();

			comboBox.addItem(DO_NOT_SAVE_AS_CLASSIFIER);
			Stage stage = ImportPanelDataService.getImportResultsPanelStageList().get(row);

			if (ClassifierStage.contains(stage.getName())) {
				comboBox.addItem(ClassifierStage.parseString(stage.getName()));
			} else {
				for (ClassifierStage classifier : ClassifierStage.values()) {
					comboBox.addItem(classifier);
				}
			}

			comboBox.setSelectedItem(saveAsClassifierValue);
			comboBox.addActionListener(this);
			if (!isSelected) {
				comboBox.setBackground(table.getSelectionBackground());
			}
			return comboBox;

		}

		@Override
		public void actionPerformed(ActionEvent event) {
			@SuppressWarnings("unchecked")
			JComboBox<Object> comboBox = (JComboBox<Object>) event.getSource();
			this.saveAsClassifierValue = comboBox.getSelectedItem();

			if (event.getActionCommand().equals("comboBoxChanged")) {
				this.stopCellEditing();
				for (int row = 0; row < databaseMatchInfoTable.getRowCount(); row++) {
					Object importAsClassifierCellValue = databaseMatchInfoTable.getModel().getValueAt(row, 4);
					Stage stage = TableRowToStageMap.get(row);
					if (importAsClassifierCellValue instanceof ClassifierStage) {
						stage.setSaveAsClassifierStage((ClassifierStage) importAsClassifierCellValue);
					} else
						stage.setSaveAsClassifierStage(null);
				}
			}

		}
	}
}