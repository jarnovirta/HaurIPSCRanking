package haur_ranking.domain;

public class DatabaseStatistics {
	int matchCount;
	int stageCount;
	int competitorCount;

	public int getCompetitorCount() {
		return competitorCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public int getStageCount() {
		return stageCount;
	}

	public void setCompetitorCount(int competitorCount) {
		this.competitorCount = competitorCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}

}