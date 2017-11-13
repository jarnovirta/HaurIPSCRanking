package fi.haur_ranking.repository.haur_ranking_repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HaurRankingDatabaseUtils {
	private static EntityManagerFactory emf;
		static {
	        try {
	        	emf = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
	        } catch(ExceptionInInitializerError e) {
	            throw e;
	        }
	    }
	public static EntityManager createEntityManager() {    
		return emf.createEntityManager();
	}
}
