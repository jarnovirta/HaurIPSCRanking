package fi.haur_ranking.domain;

import java.util.List;

public class Match {
	private int winMssMatchId;
	private int ssiMatchId;
	private String matchName;
	private List<StageScoreSheet> stageScoreSheets;
	
	public int getWinMssMatchId() {
		return winMssMatchId;
	}
	public void setWinMssMatchId(int winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
	}
	public int getSsiMatchId() {
		return ssiMatchId;
	}
	public void setSsiMatchId(int ssiMatchId) {
		this.ssiMatchId = ssiMatchId;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public List<StageScoreSheet> getStageScoreSheets() {
		return stageScoreSheets;
	}
	public void setStageScoreSheets(List<StageScoreSheet> stageScoreSheets) {
		this.stageScoreSheets = stageScoreSheets;
	}
}
