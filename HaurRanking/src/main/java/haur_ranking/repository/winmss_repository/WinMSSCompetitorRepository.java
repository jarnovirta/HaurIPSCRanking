package haur_ranking.repository.winmss_repository;

import haur_ranking.domain.Competitor;

public interface WinMSSCompetitorRepository {
	public Competitor findCompetitor(Long winMSSMemberId, Long matchId);

	public boolean isDisqualified(Long winMssMemberId, Long matchId);

}
