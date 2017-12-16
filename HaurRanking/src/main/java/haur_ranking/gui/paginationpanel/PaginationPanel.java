package haur_ranking.gui.paginationpanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import haur_ranking.event.PaginationEventListener;
import haur_ranking.gui.databasepanel.MatchDataPanelButtonCommands;

public class PaginationPanel extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int currentPage;
	private int totalPages;
	JFormattedTextField pageNumberInputField;
	JLabel totalPagesLabel;
	PaginationEventListener eventListener;

	public PaginationPanel(PaginationEventListener listener) {
		this.eventListener = listener;
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setMaximumSize(new Dimension(450, 50));

		JButton previousPageButton = new JButton("Edellinen");
		previousPageButton.addActionListener(this);
		previousPageButton.setActionCommand(MatchDataPanelButtonCommands.PREVIOUS_MATCH_LIST_PAGE.toString());
		add(previousPageButton);
		add(Box.createRigidArea(new Dimension(15, 0)));

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter integerFormatter = new NumberFormatter(format);
		integerFormatter.setValueClass(Integer.class);
		integerFormatter.setMinimum(1);
		integerFormatter.setAllowsInvalid(true);
		pageNumberInputField = new JFormattedTextField(integerFormatter);
		pageNumberInputField.setText(String.valueOf(currentPage));
		pageNumberInputField.setColumns(2);

		pageNumberInputField.addActionListener(new TextInputListener());
		add(pageNumberInputField);

		totalPagesLabel = new JLabel(" / " + String.valueOf(totalPages));
		add(totalPagesLabel);
		totalPagesLabel.setFont(
				new Font(totalPagesLabel.getFont().getName(), Font.PLAIN, totalPagesLabel.getFont().getSize()));
		add(Box.createRigidArea(new Dimension(15, 0)));

		JButton nextPageButton = new JButton("Seuraava");
		nextPageButton.addActionListener(this);
		nextPageButton.setActionCommand(MatchDataPanelButtonCommands.NEXT_MATCH_LIST_PAGE.toString());
		add(nextPageButton);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MatchDataPanelButtonCommands.PREVIOUS_MATCH_LIST_PAGE.toString())) {
			if (currentPage > 1) {
				eventListener.changePage(currentPage - 1);
			}
		}
		if (command.equals(MatchDataPanelButtonCommands.NEXT_MATCH_LIST_PAGE.toString())) {
			if (currentPage < totalPages)
				eventListener.changePage(currentPage + 1);
		}
	}

	private class TextInputListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() != null) {
				int pageNumber = Integer.valueOf(event.getActionCommand());
				eventListener.changePage(pageNumber);
			}
		}
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		pageNumberInputField.setText(String.valueOf(currentPage));
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		if (totalPages > 1)
			this.setVisible(true);
		else
			this.setVisible(false);
		((NumberFormatter) pageNumberInputField.getFormatter()).setMaximum(totalPages);
		totalPagesLabel.setText(" / " + String.valueOf(totalPages));
	}
}
