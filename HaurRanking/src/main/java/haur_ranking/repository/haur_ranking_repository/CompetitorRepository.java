package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.exception.DatabaseException;

public interface CompetitorRepository {
	public Competitor find(String firstName, String lastName, EntityManager entityManager) throws DatabaseException;

	public List<Competitor> getCompetitorListPage(int page, int pageSize, EntityManager entityManager)
			throws DatabaseException;

	public List<Competitor> findAll(EntityManager entityManager) throws DatabaseException;

	public int getTotalCompetitorCount(EntityManager entityManager) throws DatabaseException;

	public void persist(Competitor competitor, EntityManager entityManager) throws DatabaseException;

	public void delete(Competitor competitor, EntityManager entityManager) throws DatabaseException;

}
