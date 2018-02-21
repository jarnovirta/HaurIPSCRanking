package haur_ranking.repository.haur_ranking_repository;

import java.util.List;
import java.util.Set;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.StageScoreSheet;

public interface StageScoreSheetRepository {
	public StageScoreSheet find(Long id);

	public List<StageScoreSheet> find(IPSCDivision division, ClassifierStage classifier);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			ClassifierStage classifierStage);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			Set<ClassifierStage> classifiers);

	public List<StageScoreSheet> find(Competitor competitor);

	public List<StageScoreSheet> findAll();

	public int getCount();

	public void removeInBatch(List<Long> idList);

	public void save(StageScoreSheet sheet);

	public int getCompetitorStageScoreSheetCount(Competitor competitor);

	public List<Double> getCompetitorLatestScores(Competitor competitor, IPSCDivision division);

}
