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


//PRODUCTION DIVISION
//STAGE CLC01 HF: 					REL. SCORES
//	Jarno 	4.0, 3.9				0.65041, 0.63414
//	Jerry 	3.2, 0.0				0.52033, 0	
//	JC 		2.0, 3.1, 6.8			0.32520, 0.524065, 1,10569
//	Ben 	4.1, 3.2, 5.5, 2.3		0.66667, 0.520325, 0.89430, 0.37398
//
//	TOP 2 hf: 	6.8, 5.5 - AVG: 6.15
//===============	
//STAGE CLC02 HF:					REL. SCORES					
//	Jarno 	6.2, 5.1				1.008130, 0.82927
//	Jerry 	6.1						0.991870
//	JC 		4.1, 3.6				0.666667, 0.585366
//	Ben 	3.3, 2.1, 1.1, 3.4		0.536585, 0.341463, 0.1788612, 0.552846
//	
//	TOP 2 hf: 	6.2, 6.1 - AVG: 6.15
//===============
//
//RESULTS:													AVG 
//	Jarno	0.65041, 	0.63414, 	1.008130, 	0.82927		0.78049
//	Jerry	- (less than 4 results)							-
//	JC		1,10569, 	0.666667, 	0.585366, 	0.524065	0.72045
//	Ben		0.89430, 	0.66667, 	0.552846, 	0.536585	0.66260
//
//RANKING:
//	1. Jarno	100%
//	2. JC		92%
//	3. Ben		84%



public class RankingServiceTests {
	private Stage CLC01;
	private Stage CLC02;
	private Competitor jarnoVirta;
	private Competitor jerryMiculek;
	private Competitor jcTran;
	private Competitor benStoeger;
	
	Map<ClassifierStage, Double> classifierStageTopResultAverages;
	private List<StageScoreSheet> jarnoVirtaLatestScoreSheets;
	private List<StageScoreSheet> jerryMiculekLatestScoreSheets;
	private List<StageScoreSheet> jcTranLatestScoreSheets;
	private List<StageScoreSheet> benStoegerLatestScoreSheets;
	
	public RankingServiceTests() {
		
		CLC01 = new Stage("CLC01", ClassifierStage.CLC01);
		CLC02 = new Stage("CLC02", ClassifierStage.CLC02);
		
		jarnoVirta = new Competitor("Jarno", "Virta");
		jerryMiculek = new Competitor("Jerry", "Miculek");
		jcTran = new Competitor("JC", "Tran");
		benStoeger = new Competitor("Ben", "Stoeger");
		
		double[] jarnoVirtaCLC01HfList = { 4.0, 3.9 };
		double[] jarnoVirtaCLC02HfList = { 6.2, 5.1 };
		
		double[] jerryMiculekCLC01HfList = { 3.2, 0.0 };
		double[] jerryMiculekCLC02HfList = { 6.1 };
		
		double[] jcTranCLC01HfList = { 2.0, 3.1, 6.8 };
		double[] jcTranCLC02HfList = { 4.1, 3.6 };
		 
		double[] benStoegerCLC01HfList = { 4.1, 3.2,5.5, 2.3 };
		double[] benStoegerCLC02HfList = { 3.3, 2.1, 1.1, 3.4 };
		
		jarnoVirtaLatestScoreSheets = getScoreSheetList(jarnoVirtaCLC01HfList, jarnoVirtaCLC02HfList);
		jerryMiculekLatestScoreSheets = getScoreSheetList(jerryMiculekCLC01HfList, jerryMiculekCLC02HfList);
		jcTranLatestScoreSheets = getScoreSheetList(jcTranCLC01HfList, jcTranCLC01HfList);
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
			double jarnoTopScoresAverage = (double) method.invoke(RankingService.class, jarnoVirtaLatestScoreSheets, classifierStageTopResultAverages);
			assertEquals("Top scores average for test competitor Jarno must be 0.780",  0.780, jarnoTopScoresAverage, 0.001);
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
		averageScoreList.add(new Object[] { jcTran, 0.72045 });
		averageScoreList.add(new Object[] { benStoeger, 0.66260 });
		return averageScoreList;
	}
}
