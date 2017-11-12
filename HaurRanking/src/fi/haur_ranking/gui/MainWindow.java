package fi.haur_ranking.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import fi.haur_ranking.domain.DatabaseStatistics;
import fi.haur_ranking.gui.filters.WinMSSFileFilter;
import fi.haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;
import fi.haur_ranking.service.DatabaseStatisticsService;
import fi.haur_ranking.service.MatchService;
import fi.haur_ranking.service.RankingService;

public class MainWindow {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JPanel databaseStatisticsPanel;
	private JLabel databaseMatchCountPanel;
	private JLabel databaseStageAndCompetitorCountPanel;
	
	private JPanel controlPanel;
	
	private String lastMSSDbFileLocation = null;

	public void prepareGUI() {
		
		initializeFontSize();
		mainFrame = new JFrame("HAUR varjoranking");
		mainFrame.setSize(800, 600);
		mainFrame.setLayout(new GridLayout(3, 1));
		List<Image> icons = new ArrayList<Image>();
		try {
		icons.add(ImageIO.read(new File("images/small_haur_logo.png")));
		icons.add(ImageIO.read(new File("images/large_haur_logo.png")));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.setIconImages(icons);
		
		headerLabel = new JLabel("",JLabel.CENTER );
		
		databaseStatisticsPanel = new JPanel(new GridLayout(2, 1));
		databaseStatisticsPanel.setSize(350, 20);		

		databaseMatchCountPanel = new JLabel("",JLabel.LEFT);        
		databaseMatchCountPanel.setSize(300, 10);
		databaseStageAndCompetitorCountPanel = new JLabel("",JLabel.LEFT);        
		databaseStageAndCompetitorCountPanel.setSize(300, 10);
		
		databaseStatisticsPanel.add(databaseMatchCountPanel);
		databaseStatisticsPanel.add(databaseStageAndCompetitorCountPanel);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
			WinMssDatabaseUtil.closeConnecion();
			System.exit(0);
			}        
		});    
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(databaseStatisticsPanel);
		
	}
	public void showHaurRankingGui() {
		updateDatabaseStaticsPanel();
		JButton importCompetitions = new JButton("Tuo kilpailuja");
		importCompetitions.setActionCommand("importCompetitions");
		importCompetitions.addActionListener(new ButtonClickListener()); 
		controlPanel.add(importCompetitions);
		importCompetitions = new JButton("Luo ranking");
		importCompetitions.setActionCommand("generateRanking");
		importCompetitions.addActionListener(new ButtonClickListener()); 
		controlPanel.add(importCompetitions);
		mainFrame.setVisible(true);		 
	}

	private void updateDatabaseStaticsPanel() {
		DatabaseStatistics databaseStatistics = DatabaseStatisticsService.getDatabaseStatistics();
		databaseMatchCountPanel.setText("Kilpailuja : " + databaseStatistics.getMatchCount());
		databaseStageAndCompetitorCountPanel.setText("Asematuloksia : " + databaseStatistics.getStageCount() 
			+ " (" + databaseStatistics.getCompetitorCount() + " kilpailijaa)");
	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();  
			if (command.equals("importCompetitions")) {
				importCompetitionsCommandHandler();
			}
			if (command.equals("generateRanking")) {
				RankingService.generateRanking();
			}
		}
	}
	private void initializeFontSize() {
        float multiplier = 1.8f;
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> e = defaults.keys();
        for (int i = 0; e.hasMoreElements(); i++) {
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
	}
	private void importCompetitionsCommandHandler() {
		JFileChooser fileChooser;
		if (lastMSSDbFileLocation != null) fileChooser = new JFileChooser(lastMSSDbFileLocation);
		else fileChooser = new JFileChooser();
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
}
	
