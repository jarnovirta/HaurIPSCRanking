package haur_ranking.repository.winmss_repository;

import java.util.List;
import java.util.Map;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;

public interface WinMSSStageRepository {
	public List<Stage> findStagesForMatch(Match match);

	public Map<ClassifierStage, Stage> getValidClassifiers();

}
