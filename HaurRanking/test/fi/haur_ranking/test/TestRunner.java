package fi.haur_ranking.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import fi.haur_ranking.test.RankingServiceTests;

public class TestRunner {
	public static void main(String[] args) {
	  Result result = JUnitCore.runClasses(RankingServiceTests.class);
	  for (Failure failure : result.getFailures()) {
	    System.out.println(failure.toString());
	  }
	}
}
