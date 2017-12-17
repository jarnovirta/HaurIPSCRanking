package haur_ranking.repository.winmss_repository;

import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;

public interface WinMSSStageScoreSheetRepository {
	public int getScoreSheetCountForStage(Match match, Stage stage);

	public List<StageScoreSheet> find(Match match, Stage stage);
}
