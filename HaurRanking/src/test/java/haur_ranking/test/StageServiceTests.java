package haur_ranking.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.service.StageService;

public class StageServiceTests {
	@AfterClass
	public static void cleanup() {
		TestUtils.cleanup();
	}

	@BeforeClass
	public static void init() {
		TestUtils.setupDatabase();
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
