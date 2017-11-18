package fi.haur_ranking.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MatchServiceTests.class, RankingServiceTests.class, StageScoreSheetServiceTests.class,
		StageServiceTests.class })
public class AllTests {

}
