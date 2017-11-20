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

		// If minimum 4 classifier results for competitor, calculate relative
		// score for each of 8 latest results (competitor hit factor
		// divided by average of top two results for classifier stage). Then
		// calculate average of 4 best relative scores.

		List<StageScoreSheet> validSheets = new ArrayList<StageScoreSheet>();
		for (StageScoreSheet sheet : competitorLatestScoreSheets) {
			if (classifierStageTopResultAverages.keySet().contains(sheet.getStage().getClassifierStage()))
				validSheets.add(sheet);
		}
		if (competitorLatestScoreSheets.size() >= 4) {

			List<Double> competitorRelativeScores = new ArrayList<Double>();
			List<Double> competitorHitFactors = new ArrayList<Double>();
			for (StageScoreSheet sheet : validSheets) {
				ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
				if (classifierStageTopResultAverages.keySet().contains(classifierStage)) {
					double classifierStageTopTwoResultsAverage = classifierStageTopResultAverages.get(classifierStage);
					competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
					competitorHitFactors.add(sheet.getHitFactor());
				}
			}
			Collections.sort(competitorHitFactors);
			Collections.reverse(competitorHitFactors);
			Collections.sort(competitorRelativeScores);
			Collections.reverse(competitorRelativeScores);
			if (competitorRelativeScores.size() > 4) {
				competitorRelativeScores.subList(4, competitorRelativeScores.size()).clear();
				competitorHitFactors.subList(4, competitorHitFactors.size());
			}

			Double competitorTopScoresAverage;
			Double competitorHitFactorsAverage;
			double scoreSum = 0;
			double hitFactorSum = 0;

			for (int i = 0; i < 4; i++) {
				scoreSum += competitorRelativeScores.get(i);
				hitFactorSum += competitorHitFactors.get(i);
			}

			competitorTopScoresAverage = scoreSum / 4;
			competitorHitFactorsAverage = hitFactorSum / 4;
			DivisionRankingLine line = new DivisionRankingLine(competitor, competitorTopScoresAverage,
					competitorHitFactorsAverage, competitorLatestScoreSheets.size());

			return line;
		}
		return null;
	}

	// Expects resultList to be ordered, top score first.
	private static void convertAverageScoresToPercentage(List<DivisionRankingLine> resultList) {
		if (resultList != null && resultList.size() > 0 && resultList.get(0).getBestResultsAverage() > 0) {
			double topScore = resultList.get(0).getBestResultsAverage();
			for (DivisionRankingLine line : resultList) {
				line.setResultPercentage(round(line.getBestResultsAverage() / topScore * 100));
			}
		}
	}

	private static DivisionRanking getDivisionRanking(IPSCDivision division) {
		DivisionRanking divisionRanking = new DivisionRanking(division);
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		Map<ClassifierStage, Double> classifierStageTopResultAverages = StageService
				.getClassifierStagesWithTwoOrMoreResults(division);
		if (classifierStageTopResultAverages.keySet().isEmpty())
			return divisionRanking;
		List<DivisionRankingLine> resultList = new ArrayList<DivisionRankingLine>();
		List<Competitor> competitors = CompetitorService.findAll();

		for (Competitor competitor : competitors) {
			List<StageScoreSheet> scoreSheets = StageScoreSheetRepository.find(competitor.getFirstName(),
					competitor.getLastName(), division, classifierStageTopResultAverages.keySet(), entityManager);

			DivisionRankingLine line = competitorTopScoresAverage(competitor, scoreSheets,
					classifierStageTopResultAverages);
			if (line != null) {
				resultList.add(line);
			}
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
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		RankingRepository.save(ranking, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private static int round(double d) {
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - i;
		if (result < 0.5) {
			return d < 0 ? -i : i;
		} else {
			return d < 0 ? -(i + 1) : i + 1;
		}
	}

}
