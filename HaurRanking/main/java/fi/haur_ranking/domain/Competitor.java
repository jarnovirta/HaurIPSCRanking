package fi.haur_ranking.domain;


public class Competitor {

	private int ssiPrimaryKey;
	private int winMssCompetitorId;
	private int winMssMemberId;
	private String firstName;
	private String lastName;
	private String ICS;
	private String email;
	private String ssiDisqualificationReason = "no";
	private int winMssTypeDisqualifyRuleId;
	private int winMssTypeDisqualificationRuleId;
	public Competitor() { }
	
	public Competitor(String firstName, String lastName, String ICS) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ICS = ICS; 
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getICS() {
		return ICS;
	}

	public void setICS(String iCS) {
		ICS = iCS;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSsiPrimaryKey() {
		return ssiPrimaryKey;
	}

	public void setSsiPrimaryKey(int ssiPrimaryKey) {
		this.ssiPrimaryKey = ssiPrimaryKey;
	}

	public String getSsiDisqualificationReason() {
		return ssiDisqualificationReason;
	}

	public void setSsiDisqualificationReason(String ssiDisqualificationReason) {
		this.ssiDisqualificationReason = ssiDisqualificationReason;
	}
	
	public int getWinMssTypeDisqualifyRuleId() {
		return winMssTypeDisqualifyRuleId;
	}

	public void setWinMssTypeDisqualifyRuleId(int winMssDisqualificationReason) {
		this.winMssTypeDisqualifyRuleId = winMssDisqualificationReason;
		if (winMssDisqualificationReason == 0) ssiDisqualificationReason = "no";
		if (winMssDisqualificationReason >= 1 && winMssDisqualificationReason <= 3) ssiDisqualificationReason = "ad";
		if (winMssDisqualificationReason >= 4 && winMssDisqualificationReason <= 16) ssiDisqualificationReason = "ug";
	}

	public int getWinMssTypeDisqualificationRuleId() {
		return winMssTypeDisqualificationRuleId;
	}

	public void setWinMssTypeDisqualificationRuleId(int winMssTypeDisqualificationRuleId) {
		this.winMssTypeDisqualificationRuleId = winMssTypeDisqualificationRuleId;
		if (winMssTypeDisqualificationRuleId == 2) ssiDisqualificationReason = "uc";
		if (winMssTypeDisqualificationRuleId == 3) ssiDisqualificationReason = "pc";
		if (winMssTypeDisqualificationRuleId == 4) ssiDisqualificationReason = "gr";
	}

	public int getWinMssCompetitorId() {
		return winMssCompetitorId;
	}

	public void setWinMssCompetitorId(int winMssCompetitorId) {
		this.winMssCompetitorId = winMssCompetitorId;
	}

	public int getWinMssMemberId() {
		return winMssMemberId;
	}

	public void setWinMssMemberId(int winMssMemberId) {
		this.winMssMemberId = winMssMemberId;
	}
}
