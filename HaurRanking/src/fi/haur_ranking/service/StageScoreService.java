package fi.haur_ranking.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fi.haur_ranking.database.winMss.WinMssDatabaseUtil;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.StageScoreSheet;


// Reads score data from a WinMSS .mdb file (Microsoft Access database)
public class StageScoreService {
	
	Connection connection;
	
	public List<StageScoreSheet> findScoreSheetsForMatch(Match match) {
		List<StageScoreSheet> stageScoreSheets= new ArrayList<StageScoreSheet>();
		connection = WinMssDatabaseUtil.connectToAccessDatabase();		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblMatchStageScore WHERE MatchId = " + match.getWinMssMatchId());
			while (resultSet.next()) {
				StageScoreSheet sheet = new StageScoreSheet();
				sheet.setWinMssStageId(resultSet.getInt(2));
				sheet.setWinMssMemberId(resultSet.getInt(3));
				sheet.setaHits(resultSet.getInt(4));
				sheet.setbHits(resultSet.getInt(5));
				sheet.setcHits(resultSet.getInt(6));
				sheet.setdHits(resultSet.getInt(7));
				sheet.setMisses(resultSet.getInt(8));
				sheet.setPenalties(resultSet.getInt(9));
				sheet.setProcedurals(resultSet.getInt(10));
				sheet.setTime(resultSet.getDouble(11));
				sheet.setScoresZeroedForStage(resultSet.getBoolean(12));
				stageScoreSheets.add(sheet);
			}
			resultSet.close();
			statement.close();
			WinMssDatabaseUtil.closeConnecion(connection);
			return stageScoreSheets;
		} catch (Exception ex) {
			ex.printStackTrace();
			WinMssDatabaseUtil.closeConnecion(connection);
			return null;
		}
	}
	
	public void testSave(StageScoreSheet sheet) {
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
				    
				    EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(sheet);
			em.getTransaction().commit();
			emf.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testSave(List<StageScoreSheet> sheets) {
		try {
			EntityManagerFactory emf = 
				      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
				    
				    EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			for (StageScoreSheet sheet : sheets)
				em.persist(sheet);
			em.getTransaction().commit();
			emf.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<StageScoreSheet> findAll() {
		EntityManagerFactory emf = 
			      Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			    EntityManager em = emf.createEntityManager();
		return em.createQuery("Select t from StageScoreSheet t").getResultList();
		
	}
	
}
