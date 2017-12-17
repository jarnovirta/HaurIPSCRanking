package haur_ranking.repository.winmss_repository;

import java.util.List;

import haur_ranking.domain.Match;

public interface WinMSSMatchRepository {
	public List<Match> findAll(String fileLocation);
}
