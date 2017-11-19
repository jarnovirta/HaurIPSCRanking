package haur_ranking.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import haur_ranking.service.MatchService;

public class MatchServiceTests {
	@AfterClass
	public static void cleanup() {
		TestUtils.cleanup();
	}

	@BeforeClass
	public static void init() {
		TestUtils.setupDatabase();
	}

	@Test
	public void getTotalMatchCountTest() {
		assertEquals("Database should have two matches which were saved by StageScoreSheetServiceTests.", 2,
				MatchService.getTotalMatchCount());
	}
}
