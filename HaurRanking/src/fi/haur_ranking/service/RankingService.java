package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.DivisionRanking;
import fi.haur_ranking.domain.DivisionRankingLine;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Ranking;
import fi.haur_ranking.domain.StageScoreSheet;

public class RankingService {
	private static double calculateCompetitorTopScoresAverage(List<StageScoreSheet> competitorLatestScoreSheets,
			Map<ClassifierStage, Double> classifierStageTopResultAverages) {

		// If minimum 4 classifier results for competitor, calculate relative
		// score for each of 8 latest results (competitor hit factor
		// divided by average of top two results for classifier stage). Then
		// calculate average of 4 best relative scores.

		if (competitorLatestScoreSheets.size() >= 4) {
			List<Double> competitorRelativeScores = new ArrayList<Double>();

			for (StageScoreSheet sheet : competitorLatestScoreSheets) {
				ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
				if (classifierStageTopResultAverages.keySet().contains(classifierStage)) {
					double classifierStageTopTwoResultsAverage = classifierStageTopResultAverages.get(classifierStage);
					competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
				}
			}

			Collections.sort(competitorRelativeScores);
			Collections.reverse(competitorRelativeScores);
			if (competitorRelativeScores.size() > 4) {
				competitorRelativeScores.subList(4, competitorRelativeScores.size()).clear();
			}

			Double competitorTopScoresAverage;
			double scoreSum = 0;
			for (Double score : competitorRelativeScores)
				scoreSum += score;
			competitorTopScoresAverage = scoreSum / competitorRelativeScores.size();

			return competitorTopScoresAverage;
		}
		return -1;
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

	private static DivisionRanking generateDivisionRanking(IPSCDivision division) {
		DivisionRanking divisionRanking = new DivisionRanking(division);

		Map<ClassifierStage, Double> classifierStageTopResultAvgerages = StageService
				.getClassifierStagesWithTwoOrMoreResults(division);
		List<DivisionRankingLine> resultList = new ArrayList<DivisionRankingLine>();
		List<Competitor> competitors = CompetitorService.findAll();

		for (Competitor competitor : competitors) {
			List<StageScoreSheet> scoreSheets = StageScoreSheetService.findClassifierStageResultsForCompetitor(
					competitor.getFirstName(), competitor.getLastName(), division);
			double competitorResult = calculateCompetitorTopScoresAverage(scoreSheets,
					classifierStageTopResultAvgerages);
			if (competitorResult >= 0.0) {

				resultList.add(new DivisionRankingLine(competitor, competitorResult));
			}
		}

		Collections.sort(resultList);
		Collections.reverse(resultList);
		convertAverageScoresToPercentage(resultList);
		divisionRanking.setRankingLines(resultList);
		return divisionRanking;
	}

	public static Ranking generateRanking() {
		Ranking ranking = new Ranking();
		for (IPSCDivision division : IPSCDivision.values()) {
			ranking.getDivisionRankings().put(division, generateDivisionRanking(division));
		}
		return ranking;
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
