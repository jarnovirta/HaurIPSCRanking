package haur_ranking.gui.utils;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class JTableUtils {
	public static DefaultTableCellRenderer getLeftRenderer() {
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		return leftRenderer;
	}

	public static DefaultTableCellRenderer getRightRenderer() {
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		return leftRenderer;
	}

	public static DefaultTableCellRenderer getCenterRenderer() {
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		return leftRenderer;
	}

}
