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

import haur_ranking.gui.MainWindow;

public class CompetitorDataControlsPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JButton chooseCompetitorsToDeleteButton;
	JButton deleteCompetitorsButton;
	JButton cancelDeleteButton;
	ButtonClickListener buttonClickListener = new ButtonClickListener();

	public CompetitorDataControlsPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 20));

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		JLabel instructionLine = new JLabel("Poista kilpailijoita");
		instructionLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionLine
				.setFont(new Font(instructionLine.getFont().getName(), Font.BOLD, instructionLine.getFont().getSize()));
		controlsPanel.add(instructionLine);

		controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		chooseCompetitorsToDeleteButton = new JButton("Valitse");
		chooseCompetitorsToDeleteButton.setActionCommand("chooseStagesToDelete");
		chooseCompetitorsToDeleteButton.addActionListener(buttonClickListener);
		buttonsPanel.add(chooseCompetitorsToDeleteButton);

		deleteCompetitorsButton = new JButton("Poista");
		deleteCompetitorsButton.setActionCommand("deleteStages");
		deleteCompetitorsButton.addActionListener(buttonClickListener);
		deleteCompetitorsButton.setEnabled(false);
		buttonsPanel.add(deleteCompetitorsButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand("cancelDelete");
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);
		buttonsPanel.add(cancelDeleteButton);
		controlsPanel.add(buttonsPanel);

		add(controlsPanel, BorderLayout.SOUTH);

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
		deleteCompetitorsButton.setEnabled(true);
		chooseCompetitorsToDeleteButton.setEnabled(false);
	}

	private void deleteStagesCommandHandler() {

		chooseCompetitorsToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteCompetitorsButton.setEnabled(false);

	}

	private void cancelDeleteCommandHandler() {
		chooseCompetitorsToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteCompetitorsButton.setEnabled(false);
	}

	public void addButtonClickListener(ActionListener listener) {
		chooseCompetitorsToDeleteButton.addActionListener(listener);
		deleteCompetitorsButton.addActionListener(listener);
		cancelDeleteButton.addActionListener(listener);
	}
}