package haur_ranking.gui.importpanel;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ComboBoxCellRenderer extends JComboBox<Object> implements TableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof String && ((String) value).equals("Tallennettu")) {
			String cellValue = (String) value;
			DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
			defaultRenderer.setText(cellValue);
			defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			return defaultRenderer;
		}
		setSelectedItem(value);

		return this;
	}
}
