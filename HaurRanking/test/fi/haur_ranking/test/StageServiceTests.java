package fi.haur_ranking.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.service.StageService;

public class StageServiceTests {
	@BeforeClass
	public static void init() {
		TestUtils.setupDatabase();
	}

	@AfterClass
	public static void cleanup() {
		TestUtils.cleanup();
	}

	// Test that all test stages are found for which test data was saved to
	// database
	@Test
	public void findTest() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Match> testMatches = TestUtils.createTestMatches();
		for (Match match : testMatches) {
			for (Stage stage : match.getStages()) {
				assertNotNull("All test stages should be in database.", StageService.find(stage, entityManager));
			}
		}
		entityManager.close();
	}
}
