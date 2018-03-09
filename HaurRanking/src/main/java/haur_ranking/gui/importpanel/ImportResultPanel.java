package haur_ranking.gui.importpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import haur_ranking.domain.Stage;
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.utils.DateFormatUtils;

public class ImportResultPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JTable importStatisticsTable;

	private JPanel invalidClassifiersPanel;
	private JTextArea invalidClassifiersTextArea;

	private JPanel stagesWithNoResultsPanel;
	private JTextArea stagesWithNoResultsTextArea;

	public ImportResultPanel() {
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		panel.add(getStatisticsPanel());
		invalidClassifiersPanel = getInvalidClassifiersPanel();
		panel.add(invalidClassifiersPanel);
		stagesWithNoResultsPanel = getStagesWithNoResultPanel();
		panel.add(stagesWithNoResultsPanel);
		JScrollPane scrollPane = new JScrollPane(panel);
		// scrollPane.setPreferredSize(new Dimension(MainWindow.RIGHT_PANE_WIDTH
		// - 50, MainWindow.HEIGHT - 200));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(scrollPane);

		DataEventService.addDataEventListener(this);

	}

	private JPanel getStatisticsPanel() {
		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setMaximumSize(new Dimension(600, 400));
		importStatisticsTable = getImportStatisticsTable();
		importStatisticsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		statisticsPanel.setBackground(Color.WHITE);
		statisticsPanel.setLayout(new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel("Tulostiedot tallennettu ja ranking päivitetty!");
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		statisticsPanel.add(titleLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		statisticsPanel.add(importStatisticsTable);

		return statisticsPanel;

	}

	private JPanel getInvalidClassifiersPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		JLabel titleLabel = new JLabel("<html>Asema ei vastaa valittua luokitteluohjelmaa<br>(ei tallennettu):");
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(titleLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));

		invalidClassifiersTextArea = new JTextArea();
		invalidClassifiersTextArea.setLineWrap(true);
		invalidClassifiersTextArea.setBackground(Color.WHITE);
		invalidClassifiersTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(invalidClassifiersTextArea);

		panel.setVisible(false);
		return panel;
	}

	private JPanel getStagesWithNoResultPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		JLabel titleLabel = new JLabel("Asemaan ei WinMSS-tulostietoja (ei tallennettu): ");
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(titleLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));

		stagesWithNoResultsTextArea = new JTextArea();
		stagesWithNoResultsTextArea.setLineWrap(true);
		stagesWithNoResultsTextArea.setBackground(Color.WHITE);
		stagesWithNoResultsTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(stagesWithNoResultsTextArea);
		panel.setVisible(false);
		return panel;
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
		statisticsTableModel.setValueAt("Uusia henkilöitä:", 4, 0);

		return statisticsTable;
	}

	private void updateImportStatisticsData(DataImportEvent importEvent) {
		DefaultTableModel statisticsTableModel = (DefaultTableModel) importStatisticsTable.getModel();
		statisticsTableModel.setValueAt(importEvent.getNewStagesCount(), 0, 1);
		statisticsTableModel.setValueAt(importEvent.getNewScoreSheetsCount(), 1, 1);
		statisticsTableModel.setValueAt(importEvent.getOldScoreSheetsRemovedCount(), 3, 1);
		statisticsTableModel.setValueAt(importEvent.getNewCompetitorsCount(), 4, 1);

		if (importEvent.getInvalidClassifiers() != null && importEvent.getInvalidClassifiers().size() > 0) {
			invalidClassifiersTextArea.setText(getClassifierListString(importEvent.getInvalidClassifiers()));
			invalidClassifiersPanel.setVisible(true);
		} else {
			invalidClassifiersPanel.setVisible(false);
		}

		if (importEvent.getStagesWithNoScoreSheets() != null && importEvent.getStagesWithNoScoreSheets().size() > 0) {
			stagesWithNoResultsTextArea.setText(getClassifierListString(importEvent.getStagesWithNoScoreSheets()));
			stagesWithNoResultsPanel.setVisible(true);
		} else {
			stagesWithNoResultsPanel.setVisible(false);
		}

	}

	private String getClassifierListString(List<Stage> classifierStages) {

		String invalidClassifiersString = "";
		boolean firstItem = true;
		for (Stage stage : classifierStages) {
			if (!firstItem) {
				invalidClassifiersString += "\n";

			} else {
				firstItem = false;
			}
			invalidClassifiersString += "- Kilpailu \"" + stage.getMatch().getName() + "\" ("
					+ DateFormatUtils.calendarToDateString(stage.getMatch().getDate()) + ") -  asema: "
					+ stage.getName();
		}

		return invalidClassifiersString;
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT
				&& event.getDataImportEvent().getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
				&& event.getDataImportEvent().getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
			updateImportStatisticsData(event.getDataImportEvent());
		}
	}
}
