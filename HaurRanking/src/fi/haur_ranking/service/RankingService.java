package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class RankingService {
	public static Map<IPSCDivision, List<String[]>> generateRanking() {
		System.out.println("\n*** HAUR VARJORANKING ***");
		Map<IPSCDivision, List<String[]>> divisionRankings = new HashMap<IPSCDivision, List<String[]>>();
		for (IPSCDivision division : IPSCDivision.values()) {
			divisionRankings.put(division, generateRankingForDivision(division));
		}
	
		return divisionRankings;
	}
	private static List<String[]> generateRankingForDivision(IPSCDivision division) {
		System.out.println("\nDIVISION: " + division.toString());
		
		List<String[]> divisionRankingLineItems = new ArrayList<String[]>();
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		
		// Get a list of classifier stages for which results will be taken into account in ranking 
		// (stages with min. 2 results). Get average of two best results for each.
		
		Map<ClassifierStage, Double> classifierStageTopResultAvgerages = StageService.getClassifierStagesWithTwoOrMoreResults(division);
		
		List<Competitor> allCompetitors = CompetitorRepository.findAll(entityManager);
		List<Object[]> resultList = new ArrayList<Object[]>();
		for (Competitor competitor : allCompetitors) {
				List<StageScoreSheet> competitorLatestScoreSheets = 
						StageScoreSheetRepository.findClassifierStageResultsForCompetitor(competitor, division, 
								classifierStageTopResultAvgerages.keySet(), entityManager);
				double competitorRelativeScore = calculateCompetitorTopScoresAverage(competitorLatestScoreSheets, classifierStageTopResultAvgerages);
				if (competitorRelativeScore >= 0) resultList.add(new Object[]{competitor, competitorRelativeScore});
			}
		
		sortResultList(resultList);
		int position = 1;
		for (Object[] rankingItem : resultList) {
			Competitor competitor = (Competitor) rankingItem[0];
			Double result = (Double) rankingItem[1];
			System.out.println(position + ". " + competitor.getFirstName() + " " + competitor.getLastName() + " / score : " + result);
			position++;
		}
		entityManager.close();
		return divisionRankingLineItems;
	}
	private static double calculateCompetitorTopScoresAverage(List<StageScoreSheet> competitorLatestScoreSheets, 
			Map<ClassifierStage, Double> classifierStageTopResultAvgerages) {
		
		// If minimum 4 classifier results for competitor, calculate relative score for each of 8 latest results (competitor hit factor
		// divided by average of top two results for classifier stage). Then calculate average of 4 best relative scores.
		
		if (competitorLatestScoreSheets.size() >= 4) {
			List<Double> competitorRelativeScores = new ArrayList<Double>();
			int resultCounter = 0;
			for (StageScoreSheet sheet : competitorLatestScoreSheets) {
				ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
				double classifierStageTopTwoResultsAverage = classifierStageTopResultAvgerages.get(classifierStage);
				competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
				if (++resultCounter == 8) break;
			}
			Collections.sort(competitorRelativeScores);
			Collections.reverse(competitorRelativeScores);
			Double competitorTopScoresAverage; 
			double scoreSum = 0;
			for (Double score : competitorRelativeScores) scoreSum += score;
			competitorTopScoresAverage = scoreSum / competitorRelativeScores.size();
			
			return competitorTopScoresAverage;
		}
		return -1;
	}

	private static void sortResultList(List<Object[]> unorderedResultList) {
		// Generate a list of ordered competitor-score Object arrays. 
		List<Object[]> orderedResultList = new ArrayList<Object[]>();
		while(unorderedResultList.size() > 0) {
			int indexOfTopScore = -1;
			double topScore = -1;
			for (Object[] competitorResult : unorderedResultList) {
				if ((double) competitorResult[1] > topScore) {
					indexOfTopScore = unorderedResultList.indexOf(competitorResult);
					topScore = (double) competitorResult[1];
				}
			}
			orderedResultList.add(unorderedResultList.get(indexOfTopScore));
			unorderedResultList.remove(unorderedResultList.get(indexOfTopScore));
		}
		unorderedResultList.addAll(orderedResultList);
	}
}

