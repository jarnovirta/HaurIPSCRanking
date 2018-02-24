package haur_ranking.repository.haur_ranking_repository;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.exception.DatabaseException;

public interface StageScoreSheetRepository {
	public StageScoreSheet find(Long id, EntityManager entityManager);

	public List<StageScoreSheet> find(IPSCDivision division, ClassifierStage classifier, EntityManager entityManager);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			ClassifierStage classifierStage, EntityManager entityManager);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			EntityManager entityManager);

	public List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			Set<ClassifierStage> classifiers, EntityManager entityManager);

	public List<StageScoreSheet> find(Competitor competitor, EntityManager entityManager);

	public List<StageScoreSheet> findAll(EntityManager entityManager);

	public int getCount(EntityManager entityManager);

	public void removeInBatch(List<Long> idList, EntityManager entityManager) throws DatabaseException;

	public void persist(StageScoreSheet sheet, EntityManager entityManager) throws DatabaseException;

	public int getCompetitorStageScoreSheetCount(Competitor competitor, EntityManager entityManager);

	public List<Double> getCompetitorLatestScores(Competitor competitor, IPSCDivision division,
			EntityManager entityManager);

}
