package haur_ranking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingLine;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Ranking;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.RankingRepository;
import haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class RankingService {
	private static DivisionRankingLine competitorTopScoresAverage(Competitor competitor,
			List<StageScoreSheet> competitorLatestScoreSheets,
			Map<ClassifierStage, Double> classifierStageTopResultAverages) {

		if (competitorLatestScoreSheets.size() < 4)
			return new DivisionRankingLine(competitor, false, competitorLatestScoreSheets.size());
		int resultCount = competitorLatestScoreSheets.size();
		if (competitorLatestScoreSheets.size() > 8)
			competitorLatestScoreSheets = competitorLatestScoreSheets.subList(0, 8);

		List<Double> competitorRelativeScores = new ArrayList<Double>();
		List<Double> competitorHitFactors = new ArrayList<Double>();

		double hitFactorSum = 0.0;
		for (StageScoreSheet sheet : competitorLatestScoreSheets) {
			hitFactorSum += sheet.getHitFactor();
			ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
			if (classifierStageTopResultAverages.keySet().contains(classifierStage)) {
				double classifierStageTopTwoResultsAverage = classifierStageTopResultAverages.get(classifierStage);
				competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
				competitorHitFactors.add(sheet.getHitFactor());
			}
		}
		Double competitorHitFactorsAverage = hitFactorSum / competitorLatestScoreSheets.size();
		Collections.sort(competitorHitFactors);
		Collections.reverse(competitorHitFactors);
		Collections.sort(competitorRelativeScores);
		Collections.reverse(competitorRelativeScores);
		if (competitorRelativeScores.size() > 4) {
			competitorRelativeScores.subList(4, competitorRelativeScores.size()).clear();
			competitorHitFactors.subList(4, competitorHitFactors.size());
		}

		Double competitorTopScoresAverage;
		double scoreSum = 0;
		for (int i = 0; i < 4; i++) {
			scoreSum += competitorRelativeScores.get(i);
			hitFactorSum += competitorHitFactors.get(i);
		}

		competitorTopScoresAverage = scoreSum / 4;
		DivisionRankingLine line = new DivisionRankingLine(competitor, true, competitorTopScoresAverage,
				competitorHitFactorsAverage, resultCount);

		return line;

	}

	// Expects resultList to be ordered, top score first.
	private static void convertAverageScoresToPercentage(List<DivisionRankingLine> resultList) {
		if (resultList != null && resultList.size() > 0 && resultList.get(0).getBestResultsAverage() > 0) {
			double topScore = resultList.get(0).getBestResultsAverage();
			for (DivisionRankingLine line : resultList) {
				line.setResultPercentage(line.getBestResultsAverage() / topScore * 100);
			}
		}
	}

	private static DivisionRanking getDivisionRanking(IPSCDivision division) {
		DivisionRanking divisionRanking = new DivisionRanking(division);
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		// Get valid classifiers with two or more results, and top two hit
		// scores average for each.
		Map<ClassifierStage, Double> classifierStageTopResultAverages = StageService
				.getClassifierStagesWithTwoOrMoreResults(division);

		if (classifierStageTopResultAverages.keySet().isEmpty())
			return divisionRanking;
		List<DivisionRankingLine> resultList = new ArrayList<DivisionRankingLine>();
		List<Competitor> competitors = CompetitorService.findAll();

		for (Competitor competitor : competitors) {
			// Get competitor score sheets and limit number to eight.
			List<StageScoreSheet> scoreSheets = StageScoreSheetRepository.find(competitor.getFirstName(),
					competitor.getLastName(), division, classifierStageTopResultAverages.keySet(), entityManager);
			if (scoreSheets.size() > 0)
				resultList.add(competitorTopScoresAverage(competitor, scoreSheets, classifierStageTopResultAverages));
		}

		Collections.sort(resultList);
		Collections.reverse(resultList);
		convertAverageScoresToPercentage(resultList);
		divisionRanking.setRankingLines(resultList);
		entityManager.close();
		return divisionRanking;
	}

	public static Ranking getRanking() {
		Ranking ranking = new Ranking();
		for (IPSCDivision division : IPSCDivision.values()) {
			ranking.getDivisionRankings().add(getDivisionRanking(division));
		}
		persist(ranking);

		System.out.println("\n*** HAUR VARJORANKING ***");
		System.out.println(ranking.getDate());

		for (DivisionRanking div : ranking.getDivisionRankings()) {
			System.out.println(div.getDivision().toString() + ":");
			if (div.getRankingLines() == null)
				continue;
			int pos = 1;
			for (DivisionRankingLine line : div.getRankingLines()) {
				System.out.println(
						pos++ + ". " + line.getCompetitor().getFirstName() + " " + line.getCompetitor().getLastName()
								+ ": " + line.getResultPercentage() + "% " + line.getBestResultsAverage());
			}
		}

		return ranking;
	}

	public static void persist(Ranking ranking) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		RankingRepository.save(ranking, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

}
