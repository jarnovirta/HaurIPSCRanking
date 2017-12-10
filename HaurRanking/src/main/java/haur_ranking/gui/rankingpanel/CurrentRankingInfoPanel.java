package haur_ranking.gui.rankingpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.MainWindow;
import haur_ranking.gui.service.DataService;
import haur_ranking.utils.DateFormatUtils;

public class CurrentRankingInfoPanel extends JPanel implements GUIDataEventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JLabel infoLabel;

	public CurrentRankingInfoPanel() {
		int verticalSpacingBetweenPanes = 60;
		setPreferredSize(
				new Dimension(MainWindow.LEFT_PANE_WIDTH, (MainWindow.HEIGHT - verticalSpacingBetweenPanes) / 2));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		JPanel rankingInfoPanel = new JPanel();
		rankingInfoPanel.setLayout(new BoxLayout(rankingInfoPanel, BoxLayout.Y_AXIS));
		rankingInfoPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
		infoLabel = new JLabel("Haur Ranking");
		rankingInfoPanel.add(infoLabel);
		infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.BOLD, infoLabel.getFont().getSize()));
		rankingInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		add(rankingInfoPanel, BorderLayout.NORTH);

		DataService.addDataEventListener(this);

	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.GUI_DATA_UPDATE && DataService.getRanking() != null
				&& DataService.getRanking().getDate() != null) {
			infoLabel.setText(
					"Haur Ranking " + DateFormatUtils.calendarToDateString(DataService.getRanking().getDate()));
		}
	}
}
