package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fi.haur_ranking.domain.Match;

public class MatchRepository {
	public static Match save(Match match) { 
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
				    EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(match);
			em.getTransaction().commit();
			emf.close();
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static List<Match> saveAll(List<Match> matches) { 
		List<Match> savedMatches = new ArrayList<Match>();
		for (Match match : matches) {
			savedMatches.add(save(match));
		}
		return savedMatches;
		
	}
	public static Match find(Long id) {
		Match match;
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			match = (Match) em.createQuery("SELECT m from Match m WHERE m.id = " + id).getSingleResult();
			emf.close();
			return match;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
