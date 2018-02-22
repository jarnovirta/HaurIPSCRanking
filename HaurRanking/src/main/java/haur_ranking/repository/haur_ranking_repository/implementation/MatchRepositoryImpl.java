package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Match;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;

public class MatchRepositoryImpl implements MatchRepository {
	@Override
	public Match find(Match match, EntityManager entityManager) {
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
		return resultMatch;
	}

	@Override
	public List<Match> getMatchListPage(int page, int pageSize, EntityManager entityManager) {
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
		return resultList;
	}

	@Override
	public void delete(Match match, EntityManager entityManager) {
		try {
			if (!entityManager.contains(match)) {
				match = entityManager.merge(match);
			}
			entityManager.remove(match);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Match findNewestMatch(EntityManager entityManager) {
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
		return match;
	}

	@Override
	public List<Match> findAll(EntityManager entityManager) {
		List<Match> matches = null;
		try {
			String queryString = "SELECT m FROM Match m ORDER BY m.date DESC";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			matches = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return matches;
	}

	@Override
	public int getMatchCount(EntityManager entityManager) {
		int count = -1;
		try {
			count = ((Long) entityManager.createQuery("SELECT COUNT(m) from Match m").getSingleResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return count;

	}

	@Override
	public void persist(Match match, EntityManager entityManager) {
		try {
			entityManager.persist(match);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
