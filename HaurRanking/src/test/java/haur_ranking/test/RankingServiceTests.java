package haur_ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingRow;
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
	public void setImprovedRankingResultsTest() {
		Ranking ranking = TestUtils.getTestRanking();
		Ranking compareToRanking = TestUtils.getCompareToRanking();
		System.out.println("Calling setImprovedResults");
		RankingService.setImprovedRankingResults(ranking, compareToRanking);

		DivisionRankingRow firstRow = ranking.getDivisionRankings().get(0).getDivisionRankingRows().get(0);
		DivisionRankingRow secondRow = ranking.getDivisionRankings().get(0).getDivisionRankingRows().get(1);
		DivisionRankingRow thirdRow = ranking.getDivisionRankings().get(0).getDivisionRankingRows().get(2);
		DivisionRankingRow fourthRow = ranking.getDivisionRankings().get(0).getDivisionRankingRows().get(3);
		DivisionRankingRow fifthRow = ranking.getDivisionRankings().get(0).getDivisionRankingRows().get(4);
		// Jarno went from 2nd to 1st
		assertTrue("Jarno should have improvedResult set to 'true'", firstRow.isImprovedResult());
		// Jerry went from 1st to second
		assertFalse("Jerry should have improvedResult set to 'false'", secondRow.isImprovedResult());
		// Ben stayed 3rd
		assertFalse("Ben should have improvedResult set to 'false'", thirdRow.isImprovedResult());
		// Clint does not have a ranking position
		assertFalse("Clint should have improvedResult set to 'false'", fourthRow.isImprovedResult());
		// Rob went from 4th to 5th.
		assertFalse("Rob should have improvedResult set to 'false'", fifthRow.isImprovedResult());
	}

	@Test
	public void generateRankingTest() {
		Ranking ranking = RankingService.generateRanking();
		DivisionRanking productionRanking = new DivisionRanking();
		DivisionRanking standardRanking = new DivisionRanking();
		for (DivisionRanking div : ranking.getDivisionRankings()) {
			if (div.getDivision().equals(IPSCDivision.PRODUCTION))
				productionRanking = div;
			if (div.getDivision().equals(IPSCDivision.STANDARD))
				standardRanking = div;

		}
		DivisionRankingRow firstLine = productionRanking.getDivisionRankingRows().get(0);
		DivisionRankingRow secondLine = productionRanking.getDivisionRankingRows().get(1);
		DivisionRankingRow thirdLine = productionRanking.getDivisionRankingRows().get(2);
		DivisionRankingRow fourthLine = productionRanking.getDivisionRankingRows().get(3);
		assertEquals("Jarno Virta should be #1 for Production Division ranking.", "Jarno",
				firstLine.getCompetitor().getFirstName());
		assertEquals("Jarno Virta should have bestResultsAverage of 1.25 for Production Division.", 1.253,
				firstLine.getBestResultsAverage(), 0.001);
		assertEquals("Jarno Virta should have result of 100%", 100.0, firstLine.getResultPercentage(), 0.1);
		assertEquals("Jerry Miculek should be #2 for Production Division ranking.", "Jerry",
				secondLine.getCompetitor().getFirstName());
		assertEquals("Jerry Miculek should have bestResultsAverage of 0.87 for Production Division.", 0.87204,
				secondLine.getBestResultsAverage(), 0.001);
		assertEquals("Jerry Miculek should have result of 70%", 69.6, secondLine.getResultPercentage(), 0.1);
		assertEquals("Ben Stoeger should be #3 for Production Division ranking.", "Ben",
				thirdLine.getCompetitor().getFirstName());
		assertEquals("Ben Stoeger should have bestResultsAverage of 0.82 for Production Division.", 0.818,
				thirdLine.getBestResultsAverage(), 0.001);
		assertEquals("Ben Stoeger should have result of 65%", 65.3, thirdLine.getResultPercentage(), 0.1);
		assertEquals("Rob Leatham should not have a rank (not enough results).", false,
				fourthLine.isRankedCompetitor());
		assertEquals("Standard division should have two results.", 2, standardRanking.getDivisionRankingRows().size());
	}

}
