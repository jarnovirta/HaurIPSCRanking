package haur_ranking.repository.haur_ranking_repository;

import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;
import haur_ranking.exception.DatabaseException;

public interface StageRepository {
	public Stage find(Long id, EntityManager entityManager);

	public int getCount(EntityManager entityManager);

	public void delete(Stage stage, EntityManager entityManager) throws DatabaseException;

	public Stage find(Stage stage, EntityManager entityManager);

	public Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division,
			EntityManager entityManager);
}
