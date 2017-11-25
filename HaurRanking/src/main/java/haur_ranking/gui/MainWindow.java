package haur_ranking.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import haur_ranking.domain.Ranking;
import haur_ranking.gui.filters.WinMSSFileFilter;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;

public class MainWindow {

	private JFrame mainFrame;
	private JLabel headerLabel;

	private JLabel databaseMatchCountPanel;

	private JLabel databaseStageAndCompetitorCountPanel;

	private JPanel controlPanel;

	private String lastMSSDbFileLocation = null;

	private Ranking ranking;

	private void importCompetitionsCommandHandler() {
		JFileChooser fileChooser;
		if (lastMSSDbFileLocation != null)
			fileChooser = new JFileChooser(lastMSSDbFileLocation);
		else
			fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new WinMSSFileFilter());
		int returnVal = fileChooser.showOpenDialog(mainFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String absoluteFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			lastMSSDbFileLocation = Paths.get(absoluteFilePath).getParent().toString();
			System.out.println("Loading database from " + absoluteFilePath);
			MatchService.importWinMssDatabase(absoluteFilePath);
			updateDatabaseStaticsPanel();
		}
	}

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

	public Ranking getRanking() {
		return ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("importCompetitions")) {
				importCompetitionsCommandHandler();
			}
			if (command.equals("generateRanking")) {
				RankingService.getRanking();
			}
		}
	}

}
