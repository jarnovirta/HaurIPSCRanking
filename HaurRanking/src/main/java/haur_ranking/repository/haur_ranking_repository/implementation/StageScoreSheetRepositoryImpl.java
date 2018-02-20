package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class StageScoreSheetRepositoryImpl implements StageScoreSheetRepository {

	@Override
	public List<StageScoreSheet> find(IPSCDivision division, ClassifierStage classifier) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.ipscDivision = :division AND s.stage.classifierStage = :classifier";
			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("division", division);
			query.setParameter("classifier", classifier);
			sheets = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return sheets;
	}

	@Override
	public StageScoreSheet find(Long id) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		StageScoreSheet sheet = null;
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.id = :id";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("id", id);
			sheet = query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheet;
	}

	@Override
	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			ClassifierStage classifierStage) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.stage.classifierStage = :classifierStage "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("classifierStage", classifierStage);
			query.setParameter("division", division);
			sheets = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;
	}

	@Override
	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("division", division);
			sheets = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;

	}

	@Override
	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			Set<ClassifierStage> classifiers) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s FROM StageScoreSheet s WHERE s.competitor.firstName = :firstName "
					+ "AND s.competitor.lastName = :lastName AND s.stage.classifierStage IN :classifierStages "
					+ "AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";

			final TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("firstName", firstName);
			query.setParameter("lastName", lastName);
			query.setParameter("classifierStages", EnumSet.copyOf(classifiers));
			query.setParameter("division", division);
			sheets = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;
	}

	@Override
	public List<StageScoreSheet> find(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s from StageScoreSheet s WHERE s.competitor = :competitor";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			query.setParameter("competitor", competitor);
			sheets = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;
	}

	@Override
	public List<StageScoreSheet> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = null;
		try {
			String queryString = "SELECT s from StageScoreSheet s";
			TypedQuery<StageScoreSheet> query = entityManager.createQuery(queryString, StageScoreSheet.class);
			sheets = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;
	}

	@Override
	public int getCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int matchCount = -1;
		try {
			String queryString = "SELECT COUNT(s) from StageScoreSheet s";
			TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
			matchCount = query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return matchCount;
	}

	@Override
	public void removeInBatch(List<Long> idList) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.createQuery("DELETE FROM StageScoreSheet s where s.id IN :idList")
					.setParameter("idList", idList).executeUpdate();
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}

	@Override
	public StageScoreSheet save(StageScoreSheet sheet) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		StageScoreSheet savedSheet = null;
		try {
			savedSheet = entityManager.merge(sheet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedSheet;
	}

	@Override
	public int getCompetitorStageScoreSheetCount(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int resultCount = -1;
		try {
			String queryString = "SELECT COUNT(s) FROM StageScoreSheet s WHERE s.competitor = :competitor";
			TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("competitor", competitor);
			resultCount = query.getSingleResult().intValue();

		} catch (Exception e) {
			e.printStackTrace();

		}
		entityManager.close();
		return resultCount;
	}

	@Override
	public List<Double> getCompetitorLatestScores(Competitor competitor, IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Double> hitFactorAverage;
		try {
			String queryString = "SELECT s.hitFactor FROM StageScoreSheet s WHERE s.competitor = :competitor"
					+ " AND s.ipscDivision = :division ORDER BY s.stage.match.date DESC";
			TypedQuery<Double> query = entityManager.createQuery(queryString, Double.class);
			query.setParameter("competitor", competitor);
			query.setParameter("division", division);
			query.setMaxResults(8);
			hitFactorAverage = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		entityManager.close();
		return hitFactorAverage;
	}

}
