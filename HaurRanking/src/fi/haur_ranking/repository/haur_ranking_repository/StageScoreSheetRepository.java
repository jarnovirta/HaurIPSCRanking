package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Sheet;

import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
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

	public static boolean isInHaurRankingDatabase(List<StageScoreSheet> stageSheets) {
//		System.out.println("CHECKING DB FOR SCORESHEET: " + sheet.getLastModifiedInWinMSSDatabaseString() 
//				+ " " + sheet.getStage().getName() + " " + sheet.getStage().getMatch().getName());
//		
		System.out.println("CHECKING DB FOR SCORESHEETS");
		
		List<StageScoreSheet> resultList = new ArrayList<StageScoreSheet>();
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			for (StageScoreSheet sheet : stageSheets) {
				// Must identify StageScoreSheet as same by multiple criteria because lastModified date may be same for many score cards 
				// if imported into database. Cannot store WinMSS database id and use it because this would cause a dependency to WinMSS database
				// and could cause problems in use.
				
				String queryString = "SELECT s FROM StageScoreSheet s WHERE s.lastModifiedInWinMSSDatabaseString = :lastModified AND s.stage.name = :stageName"
						+ " AND s.stage.match.name = :matchName AND s.competitor.firstName = :firstName AND s.competitor.lastName = :lastName"
						+ " AND s.ipscDivision = :ipscDiv";
						
				final Query query = em.createQuery(queryString);
				query.setParameter("lastModified", sheet.getLastModifiedInWinMSSDatabaseString());
				query.setParameter("stageName", sheet.getStageName());
				query.setParameter("matchName", sheet.getMatchName());
				query.setParameter("firstName", sheet.getCompetitor().getFirstName());
				query.setParameter("lastName", sheet.getCompetitor().getLastName());
				query.setParameter("ipscDiv", sheet.getIpscDivision());
				List<StageScoreSheet> sheets = (List<StageScoreSheet>) query.getResultList();
				if (sheets.size() > 0) resultList.addAll(sheets);
			}
			em.close();
			emf.close();
			System.out.println("Found " + resultList.size() + " existing sheets, " + (stageSheets.size() - resultList.size()) + " new sheets.");
			if (resultList.size() > 0) {
				return true;
			}
			
//			queryString = "SELECT s FROM Stage s WHERE s.name='" + sheet.getStage().getName() + "' AND s.match.name='" 
//					+ sheet.getStage().getMatch().getName() + "'";
//			List<Stage> stages = (List<Stage>) em.createQuery(queryString).getResultList();
			
			return false;
//			 if (stages.size() > 0) return true;
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
		
		
	
}

