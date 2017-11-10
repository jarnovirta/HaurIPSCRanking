package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fi.haur_ranking.domain.StageScoreSheet;

public class StageScoreSheetRepository {
	public static StageScoreSheet save(StageScoreSheet sheet) { 
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
				    EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(sheet);
			em.getTransaction().commit();
			emf.close();
			return find(sheet.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static StageScoreSheet find(Long id) {
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			StageScoreSheet sheet = (StageScoreSheet) em.createQuery("SELECT s from StageScoreSheet s WHERE s.id = " + id).getSingleResult();
			emf.close();
			return sheet; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static List<StageScoreSheet> findAll() {
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			List<StageScoreSheet> sheets = (List<StageScoreSheet>) em.createQuery("SELECT s from StageScoreSheet s").getResultList();
			emf.close();
			return sheets; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getTotalStageScoreSheetCount() {
		int matchCount;
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			matchCount = ((Long) em.createQuery("SELECT COUNT(s) from StageScoreSheet s").getSingleResult()).intValue();
			emf.close();
			return matchCount;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}

