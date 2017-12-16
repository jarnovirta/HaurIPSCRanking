package haur_ranking.gui.importpanel;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.gui.service.DataEventService;

public class ImportResultPanel extends JPanel implements GUIDataEventListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JLabel newCompetitorsLabel = new JLabel();
	JLabel newStagesLabel = new JLabel();
	JLabel newScoreSheetsLabel = new JLabel();
	JLabel oldScoreSheetsRemovedLabel = new JLabel();

	public ImportResultPanel() {

		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		JPanel importStatisticsPanel = new JPanel();
		importStatisticsPanel.setBackground(Color.WHITE);
		importStatisticsPanel.setLayout(new BoxLayout(importStatisticsPanel, BoxLayout.Y_AXIS));
		JLabel noResultsLabel = new JLabel("Tulostiedot tallennettu!");
		importStatisticsPanel.add(noResultsLabel);
		importStatisticsPanel.add(newStagesLabel);
		importStatisticsPanel.add(newScoreSheetsLabel);
		importStatisticsPanel.add(oldScoreSheetsRemovedLabel);
		importStatisticsPanel.add(newCompetitorsLabel);

		add(importStatisticsPanel);

		DataEventService.addDataEventListener(this);

	}

	@Override
	public void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT
				&& event.getDataImportEvent().getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
				&& event.getDataImportEvent().getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
			DataImportEvent importEvent = event.getDataImportEvent();
			newStagesLabel.setText("Luokitteluammuntoja: " + importEvent.getNewStagesCount());
			newScoreSheetsLabel.setText("Tuloksia: " + importEvent.getNewScoreSheetsCount());
			oldScoreSheetsRemovedLabel.setText("(vanhoja tuloksia samoihin luokitteluohjelmiin poistettu: "
					+ importEvent.getOldScoreSheetsRemovedCount() + ")");
			newCompetitorsLabel.setText("Uusia kilpailijoita: " + importEvent.getNewCompetitorsCount());
		}
	}
}
