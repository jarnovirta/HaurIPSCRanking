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
import javax.swing.JTabbedPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import haur_ranking.gui.databasepanel.DatabasePanel;
import haur_ranking.gui.importpanel.ImportPanel;
import haur_ranking.gui.rankingpanel.RankingPanel;
import haur_ranking.gui.service.DataEventService;
import haur_ranking.gui.service.DatabasePanelDataService;
import haur_ranking.gui.service.RankingPanelDataService;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;
import haur_ranking.repository.winmss_repository.implementation.WinMSSDatabaseUtil;

public class MainWindow {

	private JFrame mainFrame;
	public static final int WIDTH = 1250;
	public static final int HEIGHT = 800;
	public static final int MIN_WIDTH = 900;
	public static final int MIN_HEIGHT = 700;
	public static final int LEFT_PANE_WIDTH = 450;
	public static final int RIGHT_PANE_WIDTH = WIDTH - LEFT_PANE_WIDTH;

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
		mainFrame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainFrame.setResizable(true);
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
		tabbedMainPane.addTab("Ranking", new RankingPanel());
		tabbedMainPane.add("Tietokanta", new DatabasePanel());
		tabbedMainPane.add("Tuo tuloksia", new ImportPanel());

		mainFrame.add(tabbedMainPane);
		mainFrame.getContentPane().add(tabbedMainPane, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				WinMSSDatabaseUtil.closeConnection();
				HaurRankingDatabaseUtils.closeEntityManagerFactory();
				System.exit(0);
			}
		});

	}

	public void showHaurRankingGui() {
		DatabasePanelDataService.init();
		DataEventService.init();
		RankingPanelDataService.init();
		mainFrame.setVisible(true);
	}

}
