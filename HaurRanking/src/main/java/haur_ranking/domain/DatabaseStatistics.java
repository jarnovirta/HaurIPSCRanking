package haur_ranking.domain;

public class DatabaseStatistics {
	private int matchCount;
	private int stageScoreSheetCount;
	private int competitorCount;

	public int getCompetitorCount() {
		return competitorCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setCompetitorCount(int competitorCount) {
		this.competitorCount = competitorCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public int getStageScoreSheetCount() {
		return stageScoreSheetCount;
	}

	public void setStageScoreSheetCount(int stageScoreSheetCount) {
		this.stageScoreSheetCount = stageScoreSheetCount;
	}

}
