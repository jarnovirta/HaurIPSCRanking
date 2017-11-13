package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.StageScoreSheet;

public class CompetitorRepository {
	public static int getTotalCompetitorCount(EntityManager entityManager) {
		try {
			return ((Long) entityManager.createQuery("SELECT COUNT(c) from Competitor c").getSingleResult()).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static List<Object[]> getCompetitorRankingScoresForClassifierStages(Map<ClassifierStage, Double> classifierStages, IPSCDivision division, EntityManager entityManager) {

		List<Object[]> orderedResultList = new ArrayList<Object[]>();
		List<Object[]> unorderedResultList = new ArrayList<Object[]>();
		List<Competitor> allCompetitors = findAll(entityManager);
		try {
			// KORJATTAVA ORDER BY DATE
			
			for (Competitor competitor : allCompetitors) {
				String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor = :competitor AND s.stage.classifierStage IN :classifierStage "
						+ "AND s.ipscDivision = :division ORDER BY s.lastModifiedInWinMSSDatabaseString DESC";
						 
				final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
				query.setParameter("classifierStage", classifierStages.keySet());
				query.setParameter("division", division);
				query.setParameter("competitor", competitor);
				query.setMaxResults(8);
				List<StageScoreSheet> competitorLatestScoreSheets = query.getResultList();

				// If minimum 4 classifier results for competitor, calculate relative score for each of 8 latest results (competitor hit factor
				// divided by average of top two results for classifier stage). Then calculate average of 4 best relative scores.
				if (competitorLatestScoreSheets.size() >= 4) {
					List<Double> competitorRelativeScores = new ArrayList<Double>();
					int resultCounter = 0;
					for (StageScoreSheet sheet : competitorLatestScoreSheets) {
						ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
						double classifierStageTopTwoResultsAverage = classifierStages.get(classifierStage);
						competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
						if (++resultCounter == 4) break;
					}
					Double competitorTopResultsAverage; 
					double scoreSum = 0;
					for (Double score : competitorRelativeScores) scoreSum += score;
					competitorTopResultsAverage = scoreSum / competitorRelativeScores.size();
					Object[] competitorTotalScore = new Object[] { competitor, competitorTopResultsAverage };
					unorderedResultList.add(competitorTotalScore);
				}
			}
			orderedResultList = new ArrayList<Object[]>();
			
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return orderedResultList;
	}
	public static List<Competitor> findAll(EntityManager entityManager) {
		try {
			String queryString = "SELECT c FROM Competitor c";
			final TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			return query.getResultList();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Competitor findByName(String firstName, String lastName, EntityManager entityManager) {
		String queryString = "SELECT c FROM Competitor c WHERE c.firstName = :firstName AND c.lastName = :lastName";
		try {
			TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			List<Competitor> existingCompetitors = query.getResultList();
			if (existingCompetitors.size() > 0) return existingCompetitors.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	public static Competitor persist(Competitor competitor, EntityManager entityManager) {
		try {
			entityManager.persist(competitor);
			return findByName(competitor.getFirstName(), competitor.getLastName(), entityManager);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
