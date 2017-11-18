package fi.haur_ranking.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.haur_ranking.domain.ClassifierStage;

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

	@Test
	public void findClassifierStageResultsForCompetitorTest() {

		// try {
		// EntityManager entityManager =
		// HaurRankingDatabaseUtils.createEntityManager();
		//
		// int jarnoCLC01ResultCount =
		// StageScoreSheetService.findClassifierStageResultsForCompetitor("Jarno",
		// "Virta",
		// IPSCDivision.PRODUCTION, ClassifierStage.CLC01,
		// entityManager).size();
		// executeCompetitorTotalResultCountTest("Jarno", "Virta", 9,
		// entityManager);
		// executeCompetitorTotalResultCountTest("Jerry", "Miculek", 8,
		// entityManager);
		// executeCompetitorTotalResultCountTest("Max", "Michel", 5,
		// entityManager);
		// executeCompetitorTotalResultCountTest("Ben", "Stoeger", 6,
		// entityManager);
		// executeCompetitorTotalResultCountTest("Clint", "Upchurch", 3,
		// entityManager);
		//
		// int totalCount = StageScoreSheetService.findAll().size();
		// assertEquals("Total score sheet count for all competitors should be
		// 31.", 31, totalCount);
		//
		// //
		// // assertEquals("Competitor Jarno should have 1 result for CLC01",
		// // 1, jarnoCLC01ResultCount);
		// //
		// // List<StageScoreSheet> jarnoCLC05Results =
		// // StageScoreSheetService.findClassifierStageResultsForCompetitor(
		// // "Jarno", "Virta", IPSCDivision.PRODUCTION, ClassifierStage.CLC05,
		// // entityManager);
		// // int jarnoCLC05ResultCount = jarnoCLC05Results.size();
		// // assertEquals("Competitor Jarno should have 1 result for CLC05",
		// // 1, jarnoCLC05ResultCount);
		// //
		// // StageScoreSheet jarnoCLC05ScoreSheet = jarnoCLC05Results.get(0);
		// // assertEquals("Competitor Jarno should have hit factor 5,5 for
		// // CLC05 (newer result)", 5.5,
		// // jarnoCLC05ScoreSheet.getHitFactor(), 0.01);
		// //
		//
		// entityManager.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// fail("Error during test");
		// }

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
}
