package fi.haur_ranking.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;

// LUO LAPPUJA MUIHIN DIVISIOONIIN

public class TestUtils {

	static List<Match> testMatches = null;
		
	static ClassifierStage[] classifierStages = new ClassifierStage[] { 
			ClassifierStage.CLC01,ClassifierStage.CLC02, ClassifierStage.CLC03, ClassifierStage.CLC04, ClassifierStage.CLC05,
			ClassifierStage.CLC06, ClassifierStage.CLC07, ClassifierStage.CLC08, ClassifierStage.CLC09	};
	static Competitor jarno = new Competitor("Jarno", "Virta");
	static Competitor jerry = new Competitor("Jerry", "Miculek");
	static Competitor ben = new Competitor("Ben", "Stoeger");
	static Competitor max = new Competitor("Max", "Michel");
	static Competitor clint= new Competitor("Clint", "Upchurch");
	
	static Double[] jarnoNewMatchHitFactors = new Double[] {
			4.0, 3.9, 2.0, 5.1, 5.5, 4.0, 4.5, 3.6, 5.2 };
	
	static Double[] jerryNewMatchHitFactors = new Double[] {
			3.9, 4.5, 5.1, 3.9, 4.4, 2.3, 3.9, null, null };
	
	static Double[] maxNewMatchHitFactors = new Double[] {
			 2.3, null, 0.0, 3.9, 4.5, null, null, null, null};
	
	static Double[] benNewMatchHitFactors = new Double[] {
			 3.2, 4.1, 2.9, 0.0, 4.8, 5.1, null, null, null};
	
	static Double[] clintNewMatchHitFactors = new Double[] {
			 3.2, 4.0, 3.6, null, null, null, null, null, null};
	
	static Double[] jarnoOldMatchHitFactors = new Double[] {
			null, null, null, null, 6.0, null, null, null, null};
	
	static Double[] jerryOldMatchHitFactors = new Double[] {
			null, null, null, 4.2, null, null, null, null, null};
	
	static Double[] maxOldMatchHitFactors = new Double[] {
			null, 3.1, null, null, null, null, null, null, null};
	
		
	
	protected static List<Match> createTestMatches() {
		List<Match> matches = new ArrayList<Match>();
		Match newTestMatch = new Match();
		newTestMatch.setName("New match");
		newTestMatch.setWinMssDateString("18.11.2017");
		newTestMatch.setStages(createTestStages(newTestMatch));
		matches.add(newTestMatch);
		
		Match oldTestMatch = new Match();
		oldTestMatch.setName("Old match");
		oldTestMatch.setWinMssDateString("12.11.2017");
		oldTestMatch.setStages(createTestStages(oldTestMatch));
		
		matches.add(oldTestMatch);
		
		return matches;
	}
	
	protected static List<Stage> createTestStages(Match match) {
		List<Stage> testStages = new ArrayList<Stage>();
		
		for (ClassifierStage classifier : classifierStages) {
			testStages.add(createStage(match, classifier));
		}
		return testStages;
	}
	private static Stage createStage(Match match, ClassifierStage classifier) {
		Stage stage = new Stage(match, classifier.toString(), classifier);
		stage.setStageScoreSheets(createStageScoreSheets(stage));
		
		return stage;
	}
	
	public static List<StageScoreSheet> createStageScoreSheets(Stage stage) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		int index = 0;
		for (ClassifierStage classifier : ClassifierStage.values()) {
			if (classifier.equals(stage.getClassifierStage())) break;
			index++;
		}
		if (stage.getMatch().getName().equals("New match")) {
				if (jarnoNewMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(jarno, jarnoNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
				if (jerryNewMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(jerry, jerryNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
				if (benNewMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(ben, benNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
				if (maxNewMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(max, maxNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
				if (clintNewMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(clint, clintNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
		
		}
		else {
			if (jarnoOldMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(jarno, jarnoOldMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (jerryOldMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(jerry, jerryOldMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (maxOldMatchHitFactors[index] != null) sheets.add(new StageScoreSheet(max, maxOldMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
		}
		return sheets;
	}
		
	public static Map<ClassifierStage, Double> getAverageOfTopTwoForStage() {
		Map<ClassifierStage, Double> averages = new HashMap<ClassifierStage, Double>();
		averages.put(ClassifierStage.CLC01, 6.15);
		averages.put(ClassifierStage.CLC02, 6.15);
		
		return averages;
	}
	protected static List<StageScoreSheet> getAllStageScoreSheetsForCompetitor(Match match, Competitor competitor) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		sheets.addAll(getSheetsForCompetitorAndClassifier(match, competitor, ClassifierStage.CLC01));
		sheets.addAll(getSheetsForCompetitorAndClassifier(match, competitor, ClassifierStage.CLC02));
		return sheets;
	}
	protected static List<StageScoreSheet> getSheetsForCompetitorAndClassifier(Match match, Competitor competitor, ClassifierStage classifierStage) {
		if (competitor.getFirstName().equals("Jarno")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(match, jarno, 
						new double[] { 4.0, 3.9 }, ClassifierStage.CLC01);
			}
			else 
				return createScoreSheetList(match, jarno,
						new double[] { 6.2, 5.1 }, ClassifierStage.CLC02);
		}
		if (competitor.getFirstName().equals("Jerry")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(match, jerry, 
						new double[] { 3.2, 0.0 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(match, jerry, 
					new double[] { 6.1 }, ClassifierStage.CLC02);
		}
		if (competitor.getFirstName().equals("Max")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(match, max, 
						new double[] { 2.0, 3.1, 6.8 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(match, max, 
					new double[] { 4.1, 3.6 }, ClassifierStage.CLC02);
			}
		else {
			if (classifierStage == ClassifierStage.CLC01) {
					return createScoreSheetList(match, ben, 
							new double[] { 4.1, 3.2,5.5, 2.3, 0.15 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(match, ben, 
					new double[] { 3.3, 2.1, 1.1, 3.4 }, ClassifierStage.CLC02);
		}
		
	}
	
	protected static List<StageScoreSheet> createScoreSheetList(Match match, Competitor competitor, double[] hitFactors, ClassifierStage classifier) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		for (double hf : hitFactors) sheets.add(new StageScoreSheet(competitor, hf, new Stage(match, classifier.toString(), classifier), IPSCDivision.PRODUCTION));
		
		return sheets;
	}
	protected static List<Object[]> getCompetitorAverageScoreList() {
		List<Object[]> averageScoreList = new ArrayList<Object[]>();
		averageScoreList.add(new Object[] { jarno, 0.78049 });
		averageScoreList.add(new Object[] { max, 0.72045 });
		averageScoreList.add(new Object[] { ben, 0.66260 });
		return averageScoreList;
	}
	
	
	
	
}
