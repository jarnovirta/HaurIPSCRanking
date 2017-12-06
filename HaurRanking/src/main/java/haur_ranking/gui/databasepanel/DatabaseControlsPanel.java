package haur_ranking.gui.databasepanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import haur_ranking.event.GUIActionEvent;
import haur_ranking.event.GUIActionEvent.GUIActionEventType;
import haur_ranking.gui.service.GUIActionEventService;
import haur_ranking.gui.service.GUIDataService;

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

		chooseStagesToDeleteButton = new JButton("Poista tulostietoja");
		chooseStagesToDeleteButton.setActionCommand("chooseStagesToDelete");
		chooseStagesToDeleteButton.addActionListener(buttonClickListener);
		this.add(chooseStagesToDeleteButton);

		deleteStagesButton = new JButton("Poista valitut tulokset");
		deleteStagesButton.setActionCommand("deleteStages");
		deleteStagesButton.addActionListener(buttonClickListener);
		deleteStagesButton.setEnabled(false);
		this.add(deleteStagesButton);

		cancelDeleteButton = new JButton("Peruuta");
		cancelDeleteButton.setActionCommand("cancelDelete");
		cancelDeleteButton.addActionListener(buttonClickListener);
		cancelDeleteButton.setEnabled(false);
		this.add(cancelDeleteButton);
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
		GUIDataService.clearStagesToDelete();
		GUIActionEventService.emit(new GUIActionEvent(GUIActionEventType.CHOOSE_STAGES_TO_DELETE_BUTTON_CLICKED));
		cancelDeleteButton.setEnabled(true);
		deleteStagesButton.setEnabled(true);
		chooseStagesToDeleteButton.setEnabled(false);
	}

	private void deleteStagesCommandHandler() {

		chooseStagesToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteStagesButton.setEnabled(false);
	}

	private void cancelDeleteCommandHandler() {
		GUIDataService.clearStagesToDelete();
		GUIActionEventService.emit(new GUIActionEvent(GUIActionEventType.CANCEL_DELETE_STAGES_BUTTON_CLICKED));
		chooseStagesToDeleteButton.setEnabled(true);
		cancelDeleteButton.setEnabled(false);
		deleteStagesButton.setEnabled(false);
	}
}
