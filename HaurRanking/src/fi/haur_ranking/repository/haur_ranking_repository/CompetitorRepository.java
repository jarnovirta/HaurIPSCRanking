package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.Competitor;

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
	public static Competitor find(String firstName, String lastName, EntityManager entityManager) {
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
			return find(competitor.getFirstName(), competitor.getLastName(), entityManager);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
