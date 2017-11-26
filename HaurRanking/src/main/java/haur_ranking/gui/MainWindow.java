package haur_ranking.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import haur_ranking.domain.Ranking;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;
import haur_ranking.service.RankingService;

public class MainWindow {

	private JFrame mainFrame;
	private JLabel headerLabel;

	private JLabel databaseMatchCountPanel;

	private JLabel databaseStageAndCompetitorCountPanel;

	private JPanel controlPanel;

	private String lastMSSDbFileLocation = null;

	private static Ranking ranking;

	private void initializeFonts() {
		float multiplier = 1.9f;
		UIDefaults defaults = UIManager.getDefaults();
		Enumeration<Object> e = defaults.keys();
		while (e.hasMoreElements()) {
			Object key = e.nextElement();
			Object value = defaults.get(key);
			if (value instanceof Font) {
				Font font = (Font) value;
				int newSize = Math.round(font.getSize() * multiplier);
				if (value instanceof FontUIResource) {
					defaults.put(key, new FontUIResource(font.getName(), font.getStyle(), newSize));
				} else {
					defaults.put(key, new Font(font.getName(), font.getStyle(), newSize));
				}
			}
		}
		FontUIResource font = new FontUIResource("Times New", Font.PLAIN, 26);
		UIManager.put("Table.font", font);
	}

	public void prepareGUI() {
		initializeFonts();

		mainFrame = new JFrame("HAUR Ranking");
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setMinimumSize(new Dimension(1300, 700));
		mainFrame.setResizable(false);
		mainFrame.setLayout(new BorderLayout());
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(ClassLoader.getSystemResource("images/small_haur_logo.png")));
			icons.add(ImageIO.read(ClassLoader.getSystemResource("images/large_haur_logo.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.setIconImages(icons);

		JTabbedPane tabbedMainPane = new JTabbedPane();
		tabbedMainPane.addTab("Ranking", new RankingPanel(mainFrame, RankingService.getRanking()));
		tabbedMainPane.add("Tietokanta", new DatabasePanel(mainFrame));

		mainFrame.add(tabbedMainPane);
		mainFrame.getContentPane().add(tabbedMainPane, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				WinMssDatabaseUtil.closeConnection();
				HaurRankingDatabaseUtils.closeEntityManagerFactory();
				System.exit(0);
			}
		});

	}

	public void showHaurRankingGui() {
		updateDatabaseStaticsPanel();
		// JButton importCompetitions = new JButton("Tuo kilpailuja");
		// importCompetitions.setActionCommand("importCompetitions");
		// importCompetitions.addActionListener(new ButtonClickListener());
		// controlPanel.add(importCompetitions);
		// importCompetitions = new JButton("Luo ranking");
		// importCompetitions.setActionCommand("generateRanking");
		// importCompetitions.addActionListener(new ButtonClickListener());
		// controlPanel.add(importCompetitions);
		mainFrame.setVisible(true);
		RankingDataService.updateRankingData();
	}

	private void updateDatabaseStaticsPanel() {
		// DatabaseStatistics databaseStatistics =
		// DatabaseStatisticsService.getDatabaseStatistics();
		// databaseMatchCountPanel.setText("Kilpailuja : " +
		// databaseStatistics.getMatchCount());
		// databaseStageAndCompetitorCountPanel.setText("Asematuloksia : " +
		// databaseStatistics.getStageCount() + " ("
		// + databaseStatistics.getCompetitorCount() + " kilpailijaa)");
	}

}
