package fi.haur_ranking.repository.haur_ranking_repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HaurRankingDatabaseUtils {
	private static EntityManagerFactory productionEntityManagerFactory;
	private static EntityManagerFactory testsEntityManagerFactory;
		static {
			initialize();
	    }
	public static EntityManager createEntityManager() {    
		return productionEntityManagerFactory.createEntityManager();
	}
	
	public static EntityManager createTestsEntityManager() {    
		return testsEntityManagerFactory.createEntityManager();
	}
	public static void closeEntityManagerFactories() {
		if (productionEntityManagerFactory != null) productionEntityManagerFactory.close();
		if (testsEntityManagerFactory != null) testsEntityManagerFactory.close();
	}
	public static void initialize() {
        try {
        	productionEntityManagerFactory = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
        	testsEntityManagerFactory = Persistence.createEntityManagerFactory("fi.haur_ranking_testdatabase.jpa");
        } catch(ExceptionInInitializerError e) {
            throw e;
        }
	}
}
