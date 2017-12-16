package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Match;

public class MatchRepository {
	public static Match find(Match match, EntityManager entityManager) {
		try {
			String queryString = "SELECT m FROM Match m WHERE m.name = :name AND m.date = :matchDate";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setParameter("name", match.getName());
			query.setParameter("matchDate", match.getDate());
			List<Match> result = query.getResultList();
			if (result.size() == 0)
				return null;
			else {
				return result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Match> getMatchListPage(int page, int pageSize, EntityManager entityManager) {
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			final TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void delete(Match match, EntityManager entityManager) {
		try {
			entityManager.remove(match);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Match findLatestMatch(EntityManager entityManager) {
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setMaxResults(1);
			List<Match> matches = query.getResultList();
			if (matches != null && matches.size() > 0)
				return matches.get(0);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Match> findAll(EntityManager entityManager) {
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getMatchCount(EntityManager entityManager) {
		int matchCount;
		try {
			matchCount = ((Long) entityManager.createQuery("SELECT COUNT(m) from Match m").getSingleResult())
					.intValue();
			return matchCount;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Match save(Match match, EntityManager entityManager) {
		try {
			return entityManager.merge(match);
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

}
