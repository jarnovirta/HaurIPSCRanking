package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Match;

public interface MatchRepository {
	public Match find(Match match, EntityManager entityManager);

	public List<Match> getMatchListPage(int page, int pageSize, EntityManager entityManager);

	public void delete(Match match, EntityManager entityManager);

	public Match findNewestMatch(EntityManager entityManager);

	public List<Match> findAll(EntityManager entityManager);

	public int getMatchCount(EntityManager entityManager);

	public Match merge(Match match, EntityManager entityManager);

}
