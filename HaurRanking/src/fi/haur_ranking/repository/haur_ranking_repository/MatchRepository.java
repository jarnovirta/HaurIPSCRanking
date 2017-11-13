package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.Match;

public class MatchRepository {
	public static Match persist(Match match, EntityManager entityManager) { 
		try {
			entityManager.persist(match);
			return find(match, entityManager);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	public static Match find(Match match, EntityManager entityManager) {
		try {
			String queryString = "SELECT m FROM Match m WHERE m.name = :name AND m.winMssDateString = :dateString";
			TypedQuery<Match> query = entityManager.createQuery(queryString, Match.class);
			query.setParameter("name", match.getName());
			query.setParameter("dateString", match.getWinMssDateString());
			List<Match> result = query.getResultList();
			if (result.size() == 0) return null;
			else return result.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getTotalMatchCount(EntityManager entityManager) {
		int matchCount;
		try {
			matchCount = ((Long) entityManager.createQuery("SELECT COUNT(m) from Match m").getSingleResult()).intValue();
			return matchCount;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
