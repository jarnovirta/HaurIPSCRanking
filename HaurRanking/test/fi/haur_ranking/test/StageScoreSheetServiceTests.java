package fi.haur_ranking.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.service.MatchService;

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
    	System.out.println("\nDatabase service tests done. Clearing database");
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
    	System.out.println("\nStarting database service tests. Adding data to database.");
        MatchService.persist(TestUtils.createTestMatch());
    }

	
	@Test
	public void findAllFromHaurRankingDBTest() {
		System.out.println("Test ok");
	}
//	@Test
//	public void getTotalStageScoreSheetCountTest() {
//		
//	}
//	@Test
//	public void findClassifierStageResultsForCompetitorTest() {
//		
//	}
//	@Test
//	public void filterStageScoreSheetsExistingInDatabase() {
//		
//	}

}
