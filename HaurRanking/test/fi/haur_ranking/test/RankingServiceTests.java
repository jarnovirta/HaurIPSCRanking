package fi.haur_ranking.test;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Test;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.service.RankingService;

public class RankingServiceTests {
	public RankingServiceTests() {


	}
	
	@Test
	public void calculateCompetitorRelativeScoresTest() {
		try {
			Method method = RankingService.class.getDeclaredMethod("calculateCompetitorTopScoresAverage", List.class, Map.class);
			method.setAccessible(true);
			
			// Test for competitor with required minimum of four score sheets
			double jarnoTopScoresAverage = (double) method.invoke(RankingService.class, 
					TestUtils.getAllStageScoreSheetsForCompetitor("Jarno"), TestUtils.getAverageOfTopTwoForStage());
			assertEquals("Top scores average for test competitor Jarno must be 0.780",  0.780, jarnoTopScoresAverage, 0.001);
			// Test for competitor with three score sheets instead of the required four
			double jerryTopScoresAverage = (double) method.invoke(RankingService.class, 
					TestUtils.getAllStageScoreSheetsForCompetitor("Jerry"), TestUtils.getAverageOfTopTwoForStage());
			assertEquals("calculateCompetitorRelativeScores method must return -1 for competitor Jerry (not enough score sheets)",  -1, jerryTopScoresAverage, 0.0);
			// Test for competitor with more than eight score sheets. Only eight should count.
			double benTopScoresAverage = (double) method.invoke(RankingService.class, 
					TestUtils.getAllStageScoreSheetsForCompetitor("Ben"), TestUtils.getAverageOfTopTwoForStage());
			assertEquals("Top scores average for competitor Ben must be 0.663 (competitor with more than 8 score sheets, only 8 count).",  
					0.663, benTopScoresAverage, 0.001);
			
		}
		catch (Exception e) {
		}
	}
	
	@Test
	public void sortResultListTest() {
		List<Object[]> unorderedResultList = TestUtils.getCompetitorAverageScoreList();
		Object[] objToMove = unorderedResultList.get(0);
		unorderedResultList.add(objToMove);
		unorderedResultList.remove(0);
		
		try {
			Method method = RankingService.class.getDeclaredMethod("sortResultList", List.class);
			method.setAccessible(true);
			method.invoke(RankingService.class, unorderedResultList);
			assertEquals("Jarno", ((Competitor) unorderedResultList.get(0)[0]).getFirstName());
			assertEquals("Max", ((Competitor) unorderedResultList.get(1)[0]).getFirstName());
			assertEquals("Ben", ((Competitor) unorderedResultList.get(2)[0]).getFirstName());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void convertAverageScoresToPercentageTest() {
		// 
		List<Object[]> averageScoresList = TestUtils.getCompetitorAverageScoreList();
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
}
