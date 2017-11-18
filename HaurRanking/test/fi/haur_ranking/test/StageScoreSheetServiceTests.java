package fi.haur_ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.service.StageScoreSheetService;

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
	public void totalResultsInDatabaseForCompetitorAndDivisionTest() {

		try {
			EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
			int jarnoTotalResultsCount = StageScoreSheetService
					.findDivisionForCompetitor("Jarno", "Virta", IPSCDivision.PRODUCTION, entityManager).size();
			assertEquals("Jarno should have 8 results for Production Division.", 8, jarnoTotalResultsCount);

			int clintTotalResultsCount = StageScoreSheetService
					.findDivisionForCompetitor("Clint", "Upchurch", IPSCDivision.PRODUCTION, entityManager).size();
			assertEquals("Clint should have 3 results for Production Division.", 3, clintTotalResultsCount);

			int jerryTotalResultsCount = StageScoreSheetService
					.findDivisionForCompetitor("Jerry", "Miculek", IPSCDivision.PRODUCTION, entityManager).size();
			assertEquals("Jerry should have 8 results for Production Division.", 8, jerryTotalResultsCount);

			int maxTotalResultsCount = StageScoreSheetService
					.findDivisionForCompetitor("Max", "Michel", IPSCDivision.PRODUCTION, entityManager).size();
			assertEquals("Max should have 5 results for Production Division.", 5, maxTotalResultsCount);

			int benTotalResultsCount = StageScoreSheetService
					.findDivisionForCompetitor("Ben", "Stoeger", IPSCDivision.PRODUCTION, entityManager).size();
			assertEquals("Ben should have  results for Production Division.", 6, benTotalResultsCount);

			entityManager.close();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during test");
		}

	}

}
