package fi.haur_ranking.test;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.service.RankingService;

public class RankingServiceTests {
	private Stage CLC01;
	private Stage CLC02;
	private Competitor jarnoVirta;
	private Competitor jerryMiculek;
	private Competitor maxMichel;
	private Competitor benStoeger;
	
	Map<ClassifierStage, Double> classifierStageTopResultAverages;
	private List<StageScoreSheet> jarnoVirtaLatestScoreSheets;
	private List<StageScoreSheet> jerryMiculekLatestScoreSheets;
	private List<StageScoreSheet> maxMichelLatestScoreSheets;
	private List<StageScoreSheet> benStoegerLatestScoreSheets;
	
	public RankingServiceTests() {
		
		CLC01 = new Stage("CLC01", ClassifierStage.CLC01);
		CLC02 = new Stage("CLC02", ClassifierStage.CLC02);
		
		jarnoVirta = new Competitor("Jarno", "Virta");
		jerryMiculek = new Competitor("Jerry", "Miculek");
		maxMichel = new Competitor("JC", "Tran");
		benStoeger = new Competitor("Ben", "Stoeger");
		
		double[] jarnoVirtaCLC01HfList = { 4.0, 3.9 };
		double[] jarnoVirtaCLC02HfList = { 6.2, 5.1 };
		
		double[] jerryMiculekCLC01HfList = { 3.2, 0.0 };
		double[] jerryMiculekCLC02HfList = { 6.1 };
		
		double[] jcTranCLC01HfList = { 2.0, 3.1, 6.8 };
		double[] jcTranCLC02HfList = { 4.1, 3.6 };
		 
		double[] benStoegerCLC01HfList = { 4.1, 3.2,5.5, 2.3, 0.1521 };
		double[] benStoegerCLC02HfList = { 3.3, 2.1, 1.1, 3.4 };
		
		jarnoVirtaLatestScoreSheets = getScoreSheetList(jarnoVirtaCLC01HfList, jarnoVirtaCLC02HfList);
		jerryMiculekLatestScoreSheets = getScoreSheetList(jerryMiculekCLC01HfList, jerryMiculekCLC02HfList);
		maxMichelLatestScoreSheets = getScoreSheetList(jcTranCLC01HfList, jcTranCLC01HfList);
		benStoegerLatestScoreSheets = getScoreSheetList(benStoegerCLC01HfList, benStoegerCLC02HfList);
		
		classifierStageTopResultAverages = new HashMap<ClassifierStage, Double>();
		classifierStageTopResultAverages.put(CLC01.getClassifierStage(), 6.15);
		classifierStageTopResultAverages.put(CLC02.getClassifierStage(), 6.15);
	}
	
	@Test
	public void calculateCompetitorRelativeScoresTest() {
		try {
			Method method = RankingService.class.getDeclaredMethod("calculateCompetitorTopScoresAverage", List.class, Map.class);
			method.setAccessible(true);
			
			// Test for competitor with required minimum of four score sheets
			double jarnoTopScoresAverage = (double) method.invoke(RankingService.class, jarnoVirtaLatestScoreSheets, classifierStageTopResultAverages);
			assertEquals("Top scores average for test competitor Jarno must be 0.780",  0.780, jarnoTopScoresAverage, 0.001);
			// Test for competitor with three score sheets instead of the required four
			double jerryTopScoresAverage = (double) method.invoke(RankingService.class, jerryMiculekLatestScoreSheets, classifierStageTopResultAverages);
			assertEquals("calculateCompetitorRelativeScores method must return -1 for competitor Jerry (not enough score sheets)",  -1, jerryTopScoresAverage, 0.0);
			// Test for competitor with more than eight score sheets. Only eight should count.
			double benTopScoresAverage = (double) method.invoke(RankingService.class, benStoegerLatestScoreSheets, classifierStageTopResultAverages);
			assertEquals("Top scores average for competitor Ben must be 0.663 (competitor with more than 8 score sheets, only 8 count).",  
					0.663, benTopScoresAverage, 0.001);
			
		}
		catch (Exception e) {
			
		}
	}
	
	@Test
	public void sortResultListTest() {
		List<Object[]> unorderedResultList = getCompetitorAverageScoreList();
		Object[] objToMove = unorderedResultList.get(0);
		unorderedResultList.add(objToMove);
		unorderedResultList.remove(0);
		
		try {
			Method method = RankingService.class.getDeclaredMethod("sortResultList", List.class);
			method.setAccessible(true);
			method.invoke(RankingService.class, unorderedResultList);
			assertEquals("Jarno", ((Competitor) unorderedResultList.get(0)[0]).getFirstName());
			assertEquals("JC", ((Competitor) unorderedResultList.get(1)[0]).getFirstName());
			assertEquals("Ben", ((Competitor) unorderedResultList.get(2)[0]).getFirstName());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void convertAverageScoresToPercentageTest() {
		// 
		List<Object[]> averageScoresList = getCompetitorAverageScoreList();
		try {
			Method method = RankingService.class.getDeclaredMethod("convertAverageScoresToPercentage", List.class);
			method.setAccessible(true);
			method.invoke(RankingService.class, averageScoresList);
			assertEquals(100, (int) averageScoresList.get(0)[1]);
			assertEquals(92, (int) averageScoresList.get(1)[1]);
			assertEquals(85, (int) averageScoresList.get(2)[1]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<StageScoreSheet> getScoreSheetList(double[] CLC01HitFactors, double[] CLC02HitFactors) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		for (double hf : CLC01HitFactors) sheets.add(new StageScoreSheet(hf, CLC01));
		for (double hf : CLC02HitFactors) sheets.add(new StageScoreSheet(hf, CLC02));
		return sheets;
	}
	
	private List<Object[]> getCompetitorAverageScoreList() {
		List<Object[]> averageScoreList = new ArrayList<Object[]>();
		averageScoreList.add(new Object[] { jarnoVirta, 0.78049 });
		averageScoreList.add(new Object[] { maxMichel, 0.72045 });
		averageScoreList.add(new Object[] { benStoeger, 0.66260 });
		return averageScoreList;
	}
}
