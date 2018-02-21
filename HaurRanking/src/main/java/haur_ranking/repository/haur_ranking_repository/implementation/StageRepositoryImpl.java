package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.StageRepository;

public class StageRepositoryImpl implements StageRepository {
	@Override
	public Stage find(Long id, EntityManager entityManager) {
		Stage stage = null;
		try {
			String queryString = "SELECT s FROM Stage s WHERE s.id = " + id;
			TypedQuery<Stage> query = entityManager.createQuery(queryString, Stage.class);
			stage = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stage;
	}

	@Override
	public int getCount(EntityManager entityManager) {
		int stageCount = -1;
		try {
			stageCount = ((Long) entityManager.createQuery("SELECT COUNT(s) from Stage s").getSingleResult())
					.intValue();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return stageCount;
	}

	@Override
	public void delete(Stage stage, EntityManager entityManager) {
		try {
			if (!entityManager.contains(stage)) {
				stage = entityManager.merge(stage);
			}
			entityManager.remove(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Stage find(Stage stage, EntityManager entityManager) {
		Stage resultStage = null;
		try {
			String queryString = "SELECT s FROM Stage s WHERE s.name = :stageName AND s.match.name = :matchName and s.match.date = :matchDate";

			TypedQuery<Stage> query = entityManager.createQuery(queryString, Stage.class);
			query.setParameter("stageName", stage.getName());
			query.setParameter("matchName", stage.getMatch().getName());
			query.setParameter("matchDate", stage.getMatch().getDate());
			List<Stage> stages = query.getResultList();
			if (stages != null && stages.size() > 0)
				resultStage = stages.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStage;
	}

	// Returns a list of ClassifierStage for which there are at least two
	// results in database.
	// Only ClassifierStages with at least two results are taken into account
	// when generating a ranking.
	@Override
	public Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division,
			EntityManager entityManager) {
		Map<ClassifierStage, Double> resultClassifierStages = new HashMap<ClassifierStage, Double>();

		try {
			for (ClassifierStage classifierStage : ClassifierStage.values()) {
				String queryString = "SELECT s.hitFactor FROM StageScoreSheet s WHERE s.stage.classifierStage = :classifierStage"
						+ " AND s.ipscDivision = :division ORDER BY s.hitFactor DESC";
				final TypedQuery<Double> query = entityManager.createQuery(queryString, Double.class);
				query.setParameter("classifierStage", classifierStage);
				query.setParameter("division", division);
				query.setMaxResults(2);
				List<Double> topTwoHitFactors = query.getResultList();

				if (topTwoHitFactors.size() == 2) {
					// Calculate average of two best hit factors
					double averageOfTwoBestResults = (topTwoHitFactors.get(0) + topTwoHitFactors.get(1)) / 2;
					resultClassifierStages.put(classifierStage, averageOfTwoBestResults);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultClassifierStages;
	}
}
