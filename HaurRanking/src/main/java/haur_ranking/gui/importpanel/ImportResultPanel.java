package haur_ranking.gui.importpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.DataEventService;

public class ImportResultPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JTable importStatisticsTable;

	public ImportResultPanel() {
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());

		importStatisticsTable = getImportStatisticsTable();
		importStatisticsTable.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setBackground(Color.WHITE);
		statisticsPanel.setLayout(new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel("Tulostiedot tallennettu ja ranking päivitetty!");
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		statisticsPanel.add(titleLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		statisticsPanel.add(importStatisticsTable);

		add(statisticsPanel);
		DataEventService.addDataEventListener(this);

	}

	private JTable getImportStatisticsTable() {
		JTable statisticsTable = new JTable(5, 2) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		statisticsTable.setShowGrid(false);
		statisticsTable.setDragEnabled(false);
		statisticsTable.setOpaque(false);
		statisticsTable.setRowHeight(35);
		DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) statisticsTable
				.getDefaultRenderer(Object.class);
		cellRenderer.setOpaque(false);

		TableColumn leftColumn = statisticsTable.getColumnModel().getColumn(0);
		leftColumn.setPreferredWidth(400);
		TableColumn rightColumn = statisticsTable.getColumnModel().getColumn(1);
		rightColumn.setPreferredWidth(130);
		DefaultTableModel statisticsTableModel = (DefaultTableModel) statisticsTable.getModel();
		statisticsTableModel.setValueAt("Uusia luokitteluammuntoja:", 0, 0);
		statisticsTableModel.setValueAt("Uusia tuloksia:", 1, 0);
		statisticsTableModel.setValueAt("Vanhoja tuloksia samoihin ", 2, 0);
		statisticsTableModel.setValueAt("luokitteluohjelmiin poistettu:", 3, 0);
		statisticsTableModel.setValueAt("Uusia kilpailijoita:", 4, 0);

		return statisticsTable;
	}

	private void updateImportStatisticsTableData(DataImportEvent importEvent) {
		DefaultTableModel statisticsTableModel = (DefaultTableModel) importStatisticsTable.getModel();
		statisticsTableModel.setValueAt(importEvent.getNewStagesCount(), 0, 1);
		statisticsTableModel.setValueAt(importEvent.getNewScoreSheetsCount(), 1, 1);
		statisticsTableModel.setValueAt(importEvent.getOldScoreSheetsRemovedCount(), 3, 1);
		statisticsTableModel.setValueAt(importEvent.getNewCompetitorsCount(), 4, 1);
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT
				&& event.getDataImportEvent().getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
				&& event.getDataImportEvent().getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
			updateImportStatisticsTableData(event.getDataImportEvent());
		}
	}
}
