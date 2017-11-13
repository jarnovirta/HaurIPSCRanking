package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
	
	public static Map<ClassifierStage, Map<Competitor, Double>> getCompetitorRankingScoresForClassifierStages(Map<ClassifierStage, Double> classifierStages, IPSCDivision division, EntityManager entityManager) {
		
		Map<ClassifierStage, Map<Competitor, Double>> competitorRankingScoresByClassifierStage = new HashMap<ClassifierStage, Map<Competitor, Double>>();
		
		List<StageScoreSheet> competitorScoreSheetsForClassifierStage = new ArrayList<StageScoreSheet>();
		System.out.println("DIVISION: " + division.toString());
		try {
			
			String queryString = "SELECT c FROM Competitor c";
//					+ "DISTINCT(s.competitor) FROM StageScoreSheet s WHERE s.stage.classifierStage = :classifierStage"
//					+ " AND s.ipscDivision = :division";
			final TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
//			query.setParameter("classifierStage", classifier);
//			query.setParameter("division", division);
			
			List<Competitor> allCompetitors = query.getResultList();
			
			// KORJATTAVA ORDER BY DATE
			
			Map<Competitor, List<StageScoreSheet>> competitorList = new HashMap<Competitor, List<StageScoreSheet>>();
			System.out.println("LOOP COMPETITORS AND CHECK MINIMUM 4 RESULTS");
			for (Competitor competitor : allCompetitors) {
				queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor = :competitor AND s.stage.classifierStage IN :classifierStage "
						+ "AND s.ipscDivision = :division";
						 
				final TypedQuery<StageScoreSheet> classifierScoreSheetsForCompetitorQuery = entityManager.createQuery(queryString, StageScoreSheet.class);
				classifierScoreSheetsForCompetitorQuery.setParameter("classifierStage", classifierStages.keySet());
				
				classifierScoreSheetsForCompetitorQuery.setParameter("division", division);
				classifierScoreSheetsForCompetitorQuery.setParameter("competitor", competitor);
				List<StageScoreSheet> classifierScoreSheetsForCompetitor = classifierScoreSheetsForCompetitorQuery.getResultList();
				
				// System.out.println("Competitor " + competitor.getFirstName() + " " + competitor.getLastName() + " has " + classifierScoreSheetsForCompetitor.size() + " classifier score cards");
				if (classifierScoreSheetsForCompetitor.size() >= 4) {
					for (StageScoreSheet sheet : classifierScoreSheetsForCompetitor) {
						System.out.println(sheet.getHitFactor() + " - " + sheet.getLastModifiedInWinMSSDatabaseString());
					}
					competitorList.put(competitor, classifierScoreSheetsForCompetitor.subList(0, 7));
					System.out.println("Added " + classifierScoreSheetsForCompetitor.subList(0, 7).size() + " results for competitor, best result " + classifierScoreSheetsForCompetitor.subList(0, 7).get(0).getHitFactor());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
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
