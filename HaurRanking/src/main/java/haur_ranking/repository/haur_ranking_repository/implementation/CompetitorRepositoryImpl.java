package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Competitor;
import haur_ranking.repository.haur_ranking_repository.CompetitorRepository;

public class CompetitorRepositoryImpl implements CompetitorRepository {
	@Override
	public Competitor find(String firstName, String lastName, EntityManager entityManager) {
		String queryString = "SELECT c FROM Competitor c WHERE c.firstName = :firstName AND c.lastName = :lastName";

		try {
			TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);

			List<Competitor> existingCompetitors = query.getResultList();
			if (existingCompetitors.size() > 0)
				return existingCompetitors.get(0);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public List<Competitor> getCompetitorListPage(int page, int pageSize, EntityManager entityManager) {
		try {

			String queryString = "SELECT c FROM Competitor c ORDER BY c.lastName";
			final TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Competitor> findAll(EntityManager entityManager) {
		try {
			String queryString = "SELECT c FROM Competitor c ORDER BY c.lastName";
			final TypedQuery<Competitor> query = entityManager.createQuery(queryString, Competitor.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getTotalCompetitorCount(EntityManager entityManager) {
		try {
			return ((Long) entityManager.createQuery("SELECT COUNT(c) from Competitor c").getSingleResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void persist(Competitor competitor, EntityManager entityManager) {
		try {
			entityManager.persist(competitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Competitor competitor, EntityManager entityManager) {
		try {
			if (!entityManager.contains(competitor))
				competitor = entityManager.merge(competitor);
			entityManager.remove(competitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
