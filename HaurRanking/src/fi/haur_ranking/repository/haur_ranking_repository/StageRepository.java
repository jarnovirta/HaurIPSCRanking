package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.StageScoreSheet;

public class StageRepository {
	// Returns a list of ClassifierStage for which there are at least two results in database.
	// Only ClassifierStages with at least two results are taken into account when generating a ranking.
	public static Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division, EntityManager entityManager) {
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return resultClassifierStages;
	}
}