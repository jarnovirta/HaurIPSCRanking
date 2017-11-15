package fi.haur_ranking.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;

public class TestUtils {

	protected static Match createTestMatch() {
		Match testMatchCLC01 = new Match();
		testMatchCLC01.setName("Test Match with CLC01 and CLC02");
		testMatchCLC01.setStages(createTestStages());
		return testMatchCLC01;
	}
	
	protected static List<Stage> createTestStages() {
		List<Stage> testStages = new ArrayList<Stage>();
		Stage CLC01 = new Stage("CLC01", ClassifierStage.CLC01);
		Stage CLC02 = new Stage("CLC02", ClassifierStage.CLC02);
		List<StageScoreSheet> sheetList = new ArrayList<StageScoreSheet>();
		
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Jarno", ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Jerry", ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Max", ClassifierStage.CLC01));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Ben", ClassifierStage.CLC01));
		CLC01.setStageScoreSheets(sheetList);
		testStages.add(CLC01);
		
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Jarno", ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Jerry", ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Max", ClassifierStage.CLC02));
		sheetList.addAll(getSheetsForCompetitorAndClassifier("Ben", ClassifierStage.CLC02));
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
	protected static List<StageScoreSheet> getAllStageScoreSheetsForCompetitor(String firstName) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		sheets.addAll(getSheetsForCompetitorAndClassifier(firstName, ClassifierStage.CLC01));
		sheets.addAll(getSheetsForCompetitorAndClassifier(firstName, ClassifierStage.CLC02));
		return sheets;
	}
	protected static List<StageScoreSheet> getSheetsForCompetitorAndClassifier(String firstName, ClassifierStage classifierStage) {
		if (firstName.equals("Jarno")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(new Competitor("Jarno", "Virta"), 
						new double[] { 4.0, 3.9 }, ClassifierStage.CLC01);
			}
			else 
				return createScoreSheetList(new Competitor("Jarno", "Virta"),
						new double[] { 6.2, 5.1 }, ClassifierStage.CLC02);
		}
		if (firstName.equals("Jerry")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(new Competitor("Jerry", "Miculek"), 
						new double[] { 3.2, 0.0 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(new Competitor("Jerry", "Miculek"), 
					new double[] { 6.1 }, ClassifierStage.CLC02);
		}
		if (firstName.equals("Max")) {
			if (classifierStage == ClassifierStage.CLC01) {
				return createScoreSheetList(new Competitor("Max", "Michel"), 
						new double[] { 2.0, 3.1, 6.8 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(new Competitor("Max", "Michel"), 
					new double[] { 4.1, 3.6 }, ClassifierStage.CLC02);
			}
		else {
			if (classifierStage == ClassifierStage.CLC01) {
					return createScoreSheetList(new Competitor("Ben", "Stoeger"), 
							new double[] { 4.1, 3.2,5.5, 2.3, 0.15 }, ClassifierStage.CLC01);
			}
			else return createScoreSheetList(new Competitor("Ben", "Stoeger"), 
					new double[] { 3.3, 2.1, 1.1, 3.4 }, ClassifierStage.CLC02);
		}
		
	}
	
	protected static List<StageScoreSheet> createScoreSheetList(Competitor competitor, double[] hitFactors, ClassifierStage classifier) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		for (double hf : hitFactors) sheets.add(new StageScoreSheet(competitor, hf, new Stage(classifier.toString(), classifier)));
		
		return sheets;
	}
	protected static List<Object[]> getCompetitorAverageScoreList() {
		List<Object[]> averageScoreList = new ArrayList<Object[]>();
		averageScoreList.add(new Object[] { new Competitor("Jarno", "Virta"), 0.78049 });
		averageScoreList.add(new Object[] { new Competitor("Max", "Michel"), 0.72045 });
		averageScoreList.add(new Object[] { new Competitor("Ben", "Stoeger"), 0.66260 });
		return averageScoreList;
	}
}
