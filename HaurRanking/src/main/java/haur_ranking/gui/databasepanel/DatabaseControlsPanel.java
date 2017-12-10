package haur_ranking.gui.databasepanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import haur_ranking.gui.service.DataService;

public class DatabaseControlsPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JButton chooseStagesToDeleteButton;
	private JButton deleteStagesButton;
	private JButton cancelDeleteButton;
	private ButtonClickListener buttonClickListener = new ButtonClickListener();

	public DatabaseControlsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel instructionLine = new JLabel("Poista tulostietoja");
		instructionLine.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionLine
				.setFont(new Font(instructionLine.getFont().getName(), Font.BOLD, instructionLine.getFont().getSize()));
		add(instructionLine);

		add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		chooseStagesToDeleteButton = new JButton("Valitse");
		chooseStagesToDeleteButton.setActionCommand("chooseStagesToDelete");
		chooseStagesToDeleteButton.addActionListener(buttonClickListener);
		controlsPanel.add(chooseStagesToDeleteButton);

		deleteStagesButton = new JButton("Poista");
		deleteStagesButton.setActionCommand("deleteStages");
		deleteStagesButton.addActionListener(buttonClickListener);
		deleteStagesButton.setEnabled(false);
		controlsPanel.add(deleteStagesButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand("cancelDelete");
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);
		controlsPanel.add(cancelDeleteButton);
		add(controlsPanel);

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
}
