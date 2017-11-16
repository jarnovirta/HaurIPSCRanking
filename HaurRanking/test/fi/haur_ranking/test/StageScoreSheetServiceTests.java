package fi.haur_ranking.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.MatchRepository;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
import fi.haur_ranking.service.CompetitorService;

public class StageScoreSheetServiceTests {
	
	Match testMatch;
	Competitor competitorJarno;
	
    @BeforeClass
    public static void setUpDB() {
    	deleteDatabase();
    	Match testMatch = TestUtils.createTestMatch();
    	EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
    	entityManager.getTransaction().begin();
    	testMatch = MatchRepository.save(testMatch, entityManager);

    	entityManager.getTransaction().commit();
    	entityManager.close();
    	HaurRankingDatabaseUtils.closeEntityManagerFactory();
    }

    @AfterClass
    public static void cleanup() {
        try {
            HaurRankingDatabaseUtils.closeEntityManagerFactory();
            deleteDatabase();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findClassifierStageResultsForCompetitorTest() {
    	
    	try {
    		Set<ClassifierStage> classifiers = new HashSet<ClassifierStage>();
			classifiers.add(ClassifierStage.CLC01);
			classifiers.add(ClassifierStage.CLC02);
			Competitor competitorJarno = CompetitorService.findByName("Jarno", "Virta");
			
    		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
    		
			List<StageScoreSheet> jarnoStageScoreSheets = StageScoreSheetRepository.findClassifierStageResultsForCompetitor(competitorJarno, IPSCDivision.PRODUCTION, classifiers, entityManager);
			assertEquals("Database should have 4 classifier score sheets for competitor Jarno ", 4, jarnoStageScoreSheets.size());
			
			entityManager.close();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		fail("Error during test");
    	}
    	
	}
	@Test
	public void filterStageScoreSheetsExistingInDatabase() {
		
	}
    private static void deleteDatabase() {
    	try {
    		FileUtils.deleteDirectory(new File("data"));
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
