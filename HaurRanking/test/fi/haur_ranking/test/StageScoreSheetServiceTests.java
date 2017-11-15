package fi.haur_ranking.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;

public class StageScoreSheetServiceTests {
	EntityManager entityManager;
		
    @Before
    public void setUpDB() {
    	HaurRankingDatabaseUtils.initialize();
    	entityManager = HaurRankingDatabaseUtils.createTestsEntityManager();
    	addData();
    }

    @After
    public void tearDown() {
        try {
        	entityManager.getTransaction().begin();
        	entityManager.createQuery("DELETE FROM Match m").executeUpdate();
        	entityManager.createQuery("DELETE FROM Stage s").executeUpdate();
            entityManager.createQuery("DELETE FROM StageScoreSheet s").executeUpdate();
            entityManager.createQuery("DELETE FROM Competitor c").executeUpdate();
            entityManager.getTransaction().commit();
            HaurRankingDatabaseUtils.closeEntityManagerFactories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addData() {
        
    }

	
	@Test
	public void findAllFromHaurRankingDBTest() {
	}
	@Test
	public void getTotalStageScoreSheetCountTest() {
		
	}
	@Test
	public void findClassifierStageResultsForCompetitorTest() {
		
	}
	@Test
	public void filterStageScoreSheetsExistingInDatabase() {
		
	}

}
