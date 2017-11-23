package haur_ranking.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingLine;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Ranking;
import haur_ranking.service.RankingService;

public class RankingServiceTests {
	@AfterClass
	public static void cleanup() {
		TestUtils.cleanup();
	}

	@BeforeClass
	public static void init() {
		TestUtils.setupDatabase();
	}

	@Test
	public void calculateCompetitorRelativeScoresTest() {
		// try {
		// Method method =
		// RankingService.class.getDeclaredMethod("calculateCompetitorTopScoresAverage",
		// List.class,
		// Map.class);
		// method.setAccessible(true);
		//
		// // Test for competitor with required minimum of four score sheets
		// double jarnoTopScoresAverage = (double)
		// method.invoke(RankingService.class,
		// TestUtils.getAllStageScoreSheetsForCompetitor(new
		// Match("JarnoTestMatch", "1.1.2017"),
		// new Competitor("Jarno", "Virta")),
		// TestUtils.getAverageOfTopTwoForStage());
		// assertEquals("Top scores average for test competitor Jarno should be
		// 0.780", 0.780, jarnoTopScoresAverage,
		// 0.001);
		// // Test for competitor with three score sheets instead of the
		// // required four
		// double jerryTopScoresAverage = (double)
		// method.invoke(RankingService.class,
		// TestUtils.getAllStageScoreSheetsForCompetitor(new
		// Match("JerryTestMatch", "2.4.2017"), new Competitor("Jerry",
		// "Miculek")), TestUtils.getAverageOfTopTwoForStage());
		// assertEquals("calculateCompetitorRelativeScores method must
		// return -1
		// for competitor Jerry (not enough score sheets)", -1,
		// jerryTopScoresAverage, 0.0);
		// // Test for competitor with more than eight score sheets. Only
		// eight
		// should count.
		// double benTopScoresAverage = (double)
		// method.invoke(RankingService.class,
		// TestUtils.getAllStageScoreSheetsForCompetitor(new
		// Match("BenTestMatch", "5.5.2017"), new Competitor("Ben",
		// "Stoeger")),
		// TestUtils.getAverageOfTopTwoForStage());
		// assertEquals("Top scores average for competitor Ben must be 0.663
		// (competitor with more than 8 score sheets, only 8 count).",
		// 0.663, benTopScoresAverage, 0.001);

		// }catch(
		//
		// Exception e)
		// {
		// }
	}

	@Test
	public void convertAverageScoresToPercentageTest() {

		// List<Object[]> averageScoresList =
		// TestUtils.getCompetitorAverageScoreList();
		// try {
		// Method method =
		// RankingService.class.getDeclaredMethod("convertAverageScoresToPercentage",
		// List.class);
		// method.setAccessible(true);
		// method.invoke(RankingService.class, averageScoresList);
		// assertEquals(100, (int) averageScoresList.get(0)[1]);
		// assertEquals(92, (int) averageScoresList.get(1)[1]);
		// assertEquals(85, (int) averageScoresList.get(2)[1]);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@Test
	public void generateRankingTest() {
		Ranking ranking = RankingService.getRanking();
		DivisionRanking productionRanking = new DivisionRanking();
		for (DivisionRanking div : ranking.getDivisionRankings())
			if (div.getDivision().equals(IPSCDivision.PRODUCTION))
				productionRanking = div;
		DivisionRankingLine firstLine = productionRanking.getRankingLines().get(0);
		DivisionRankingLine secondLine = productionRanking.getRankingLines().get(1);
		DivisionRankingLine thirdLine = productionRanking.getRankingLines().get(2);
		DivisionRankingLine fourthLine = productionRanking.getRankingLines().get(3);
		assertEquals("Jarno Virta should be #1 for Production Division ranking.", "Jarno",
				firstLine.getCompetitor().getFirstName());
		assertEquals("Jarno Virta should have bestResultsAverage of 1.25 for Production Division.", 1.253,
				firstLine.getBestResultsAverage(), 0.001);
		assertEquals("Jarno Virta should have result of 100%", 100, firstLine.getResultPercentage());
		assertEquals("Jerry Miculek should be #2 for Production Division ranking.", "Jerry",
				secondLine.getCompetitor().getFirstName());
		assertEquals("Jerry Miculek should have bestResultsAverage of 0.87 for Production Division.", 0.87204,
				secondLine.getBestResultsAverage(), 0.001);
		assertEquals("Jerry Miculek should have result of 70%", 70, secondLine.getResultPercentage());
		assertEquals("Ben Stoeger should be #3 for Production Division ranking.", "Ben",
				thirdLine.getCompetitor().getFirstName());
		assertEquals("Ben Stoeger should have bestResultsAverage of 0.82 for Production Division.", 0.818,
				thirdLine.getBestResultsAverage(), 0.001);
		assertEquals("Ben Stoeger should have result of 65%", 65, thirdLine.getResultPercentage());
		assertEquals("Rob Leatham should not have a rank (not enough results).", false,
				fourthLine.isRankedCompetitor());
	}

	@Test
	public void sortResultListTest() {
		// List<Object[]> unorderedResultList =
		// TestUtils.getCompetitorAverageScoreList();
		// Object[] objToMove = unorderedResultList.get(0);
		// unorderedResultList.add(objToMove);
		// unorderedResultList.remove(0);
		//
		// try {
		// Method method =
		// RankingService.class.getDeclaredMethod("sortResultList", List.class);
		// method.setAccessible(true);
		// method.invoke(RankingService.class, unorderedResultList);
		// assertEquals("Jarno", ((Competitor)
		// unorderedResultList.get(0)[0]).getFirstName());
		// assertEquals("Max", ((Competitor)
		// unorderedResultList.get(1)[0]).getFirstName());
		// assertEquals("Ben", ((Competitor)
		// unorderedResultList.get(2)[0]).getFirstName());
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}
