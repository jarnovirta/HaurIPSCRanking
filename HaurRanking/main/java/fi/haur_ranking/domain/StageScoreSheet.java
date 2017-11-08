package fi.haur_ranking.domain;

public class StageScoreSheet {
	private String ssiModel = "match_ipsc.ipscscorecard";
	private int winMssPrimaryKey;
	private int winMssStageId;
	private int winMssMemberId;
	private int aHits;
	private int bHits;
	private int cHits;
	private int dHits;
	private int misses = 0;
	private int penalties = 0;
	private int deductedPoints = 0;
	private int specialPenalty;
	private double time;
	private String timeString;
	
	private int procedurals;
	boolean scoresZeroedForStage = false;
	private String comment = "";
	public int getWinMssStageId() {
		return winMssStageId;
	}

	public void setWinMssStageId(int winMssStageId) {
		this.winMssStageId = winMssStageId;
	}

	public int getaHits() {
		return aHits;
	}

	public void setaHits(int aHits) {
		this.aHits = aHits;
	}

	public int getbHits() {
		return bHits;
	}

	public void setbHits(int bHits) {
		this.bHits = bHits;
	}

	public int getcHits() {
		return cHits;
	}

	public void setcHits(int cHits) {
		this.cHits = cHits;
	}

	public int getdHits() {
		return dHits;
	}

	public void setdHits(int dHits) {
		this.dHits = dHits;
	}

	public int getMisses() {
		return misses;
	}

	public void setMisses(int misses) {
		deductedPoints -= this.misses * 10;		
		this.misses = misses;
		deductedPoints += misses * 10;
	}

	public int getPenalties() {
		return penalties;
	}

	public void setPenalties(int penalties) {
		deductedPoints -= this.penalties * 10;
		this.penalties = penalties;
		deductedPoints += penalties * 10;
	}

	public int getProcedurals() {
		return procedurals;
	}

	public void setProcedurals(int procedurals) {
		this.procedurals = procedurals;
	}

	public int getWinMssPrimaryKey() {
		return winMssPrimaryKey;
	}

	public void setWinMssPrimaryKey(int winMssPrimaryKey) {
		this.winMssPrimaryKey = winMssPrimaryKey;
	}

	public int getSpecialPenalty() {
		return specialPenalty;
	}

	public void setSpecialPenalty(int specialPenalty) {
		this.specialPenalty = specialPenalty;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isScoresZeroedForStage() {
		return scoresZeroedForStage;
	}

	public void setScoresZeroedForStage(boolean scoresZeroedForStage) {
		this.scoresZeroedForStage = scoresZeroedForStage;
	}

	public int getDeductedPoints() {
		return deductedPoints;
	}

	public void setDeductedPoints(int deductedPoints) {
		this.deductedPoints = deductedPoints;
	}

	public String getSsiModel() {
		return ssiModel;
	}

	public void setSsiModel(String ssiModel) {
		this.ssiModel = ssiModel;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
		this.timeString = Double.toString(time);
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public int getWinMssMemberId() {
		return winMssMemberId;
	}

	public void setWinMssMemberId(int winMssMemberId) {
		this.winMssMemberId = winMssMemberId;
	}
}
