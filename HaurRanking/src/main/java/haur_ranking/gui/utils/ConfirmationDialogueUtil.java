package haur_ranking.gui.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ConfirmationDialogueUtil {
	public static int getConfirmation(Component parentComponent, String contentText) {
		Object[] options = { "Kyllä", "Ei" };
		return JOptionPane.showOptionDialog(parentComponent, contentText, "Vahvista", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
}
