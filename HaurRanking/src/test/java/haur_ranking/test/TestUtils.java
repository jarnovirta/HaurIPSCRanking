package haur_ranking.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.service.MatchService;
import haur_ranking.utils.DateFormatUtils;

// LUO LAPPUJA MUIHIN DIVISIOONIIN

public class TestUtils {

	static List<Match> testMatches = null;

	static ClassifierStage[] classifierStages = new ClassifierStage[] { ClassifierStage.CLC01, ClassifierStage.CLC03,
			ClassifierStage.CLC05, ClassifierStage.CLC07, ClassifierStage.CLC09, ClassifierStage.CLC11,
			ClassifierStage.CLC13, ClassifierStage.CLC15, ClassifierStage.CLC17, ClassifierStage.CLC19 };
	static Competitor jarno = new Competitor("Jarno", "Virta", null);
	static Competitor jerry = new Competitor("Jerry", "Miculek", "");
	static Competitor ben = new Competitor("Ben", "Stoeger", "");
	static Competitor rob = new Competitor("Rob", "Leatham", "");
	static Competitor clint = new Competitor("Clint", "Eastwood", "");
	static Competitor charles = new Competitor("Charles", "Bronson", "");

	static Double[] jarnoNewMatchHitFactors = new Double[] { 4.0, 3.9, 2.0, null, 5.5, 4.0, 4.5, null, null, null };

	static Double[] jerryNewMatchHitFactors = new Double[] { 3.1, null, null, 3.9, null, 2.3, 3.9, null, null, 1.5 };

	static Double[] benNewMatchHitFactors = new Double[] { 1.5, 4.1, 2.9, 0.0, 4.8, null, null, null, null, null };

	static Double[] robNewMatchHitFactors = new Double[] { 2.0, 2.2, 3.6, null, null, null, null, null, 5.2, null };

	static Double[] jarnoOldMatchHitFactors = new Double[] { null, null, null, null, 6.0, null, null, 3.6, null, 3.8 };

	static Double[] jerryOldMatchHitFactors = new Double[] { null, null, null, 4.2, null, null, null, 2.5, null, null };

	static Double[] jarnoSpringMatchHitFactors = new Double[] { null, null, null, 5.1, null, null, null, null, null,
			null };

	static Double[] clintSpringMatchHitFactors = new Double[] { 3.5, 4.5, 2.5, 5.5, null, null, null, null, 4.5, null };

	static Double[] charlesSpringMatchHitFactors = new Double[] { 2.2, 5.8, 6.0, 5.7, null, null, null, null, null,
			null };

	protected static void cleanup() {
		try {
			HaurRankingDatabaseUtils.closeEntityManagerFactory();
			deleteDatabase();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static Match createNewTestMatch() {
		Match newTestMatch = new Match();
		newTestMatch.setName("New match");
		newTestMatch.setDate(DateFormatUtils.stringToCalendar("18.11.2017"));
		newTestMatch.setStages(createTestStages(newTestMatch));
		return newTestMatch;
	}

	protected static Match createOldTestMatch() {
		Match oldTestMatch = new Match();
		oldTestMatch.setName("Old match");
		oldTestMatch.setDate(DateFormatUtils.stringToCalendar("12.11.2017"));
		oldTestMatch.setStages(createTestStages(oldTestMatch));
		return oldTestMatch;
	}

	protected static List<StageScoreSheet> createScoreSheetList(Match match, Competitor competitor, double[] hitFactors,
			ClassifierStage classifier) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		for (double hf : hitFactors)
			sheets.add(new StageScoreSheet(competitor, hf, new Stage(match, classifier.toString(), classifier),
					IPSCDivision.PRODUCTION));

		return sheets;
	}

	protected static Match createSpringTestMatch() {
		Match springTestMatch = new Match();
		springTestMatch.setName("Spring match");
		springTestMatch.setDate(DateFormatUtils.stringToCalendar("01.04.2017"));
		springTestMatch.setStages(createTestStages(springTestMatch));
		return springTestMatch;
	}

	private static Stage createStage(Match match, ClassifierStage classifier) {
		Stage stage = new Stage(match, classifier.toString(), classifier);
		stage.setStageScoreSheets(createStageScoreSheets(stage));

		return stage;
	}

	public static List<StageScoreSheet> createStageScoreSheets(Stage stage) {
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		int index = 0;
		for (ClassifierStage classifier : ClassifierStage.values()) {
			if (classifier.equals(stage.getClassifierStage()))
				break;
			index++;
		}
		if (stage.getMatch().getName().equals("New match")) {
			if (jarnoNewMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(jarno, jarnoNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (jerryNewMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(jerry, jerryNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (benNewMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(ben, benNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (robNewMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(rob, robNewMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
		}
		if (stage.getMatch().getName().equals("Old match")) {
			if (jarnoOldMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(jarno, jarnoOldMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
			if (jerryOldMatchHitFactors[index] != null)
				sheets.add(new StageScoreSheet(jerry, jerryOldMatchHitFactors[index], stage, IPSCDivision.PRODUCTION));
		} else {
			if (stage.getMatch().getName().equals("Spring match")) {
				if (jarnoSpringMatchHitFactors[index] != null)
					sheets.add(new StageScoreSheet(jarno, jarnoSpringMatchHitFactors[index], stage,
							IPSCDivision.PRODUCTION));
				if (clintSpringMatchHitFactors[index] != null)
					sheets.add(new StageScoreSheet(clint, clintSpringMatchHitFactors[index], stage,
							IPSCDivision.STANDARD));
				if (charlesSpringMatchHitFactors[index] != null) {
					sheets.add(new StageScoreSheet(charles, charlesSpringMatchHitFactors[index], stage,
							IPSCDivision.STANDARD));
				}

			}
		}
		return sheets;
	}

	protected static List<Match> createTestMatches() {
		List<Match> matches = new ArrayList<Match>();
		matches.add(createNewTestMatch());
		matches.add(createOldTestMatch());
		matches.add(createSpringTestMatch());
		return matches;
	}

	protected static List<Stage> createTestStages(Match match) {
		List<Stage> testStages = new ArrayList<Stage>();

		for (ClassifierStage classifier : classifierStages) {
			testStages.add(createStage(match, classifier));
		}
		return testStages;
	}

	protected static void deleteDatabase() {
		try {
			FileUtils.deleteDirectory(new File("data"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<ClassifierStage, Double> getAverageOfTopTwoForStage() {
		Map<ClassifierStage, Double> averages = new HashMap<ClassifierStage, Double>();
		averages.put(ClassifierStage.CLC01, 3.55);
		averages.put(ClassifierStage.CLC03, 4.0);
		averages.put(ClassifierStage.CLC05, 3.25);
		averages.put(ClassifierStage.CLC07, 4.65);
		averages.put(ClassifierStage.CLC09, 5.75);
		averages.put(ClassifierStage.CLC11, 3.15);
		averages.put(ClassifierStage.CLC13, 4.2);
		averages.put(ClassifierStage.CLC15, 3.05);
		averages.put(ClassifierStage.CLC17, 2.65);
		return averages;
	}

	protected static List<Object[]> getCompetitorAverageScoreList() {
		List<Object[]> averageScoreList = new ArrayList<Object[]>();
		averageScoreList.add(new Object[] { jarno, 1.253 });
		averageScoreList.add(new Object[] { jerry, 0.865 });
		averageScoreList.add(new Object[] { ben, 0.794 });
		return averageScoreList;
	}

	protected static void setupDatabase() {
		deleteDatabase();
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		MatchService.saveAll(TestUtils.createTestMatches());

		entityManager.getTransaction().commit();
		entityManager.close();
		HaurRankingDatabaseUtils.closeEntityManagerFactory();
	}

	protected static Ranking getTestRanking() {
		Ranking ranking = new Ranking();
		DivisionRanking divisionRanking = new DivisionRanking(IPSCDivision.PRODUCTION);
		List<DivisionRankingRow> rows = new ArrayList<DivisionRankingRow>();
		rows.add(new DivisionRankingRow(jarno, divisionRanking, true, 1));
		rows.add(new DivisionRankingRow(jerry, divisionRanking, true, 1));
		rows.add(new DivisionRankingRow(ben, divisionRanking, true, 1));
		rows.add(new DivisionRankingRow(clint, divisionRanking, false, 1));
		rows.add(new DivisionRankingRow(rob, divisionRanking, true, 1));

		divisionRanking.setDivisionRankingRows(rows);
		List<DivisionRanking> divisionRankings = new ArrayList<DivisionRanking>();
		divisionRankings.add(divisionRanking);
		ranking.setDivisionRankings(divisionRankings);
		return ranking;
	}

	protected static Ranking getCompareToRanking() {
		Ranking compareToRanking = new Ranking();
		DivisionRanking compareToDivisionRanking = new DivisionRanking(IPSCDivision.PRODUCTION);

		List<DivisionRankingRow> compareToRows = new ArrayList<DivisionRankingRow>();
		compareToRows.add(new DivisionRankingRow(jerry, compareToDivisionRanking, true, 4));
		compareToRows.add(new DivisionRankingRow(jarno, compareToDivisionRanking, true, 4));
		compareToRows.add(new DivisionRankingRow(ben, compareToDivisionRanking, true, 4));
		compareToRows.add(new DivisionRankingRow(rob, compareToDivisionRanking, true, 4));
		compareToRows.add(new DivisionRankingRow(clint, compareToDivisionRanking, false, 4));

		compareToDivisionRanking.setDivisionRankingRows(compareToRows);
		List<DivisionRanking> compareToDivisionRankings = new ArrayList<DivisionRanking>();
		compareToDivisionRankings.add(compareToDivisionRanking);
		compareToRanking.setDivisionRankings(compareToDivisionRankings);
		return compareToRanking;
	}

}
