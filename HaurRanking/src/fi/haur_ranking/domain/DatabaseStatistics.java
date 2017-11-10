package fi.haur_ranking.domain;

public class DatabaseStatistics {
	int matchCount;
	int stageCount;
	int competitorCount;
	public int getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	public int getStageCount() {
		return stageCount;
	}
	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}
	public int getCompetitorCount() {
		return competitorCount;
	}
	public void setCompetitorCount(int competitorCount) {
		this.competitorCount = competitorCount;
	}

}
