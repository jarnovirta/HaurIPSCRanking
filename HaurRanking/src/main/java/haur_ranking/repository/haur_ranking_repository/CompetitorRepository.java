package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;

public interface CompetitorRepository {
	public Competitor find(String firstName, String lastName, String winMSSComment, EntityManager entityManager);

	public List<Competitor> findByLastName(String lastName, EntityManager entityManager);

	public List<Competitor> getCompetitorListPage(int page, int pageSize, EntityManager entityManager);

	public List<Competitor> findAll(EntityManager entityManager);

	public int getTotalCompetitorCount(EntityManager entityManager);

	public Competitor persist(Competitor competitor);

	public void delete(Competitor competitor, EntityManager entityManager);

}
