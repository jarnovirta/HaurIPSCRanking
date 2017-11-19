package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.StageScoreSheet;

public class StageScoreSheetRepository {

	public static void filterStageScoreSheetsExistingInDatabase(List<StageScoreSheet> sheets,
			EntityManager entityManager) {

		List<StageScoreSheet> removeScoreSheets = new ArrayList<StageScoreSheet>();
		for (StageScoreSheet sheet : sheets) {
			try {
				// Must identify StageScoreSheet as same by multiple criteria
				// because lastModified date may be same for many score cards
				// if imported into database. Cannot store WinMSS database id
				// and use it because this would cause a dependency to WinMSS
				// database
				// and could cause problems in use.

				String queryString = "SELECT s FROM StageScoreSheet s WHERE s.lastModifiedInWinMSSDatabaseString = :lastModified AND s.stage.name = :stageName"
						+ " AND s.stage.match.name = :matchName AND s.competitor.firstName = :firstName AND s.competitor.lastName = :lastName"
						+ " AND s.ipscDivision = :ipscDiv";

				final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
				query.setParameter("lastModified", sheet.getLastModifiedInWinMSSDatabaseString());
				query.setParameter("firstName", sheet.getCompetitor().getFirstName());
				query.setParameter("lastName", sheet.getCompetitor().getLastName());
				query.setParameter("ipscDiv", sheet.getIpscDivision());
				query.setParameter("stageName", sheet.getStage().getName());
				query.setParameter("matchName", sheet.getStage().getMatch().getName());
				List<StageScoreSheet> dbSheets = query.getResultList();
				if (dbSheets.size() != 0) {
					removeScoreSheets.add(sheet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (StageScoreSheet removeSheet : removeScoreSheets) {
			sheets.remove(removeSheet);
		}
	}

	public static StageScoreSheet find(Long id, EntityManager entityManager) {
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.id = :id";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("id", id);
			return query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<StageScoreSheet> findAll() {
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			List<StageScoreSheet> sheets = em.createQuery("SELECT s from StageScoreSheet s", StageScoreSheet.class)
					.getResultList();
			emf.close();
			return sheets;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<StageScoreSheet> findAllForClassifier(IPSCDivision division, ClassifierStage classifier,
			EntityManager entityManager) {
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.ipscDivision = :division AND s.stage.classifierStage = :classifier";
			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("division", division);
			query.setParameter("classifier", classifier);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<StageScoreSheet> findClassifierStageResultsForCompetitor(Competitor competitor,
			IPSCDivision division, Set<ClassifierStage> classifierStages, EntityManager entityManager) {
		List<StageScoreSheet> scoreSheets = new ArrayList<StageScoreSheet>();

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor = :competitor AND s.stage.classifierStage IN :classifierStage "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.winMssDateString DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("classifierStage", classifierStages);
			query.setParameter("division", division);
			query.setParameter("competitor", competitor);
			query.setMaxResults(8);
			scoreSheets = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return scoreSheets;
	}

	public static List<StageScoreSheet> findClassifierStageResultsForCompetitor(String firstName, String lastName,
			IPSCDivision division, ClassifierStage classifierStage, EntityManager entityManager) {

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.stage.classifierStage = :classifierStage "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.winMssDateString DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("classifierStage", classifierStage);
			query.setParameter("division", division);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<StageScoreSheet> findCompetitorClassifierResults(Competitor competitor,
			ClassifierStage classifier, IPSCDivision division, EntityManager entityManager) {
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor = :competitor AND s.ipscDivision = :division "
					+ "AND s.stage.classifierStage = :classifier";
			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("competitor", competitor);
			query.setParameter("division", division);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<StageScoreSheet> findDivisionResultsForCompetitor(String firstName, String lastName,
			IPSCDivision division, EntityManager entityManager) {

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.ipscDivision = :division ORDER BY s.hitFactor DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("division", division);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<StageScoreSheet> findDivisionResultsForCompetitorOrderByDate(String firstName, String lastName,
			IPSCDivision division, EntityManager entityManager) {

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.ipscDivision = :division ORDER BY s.stage.match.winMssDateString DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("division", division);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static int getTotalStageScoreSheetCount() {
		int matchCount;
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			EntityManager em = emf.createEntityManager();
			matchCount = ((Long) em.createQuery("SELECT COUNT(s) from StageScoreSheet s").getSingleResult()).intValue();
			emf.close();
			return matchCount;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static void removeInBatch(List<Long> idList, EntityManager entityManager) {

		try {
			entityManager.createQuery("DELETE FROM StageScoreSheet s where s.id IN :idList")
					.setParameter("idList", idList).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StageScoreSheet save(StageScoreSheet sheet, EntityManager entityManager) {
		try {
			return entityManager.merge(sheet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
