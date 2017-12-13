package haur_ranking.repository.haur_ranking_repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.StageScoreSheet;

public class StageScoreSheetRepository {

	public static List<StageScoreSheet> find(IPSCDivision division, ClassifierStage classifier,
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

	public static List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			ClassifierStage classifierStage, EntityManager entityManager) {

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.stage.classifierStage = :classifierStage "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

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

	public static List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			EntityManager entityManager) {

		try {
			// KORJATTAVA ORDER BY DATE
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

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

	public static List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			Set<ClassifierStage> classifiers, EntityManager entityManager) {

		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.stage.classifierStage IN :classifierStages "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("classifierStages", EnumSet.copyOf(classifiers));
			query.setParameter("division", division);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<StageScoreSheet> findAll(EntityManager entityManager) {
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s from StageScoreSheet s";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			sheets = query.getResultList();
			return sheets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheets;
	}

	public static int getTotalStageScoreSheetCount(EntityManager entityManager) {
		int matchCount = -1;
		try {
			String queryString = "SELECT COUNT(s) from StageScoreSheet s";
			TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
			matchCount = query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchCount;
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

	public static int getCompetitorStageScoreSheetCount(Competitor competitor, EntityManager entityManager) {
		int resultCount = -1;
		try {
			String queryString = "SELECT COUNT(s) FROM StageScoreSheet s WHERE s.competitor = :competitor";
			TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("competitor", competitor);
			resultCount = query.getSingleResult().intValue();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return resultCount;
	}

	public static List<StageScoreSheet> findStageScoreSheetsForCompetitor(Competitor competitor,
			EntityManager entityManager) {
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s from StageScoreSheet s WHERE s.competitor = :competitor";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("competitor", competitor);
			sheets = query.getResultList();
			return sheets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheets;
	}
}
