package haur_ranking.repository.haur_ranking_repository;

import java.util.Map;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;

public interface StageRepository {
	public Stage find(Long id);

	public int getCount();

	public void delete(Stage stage);

	public Stage find(Stage stage);

	public Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division);
}
