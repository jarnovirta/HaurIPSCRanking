package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Match;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;

public class MatchRepositoryImpl implements MatchRepository {
	@Override
	public Match find(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> resultList = null;
		Match resultMatch = null;
		try {

			String queryString = "SELECT m FROM Match m WHERE m.name = :name AND m.date = :matchDate";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setParameter("name", match.getName());
			query.setParameter("matchDate", match.getDate());
			resultList = query.getResultList();
			if (resultList.size() > 0) {
				resultMatch = resultList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return resultMatch;
	}

	@Override
	public List<Match> getMatchListPage(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> resultList = null;
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			final TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			resultList = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return resultList;
	}

	@Override
	public void delete(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (!entityManager.contains(match)) {
				match = entityManager.merge(match);
			}
			entityManager.remove(match);
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}

	@Override
	public Match findNewestMatch() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		Match match = null;
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setMaxResults(1);
			List<Match> matches = query.getResultList();
			if (matches != null && matches.size() > 0)
				match = matches.get(0);

		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return match;
	}

	@Override
	public List<Match> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Match> matches = null;
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			matches = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return matches;
	}

	@Override
	public int getMatchCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		int count = -1;
		try {
			count = ((Long) entityManager.createQuery("SELECT COUNT(m) from Match m").getSingleResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return count;

	}

	@Override
	public Match merge(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		try {
			match = entityManager.merge(match);
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return match;
	}

}
