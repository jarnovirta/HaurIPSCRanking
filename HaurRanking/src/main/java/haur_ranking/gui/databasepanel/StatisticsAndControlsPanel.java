package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.service.DataService;

public class StatisticsAndControlsPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JTable statisticsTable;

	public StatisticsAndControlsPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 20));
		statisticsTable = getStatisticsTable();
		statisticsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(statisticsTable, BorderLayout.NORTH);
		add(new DatabaseControlsPanel(), BorderLayout.SOUTH);
		DataService.addDataEventListener(this);

	}

	private JTable getStatisticsTable() {
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
		leftColumn.setPreferredWidth(200);
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
		statisticsTableModel.setValueAt("Luokitteluohjelmia", 3, 0);
		statisticsTableModel.setValueAt("joissa väh. 2 tulosta:", 4, 0);

		statisticsTableModel.setValueAt(statistics.getValidClassifiersCount(), 4, 1);
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			if (DataService.getDatabaseStatistics() != null)
				setStatisticsTableData(DataService.getDatabaseStatistics());
		}
	}
}
