package haur_ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
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

	private void executeCompetitorTotalResultCountTest(String firstName, String lastName, int expectedCount,
			EntityManager entityManager) {
		// int totalCount = 0;
		// for (ClassifierStage stage : getClassifierStages()) {
		// totalCount +=
		// StageScoreSheetService.findClassifierStageResultsForCompetitor(firstName,
		// lastName,
		// IPSCDivision.PRODUCTION, stage, entityManager).size();
		// }
		// assertEquals("Competitor " + firstName + " " + lastName + " should
		// have " + expectedCount + " result(s) ",
		// expectedCount, totalCount);

	}

	private Set<ClassifierStage> getClassifierStages() {
		Set<ClassifierStage> classifiers = new HashSet<ClassifierStage>();
		classifiers.add(ClassifierStage.CLC01);
		classifiers.add(ClassifierStage.CLC02);
		classifiers.add(ClassifierStage.CLC03);
		classifiers.add(ClassifierStage.CLC04);
		classifiers.add(ClassifierStage.CLC05);
		classifiers.add(ClassifierStage.CLC06);
		classifiers.add(ClassifierStage.CLC07);
		classifiers.add(ClassifierStage.CLC08);
		classifiers.add(ClassifierStage.CLC09);
		return classifiers;
	}

	@Test
	public void scoreSheetSaveMethodsTest() {
		// Competitor Jarno should have 8 results. Has 11 score sheets in total
		// in inserted results. Older score sheets for same classifier should be
		// removed (CLC-05). Inserted data has results for Jarno for 9 valid
		// classifiers. Oldest result (CLC-04 in match "Spring match" should be
		// removed.
		try {

			int jarnoTotalResultsCount = StageScoreSheetService.find("Jarno", "Virta", IPSCDivision.PRODUCTION).size();
			assertEquals("Jarno should have 9 results for Production Division.", 9, jarnoTotalResultsCount);

			EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();

			// Check that competitor Jarno has only 8 results. Score sheet for
			// Spring match (1.4.2017) should have been
			// removed.
			List<StageScoreSheet> jarnoScoreSheetsForValidClassifiers = StageScoreSheetService
					.findCompetitorScoreSheetsForValidClassifiers("Jarno", "Virta", IPSCDivision.PRODUCTION,
							entityManager);
			int jarnoScoreSheetCountForValidClassifiers = jarnoScoreSheetsForValidClassifiers.size();
			entityManager.close();
			assertEquals("Jarno should have results for 8 valid classifiers (ie. with 2 or more results each).", 8,
					jarnoScoreSheetCountForValidClassifiers);

			boolean includesCLC04Result = false;
			for (StageScoreSheet sheet : jarnoScoreSheetsForValidClassifiers) {
				if (sheet.getStage().getClassifierStage().equals(ClassifierStage.CLC04))
					includesCLC04Result = true;
			}
			assertTrue("Jarno's result for CLC-04 should have "
					+ "been removed as oldest, because he has too many results.", includesCLC04Result == false);
			int robTotalResultsCount = StageScoreSheetService.find("Rob", "Leatham", IPSCDivision.PRODUCTION).size();
			assertEquals("Rob should have 3 results for Production Division.", 3, robTotalResultsCount);

			int jerryTotalResultsCount = StageScoreSheetService.find("Jerry", "Miculek", IPSCDivision.PRODUCTION)
					.size();
			assertEquals("Jerry should have 6 results for Production Division.", 6, jerryTotalResultsCount);

			int benTotalResultsCount = StageScoreSheetService.find("Ben", "Stoeger", IPSCDivision.PRODUCTION).size();
			assertEquals("Ben should have 5 results for Production Division.", 5, benTotalResultsCount);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during test");
		}

	}

}
