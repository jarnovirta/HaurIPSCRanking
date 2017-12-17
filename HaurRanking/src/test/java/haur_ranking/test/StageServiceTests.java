package haur_ranking.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.implementation.StageRepositoryImpl;
import haur_ranking.repository.winmss_repository.implementation.WinMSSStageRepositoryImpl;
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
	// database.
	@Test
	public void findTest() {
		StageService.init(new StageRepositoryImpl(), new WinMSSStageRepositoryImpl());
		List<Match> testMatches = TestUtils.createTestMatches();
		for (Match match : testMatches) {
			for (Stage stage : match.getStages()) {
				if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
					assertNotNull("find() should find all stages in database.", StageService.find(stage));
				}
			}
		}
	}
}
