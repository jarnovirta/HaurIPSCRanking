package fi.haur_ranking.repository.haur_ranking_repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;

public class StageRepository {
	// Returns a list of ClassifierStage for which there are at least two results in database.
	// Only ClassifierStages with at least two results are taken into account when generating a ranking.
	public static List<ClassifierStage> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		List<ClassifierStage> resultClassifierStages = new ArrayList<ClassifierStage>();
		EntityManagerFactory emf = null;
		EntityManager em = null;
		try {
			emf = Persistence.createEntityManagerFactory("fi.haur_ranking.jpa");
			em = emf.createEntityManager();
			for (ClassifierStage classifierStage : ClassifierStage.values()) {
				String queryString = "SELECT s FROM StageScoreSheet s WHERE s.stage.classifierStage = :classifierStage"
						+ " AND s.ipscDivision = :division";
				final TypedQuery<StageScoreSheet> query = em.createQuery(queryString, StageScoreSheet.class);
				query.setParameter("classifierStage", classifierStage);
				query.setParameter("division", division);
				
				List<StageScoreSheet> sheets = query.getResultList();
				if (sheets.size() >= 2) resultClassifierStages.add(classifierStage);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (em != null) em.close();
		if (emf != null) emf.close();
		return resultClassifierStages;
	}
}
