package fi.haur_ranking.repository.haur_ranking_repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CompetitorRepository {
	public static int getTotalCompetitorCount() {
		int matchCount;
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			matchCount = ((Long) em.createQuery("SELECT COUNT(c) from Competitor c").getSingleResult()).intValue();
			emf.close();
			return matchCount;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
