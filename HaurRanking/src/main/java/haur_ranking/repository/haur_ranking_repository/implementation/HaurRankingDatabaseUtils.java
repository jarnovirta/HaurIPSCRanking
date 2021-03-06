package haur_ranking.repository.haur_ranking_repository.implementation;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HaurRankingDatabaseUtils {
	private static EntityManagerFactory entityManagerFactory;

	public static void closeEntityManagerFactory() {
		if (entityManagerFactory != null && entityManagerFactory.isOpen())
			entityManagerFactory.close();

	}

	public static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null || !entityManagerFactory.isOpen())
			initialize();
		return entityManagerFactory;
	}

	public static void initialize() {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
		} catch (ExceptionInInitializerError e) {
			throw e;
		}
	}
}