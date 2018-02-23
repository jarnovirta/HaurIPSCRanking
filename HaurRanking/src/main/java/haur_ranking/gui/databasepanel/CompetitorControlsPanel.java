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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.DatabasePanelDataService;

public class CompetitorControlsPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JButton chooseCompetitorsToDeleteButton;
	JButton deleteCompetitorsButton;
	JButton cancelDeleteButton;

	JLabel competitorCountLabel;

	ButtonClickListener buttonClickListener = new ButtonClickListener();

	public CompetitorControlsPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 20));

		add(getCompetitorStatisticsPanel(), BorderLayout.NORTH);
		add(getControlButtonsPanel(), BorderLayout.SOUTH);

		DataEventService.addDataEventListener(this);

	}

	private JPanel getCompetitorStatisticsPanel() {
		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setLayout(new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS));
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statisticsLabel = new JLabel("Ampujia:");
		competitorCountLabel = new JLabel();
		labelPanel.add(statisticsLabel);
		labelPanel.add(competitorCountLabel);
		competitorCountLabel.setFont(new Font(competitorCountLabel.getFont().getName(), Font.PLAIN,
				competitorCountLabel.getFont().getSize()));
		statisticsPanel.add(labelPanel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		return statisticsPanel;
	}

	private JPanel getControlButtonsPanel() {
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		JLabel instructionLine = new JLabel("Poista ampujia");
		instructionLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionLine
				.setFont(new Font(instructionLine.getFont().getName(), Font.BOLD, instructionLine.getFont().getSize()));
		controlsPanel.add(instructionLine);

		controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		chooseCompetitorsToDeleteButton = new JButton("Valitse");
		chooseCompetitorsToDeleteButton
				.setActionCommand(CompetitorDataPanelButtonCommands.CHOOSE_COMPETITORS_TO_DELETE.toString());
		chooseCompetitorsToDeleteButton.addActionListener(buttonClickListener);
		setChooseCompetitorsToDeleteButtonEnabled();
		buttonsPanel.add(chooseCompetitorsToDeleteButton, BorderLayout.WEST);

		JPanel deleteCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		deleteCompetitorsButton = new JButton("Poista");
		deleteCompetitorsButton.setActionCommand(CompetitorDataPanelButtonCommands.DELETE_COMPETITORS.toString());
		deleteCompetitorsButton.addActionListener(buttonClickListener);
		deleteCompetitorsButton.setEnabled(false);
		deleteCancelButtonsPanel.add(deleteCompetitorsButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand(CompetitorDataPanelButtonCommands.CANCEL_DELETE_COMPETITORS.toString());
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);
		deleteCancelButtonsPanel.add(cancelDeleteButton);

		buttonsPanel.add(deleteCancelButtonsPanel, BorderLayout.EAST);
		controlsPanel.add(buttonsPanel);

		return controlsPanel;
	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals(CompetitorDataPanelButtonCommands.CHOOSE_COMPETITORS_TO_DELETE.toString())) {
				chooseCompetitorsToDeleteCommandHandler();
			}
			if (command.equals(CompetitorDataPanelButtonCommands.DELETE_COMPETITORS.toString())) {
				deleteCompetitorsCommandHandler();
			}
			if (command.equals(CompetitorDataPanelButtonCommands.CANCEL_DELETE_COMPETITORS.toString())) {
				cancelDeleteCompetitorsCommandHandler();
			}
		}
	}

	private void chooseCompetitorsToDeleteCommandHandler() {
		cancelDeleteButton.setEnabled(true);
		deleteCompetitorsButton.setEnabled(true);
		chooseCompetitorsToDeleteButton.setEnabled(false);
	}

	private void deleteCompetitorsCommandHandler() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Delete competitor(s)?", "Confirm", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			DatabasePanelDataService.deleteCompetitors();
		}
		setChooseCompetitorsToDeleteButtonEnabled();
		cancelDeleteButton.setEnabled(false);
		deleteCompetitorsButton.setEnabled(false);

	}

	private void cancelDeleteCompetitorsCommandHandler() {
		setChooseCompetitorsToDeleteButtonEnabled();
		cancelDeleteButton.setEnabled(false);
		deleteCompetitorsButton.setEnabled(false);
	}

	public void addButtonClickListener(ActionListener listener) {
		chooseCompetitorsToDeleteButton.addActionListener(listener);
		deleteCompetitorsButton.addActionListener(listener);
		cancelDeleteButton.addActionListener(listener);
	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.DATABASE_COMPETITOR_TABLE_UPDATE) {
			setChooseCompetitorsToDeleteButtonEnabled();
			cancelDeleteButton.setEnabled(false);
			deleteCompetitorsButton.setEnabled(false);
			competitorCountLabel.setText(String.valueOf(DatabasePanelDataService.getDatabaseCompetitorCount()));
		}
	}

	private void setChooseCompetitorsToDeleteButtonEnabled() {
		if (DatabasePanelDataService.getDatabaseCompetitorInfoTableData() != null
				&& DatabasePanelDataService.getDatabaseCompetitorInfoTableData().size() > 0) {
			chooseCompetitorsToDeleteButton.setEnabled(true);
		} else {
			chooseCompetitorsToDeleteButton.setEnabled(false);
		}
	}
}