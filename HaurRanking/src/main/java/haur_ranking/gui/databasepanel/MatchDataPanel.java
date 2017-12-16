package haur_ranking.gui.databasepanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;

import haur_ranking.domain.Match;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.DatabasePanelDataService;
import haur_ranking.gui.utils.JTableUtils;
import haur_ranking.utils.DateFormatUtils;

public class MatchDataPanel extends JPanel implements GUIDataEventListener, ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTable databaseMatchInfoTable;
	private CardLayout cardLayout;

	private enum DatabaseDataTableStatus {
		NO_DATA, HAS_DATA
	};

	public MatchDataPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		databaseMatchInfoTable = getDatabaseMatchInfoTable();
		JPanel databaseMatchInfoPanel = getDatabaseMatchInfoPanel(databaseMatchInfoTable);
		add(databaseMatchInfoPanel, DatabaseDataTableStatus.HAS_DATA.toString());
		add(getNoDataPanel(), DatabaseDataTableStatus.NO_DATA.toString());
		cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
		DataEventService.addDataEventListener(this);

	}

	private JPanel getNoDataPanel() {
		JPanel noResultsPanel = new JPanel(new BorderLayout());
		noResultsPanel.setBackground(Color.WHITE);
		JLabel noResultsLabel = new JLabel("Ei tulostietoja", SwingConstants.CENTER);
		noResultsPanel.add(noResultsLabel);
		return noResultsPanel;
	}

	private JPanel getDatabaseMatchInfoPanel(JTable databaseMatchInfoTable) {
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(databaseMatchInfoTable);
		tablePanel.add(scrollPane);

		JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		paginationPanel.setPreferredSize(new Dimension(300, 50));

		JButton previousPageButton = new JButton("Edellinen");
		previousPageButton.addActionListener(this);
		previousPageButton.setActionCommand(MatchDataPanelButtonCommands.PREVIOUS_MATCH_LIST_PAGE.toString());
		paginationPanel.add(previousPageButton);
		paginationPanel.add(Box.createRigidArea(new Dimension(15, 0)));

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter integerFormatter = new NumberFormatter(format);
		integerFormatter.setValueClass(Integer.class);
		integerFormatter.setMinimum(1);
		// SWITCH TO TOTAL PAGES : ////////
		integerFormatter.setMaximum(2);
		integerFormatter.setAllowsInvalid(true);
		JFormattedTextField pageNumberInputField = new JFormattedTextField(integerFormatter);
		pageNumberInputField.setText("1");
		pageNumberInputField.setColumns(2);

		pageNumberInputField.addActionListener(new TextInputListener());
		paginationPanel.add(pageNumberInputField);

		JLabel totalPages = new JLabel(" / 4");
		paginationPanel.add(totalPages);
		totalPages.setFont(new Font(totalPages.getFont().getName(), Font.PLAIN, totalPages.getFont().getSize()));
		paginationPanel.add(Box.createRigidArea(new Dimension(15, 0)));

		JButton nextPageButton = new JButton("Seuraava");
		nextPageButton.addActionListener(this);
		nextPageButton.setActionCommand(MatchDataPanelButtonCommands.NEXT_MATCH_LIST_PAGE.toString());
		paginationPanel.add(nextPageButton);

		tablePanel.add(paginationPanel);
		return tablePanel;

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
				setSelectedStagesToDelete();
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

		DefaultTableCellRenderer centerRenderer = JTableUtils.getCenterRenderer();
		DefaultTableCellRenderer rightRenderer = JTableUtils.getRightRenderer();
		DefaultTableCellRenderer leftRenderer = JTableUtils.getLeftRenderer();

		matchCountColumn.setHeaderValue("");
		matchCountColumn.setCellRenderer(rightRenderer);
		matchCountColumn.setPreferredWidth(150);

		matchNameColumn.setHeaderValue("Kilpailu");
		matchNameColumn.setCellRenderer(leftRenderer);
		matchNameColumn.setPreferredWidth(700);

		matchDateColumn.setHeaderValue("Pvm");
		matchDateColumn.setCellRenderer(centerRenderer);
		matchDateColumn.setPreferredWidth(250);

		classifiersColumn.setHeaderValue("Luokitteluohj.");
		classifiersColumn.setCellRenderer(centerRenderer);
		classifiersColumn.setPreferredWidth(250);

		return table;
	}

	private void updateDatabaseMatchInfoTable(List<Match> databaseMatchTableData) {
		if (databaseMatchTableData == null || databaseMatchTableData.size() == 0) {
			cardLayout.show(this, DatabaseDataTableStatus.NO_DATA.toString());
			return;
		}

		DefaultTableModel tableModel = (DefaultTableModel) databaseMatchInfoTable.getModel();
		tableModel.setRowCount(0);

		int rowCounter = 0;
		for (Match match : databaseMatchTableData) {
			String[] row = new String[4];
			row[0] = rowCounter + 1 + ". ";
			row[1] = match.getName();
			row[2] = DateFormatUtils.calendarToDateString(match.getDate());
			if (match.getStages() != null) {
				if (match.getStages().size() > 0) {
					row[3] = match.getStages().get(0).getName();
					tableModel.addRow(row);
					rowCounter++;
				}
				if (match.getStages().size() > 1) {
					for (int i = 1; i < match.getStages().size(); i++) {
						row = new String[4];
						row[0] = "";
						row[1] = "";
						row[2] = "";
						row[3] = match.getStages().get(i).getName();
						tableModel.addRow(row);
						rowCounter++;
					}
				}
			}
		}

		tableModel.fireTableDataChanged();
		if (databaseMatchInfoTable.getRowCount() > 0) {
			databaseMatchInfoTable.setRowSelectionInterval(0, 0);

		}
		cardLayout.show(this, DatabaseDataTableStatus.HAS_DATA.toString());
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.DATABASE_MATCH_TABLE_UPDATE) {
			updateDatabaseMatchInfoTable(DatabasePanelDataService.getDatabaseMatchInfoTableData());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MatchDataPanelButtonCommands.CHOOSE_STAGES_TO_DELETE.toString())) {
			databaseMatchInfoTable.setRowSelectionAllowed(true);
			if (databaseMatchInfoTable.getRowCount() > 0) {
				databaseMatchInfoTable.setRowSelectionInterval(0, 0);
			}
			setSelectedStagesToDelete();
		}
		if (command.equals(MatchDataPanelButtonCommands.DELETE_STAGES.toString())) {
			databaseMatchInfoTable.setRowSelectionAllowed(false);
		}

		if (command.equals(MatchDataPanelButtonCommands.CANCEL_STAGE_DELETE.toString())) {
			databaseMatchInfoTable.setRowSelectionAllowed(false);
		}
		if (command.equals(MatchDataPanelButtonCommands.PREVIOUS_MATCH_LIST_PAGE.toString())) {
			previousPageCommandHandler();
		}
		if (command.equals(MatchDataPanelButtonCommands.NEXT_MATCH_LIST_PAGE.toString())) {
			nextPageCommandHandler();
		}

	}

	private void setSelectedStagesToDelete() {
		DatabasePanelDataService.clearStagesToDelete();
		int[] selectedRowIndexes = databaseMatchInfoTable.getSelectedRows();
		for (int index : selectedRowIndexes) {
			DatabasePanelDataService.getDatabaseMatchInfoTableStagesToDelete()
					.add(DatabasePanelDataService.getDatabaseMatchInfoTableStages().get(index));
		}
	}

	private class TextInputListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String input = event.getActionCommand();
			System.out.println(input);
		}
	}

	private void previousPageCommandHandler() {
		System.out.println("Previous page");
	}

	private void nextPageCommandHandler() {
		System.out.println("Next page");
	}

}
