package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Competitor;

public class CompetitorRepository {
	public static Competitor find(String firstName, String lastName, String winMSSComment,
			EntityManager entityManager) {
		String queryString = "SELECT c FROM Competitor c WHERE c.firstName = :firstName AND c.lastName = :lastName";
		if (winMSSComment != null)
			queryString += " AND c.winMSSComment = :winMSSComment";
		try {
			TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			if (winMSSComment != null)
				query.setParameter("winMSSComment", winMSSComment);
			List<Competitor> existingCompetitors = query.getResultList();
			if (existingCompetitors.size() > 0)
				return existingCompetitors.get(0);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public static List<Competitor> findAll(EntityManager entityManager) {

		try {
			String queryString = "SELECT c FROM Competitor c ORDER BY c.lastName";
			final TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getTotalCompetitorCount(EntityManager entityManager) {
		try {
			return ((Long) entityManager.createQuery("SELECT COUNT(c) from Competitor c").getSingleResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Competitor persist(Competitor competitor, EntityManager entityManager) {
		try {
			entityManager.persist(competitor);
			return find(competitor.getFirstName(), competitor.getLastName(), competitor.getWinMSSComment(),
					entityManager);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void delete(Competitor competitor, EntityManager entityManager) {
		try {
			if (!entityManager.contains(competitor))
				competitor = entityManager.merge(competitor);
			entityManager.remove(competitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
