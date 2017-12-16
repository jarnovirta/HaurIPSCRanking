package haur_ranking.domain;

public class DatabaseStatistics {
	private int matchCount;
	private int stageCount;
	private int stageScoreSheetCount;
	private int competitorCount;
	private int validClassifiersCount;

	public int getValidClassifiersCount() {
		return validClassifiersCount;
	}

	public void setValidClassifiersCount(int validClassifiersCount) {
		this.validClassifiersCount = validClassifiersCount;
	}

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

	public int getStageCount() {
		return stageCount;
	}

	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}

}
