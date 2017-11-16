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

public class TestUtils {

	static Competitor jarno;
	static Competitor jerry;
	static Competitor ben;
	static Competitor max;
	static
		{
			jarno = new Competitor("Jarno", "Virta");
			jerry = new Competitor("Jerry", "Miculek");
			ben = new Competitor("Ben", "Stoeger");
			max = new Competitor("Max", "Michel");
		}
	protected static Match createTestMatch() {
		jarno = new Competitor("Jarno", "Virta");
		jerry = new Competitor("Jerry", "Miculek");
		ben = new Competitor("Ben", "Stoeger");
		max = new Competitor("Max", "Michel");
		
		Match testMatch = new Match();
		testMatch.setName("Test Match with CLC01 and CLC02");
		testMatch.setStages(createTestStages(testMatch));
		
		return testMatch;
	}
	
	protected static List<Stage> createTestStages(Match match) {
		List<Stage> testStages = new ArrayList<Stage>();
		Stage CLC01 = new Stage(match, "CLC01", ClassifierStage.CLC01);
		Stage CLC02 = new Stage(match, "CLC02", ClassifierStage.CLC02);
		List<StageScoreSheet> sheetList = new ArrayList<StageScoreSheet>();
		
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, jarno, ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, jerry, ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, max, ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, ben, ClassifierStage.CLC01));
		CLC01.setStageScoreSheets(sheetList);
		testStages.add(CLC01);
		
		sheetList = new ArrayList<StageScoreSheet>();
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, jarno, ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, jerry, ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, max, ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier(match, ben, ClassifierStage.CLC02));
		CLC02.setStageScoreSheets(sheetList);
		testStages.add(CLC02);
		
		return testStages;
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
