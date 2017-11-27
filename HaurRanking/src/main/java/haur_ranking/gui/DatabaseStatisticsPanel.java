package haur_ranking.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.Event.NewGUIDataEvent;
import haur_ranking.Event.NewGUIDataEventListener;
import haur_ranking.domain.DatabaseStatistics;

public class DatabaseStatisticsPanel extends JPanel implements NewGUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JTable statisticsTable;

	public DatabaseStatisticsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 0, 0));
		statisticsTable = getStatisticsTable();
		statisticsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(statisticsTable);
		setMaximumSize(new Dimension(410, 310));
		GUIDataService.addRankingDataUpdatedEventListener(this);

	}

	private JTable getStatisticsTable() {
		JTable statisticsTable = new JTable(3, 2);
		statisticsTable.setShowGrid(false);
		statisticsTable.setOpaque(false);
		statisticsTable.setRowHeight(35);
		DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) statisticsTable
				.getDefaultRenderer(Object.class);
		cellRenderer.setOpaque(false);

		TableColumn leftColumn = statisticsTable.getColumnModel().getColumn(0);
		leftColumn.setPreferredWidth(160);
		TableColumn rightColumn = statisticsTable.getColumnModel().getColumn(1);
		rightColumn.setPreferredWidth(130);
		return statisticsTable;
	}

	private void setStatisticsTableData(DatabaseStatistics statistics) {
		DefaultTableModel statisticsTableModel = (DefaultTableModel) statisticsTable.getModel();
		statisticsTableModel.setValueAt("Kilpailuja:", 0, 0);
		statisticsTableModel.setValueAt(statistics.getMatchCount(), 0, 1);
		statisticsTableModel.setValueAt("Tuloksia:", 1, 0);
		statisticsTableModel.setValueAt(statistics.getStageScoreSheetCount(), 1, 1);
		statisticsTableModel.setValueAt("Kilpailijoita:", 2, 0);
		statisticsTableModel.setValueAt(statistics.getCompetitorCount(), 2, 1);

	}

	@Override
	public void updateGUIData(NewGUIDataEvent event) {
		setStatisticsTableData(event.getDatabaseStatistics());
	}
}
