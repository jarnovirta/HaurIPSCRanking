package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
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

public class MatchStatisticsAndControlsPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JTable statisticsTable;
	private JButton chooseStagesToDeleteButton;
	private JButton deleteStagesButton;
	private JButton cancelDeleteButton;
	private ButtonClickListener buttonClickListener = new ButtonClickListener();

	public MatchStatisticsAndControlsPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 20));
		statisticsTable = getStatisticsTable();
		statisticsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(statisticsTable, BorderLayout.NORTH);
		add(getControlsPanel(), BorderLayout.SOUTH);
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

	private JPanel getControlsPanel() {
		JPanel matchControlsPanel = new JPanel();
		matchControlsPanel.setLayout(new BoxLayout(matchControlsPanel, BoxLayout.Y_AXIS));
		matchControlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel instructionLine = new JLabel("Poista tulostietoja");
		instructionLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionLine
				.setFont(new Font(instructionLine.getFont().getName(), Font.BOLD, instructionLine.getFont().getSize()));
		matchControlsPanel.add(instructionLine);

		matchControlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		chooseStagesToDeleteButton = new JButton("Valitse");
		chooseStagesToDeleteButton.setActionCommand("chooseStagesToDelete");
		chooseStagesToDeleteButton.addActionListener(buttonClickListener);
		buttonsPanel.add(chooseStagesToDeleteButton, BorderLayout.WEST);

		JPanel deleteCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		deleteStagesButton = new JButton("Poista");
		deleteStagesButton.setActionCommand("deleteStages");
		deleteStagesButton.addActionListener(buttonClickListener);
		deleteStagesButton.setEnabled(false);
		deleteCancelButtonsPanel.add(deleteStagesButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand("cancelDelete");
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);
		deleteCancelButtonsPanel.add(cancelDeleteButton);

		buttonsPanel.add(deleteCancelButtonsPanel, BorderLayout.EAST);

		matchControlsPanel.add(buttonsPanel);

		return matchControlsPanel;
	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("chooseStagesToDelete")) {
				chooseStagesToDeleteCommandHandler();
			}
			if (command.equals("deleteStages")) {
				deleteStagesCommandHandler();
			}
			if (command.equals("cancelDelete")) {
				cancelDeleteCommandHandler();
			}
		}
	}

	private void chooseStagesToDeleteCommandHandler() {
		cancelDeleteButton.setEnabled(true);
		deleteStagesButton.setEnabled(true);
		chooseStagesToDeleteButton.setEnabled(false);
	}

	private void deleteStagesCommandHandler() {
		DataService.deleteStages();
		chooseStagesToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteStagesButton.setEnabled(false);

	}

	private void cancelDeleteCommandHandler() {
		DataService.clearStagesToDelete();
		chooseStagesToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteStagesButton.setEnabled(false);
	}

	public void addButtonClickListener(ActionListener listener) {
		chooseStagesToDeleteButton.addActionListener(listener);
		deleteStagesButton.addActionListener(listener);
		cancelDeleteButton.addActionListener(listener);
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE) {
			if (DataService.getDatabaseStatistics() != null)
				setStatisticsTableData(DataService.getDatabaseStatistics());
		}
	}

}
