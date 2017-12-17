package haur_ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.IPSCDivision;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;
import haur_ranking.service.StageScoreSheetService;

public class StageScoreSheetServiceTests {

	@AfterClass
	public static void cleanup() {
		TestUtils.cleanup();
	}

	@BeforeClass
	public static void init() {
		TestUtils.setupDatabase();
	}

	@Test
	public void scoreSheetSaveMethodsTest() {

		try {
			int jarnoTotalResultsCount = StageScoreSheetService.find("Jarno", "Virta", IPSCDivision.PRODUCTION).size();
			assertEquals("Jarno should have 9 results for Production Division.", 9, jarnoTotalResultsCount);

			EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();

			// Check that competitor Jarno has only 8 results. Score sheet for
			// Spring match (1.4.2017) should have been
			// removed.
			int robScoreSheetsForValidClassifiersCount = StageScoreSheetService
					.findCompetitorScoreSheetsForValidClassifiers("Rob", "Leatham", IPSCDivision.PRODUCTION).size();

			entityManager.close();
			assertEquals("Rob should have results for 3 valid classifiers (ie. with 2 or more results each).", 3,
					robScoreSheetsForValidClassifiersCount);

			int jerryTotalResultsCount = StageScoreSheetService.find("Jerry", "Miculek", IPSCDivision.PRODUCTION)
					.size();
			assertEquals("Jerry should have 6 results for Production Division.", 6, jerryTotalResultsCount);

			int benTotalResultsCount = StageScoreSheetService.find("Ben", "Stoeger", IPSCDivision.PRODUCTION).size();
			assertEquals("Ben should have 6 results for Production Division.", 6, benTotalResultsCount);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during test");
		}

	}

}
