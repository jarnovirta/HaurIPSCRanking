package fi.haur_ranking.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Competitor")
public class Competitor {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Long ssiPrimaryKey;
	private Long winMssCompetitorId;
	private Long winMssMemberId;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSsiPrimaryKey() {
		return ssiPrimaryKey;
	}

	public void setSsiPrimaryKey(Long ssiPrimaryKey) {
		this.ssiPrimaryKey = ssiPrimaryKey;
	}

	public Long getWinMssCompetitorId() {
		return winMssCompetitorId;
	}

	public void setWinMssCompetitorId(Long winMssCompetitorId) {
		this.winMssCompetitorId = winMssCompetitorId;
	}

	public Long getWinMssMemberId() {
		return winMssMemberId;
	}

	public void setWinMssMemberId(Long winMssMemberId) {
		this.winMssMemberId = winMssMemberId;
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

	public String getSsiDisqualificationReason() {
		return ssiDisqualificationReason;
	}

	public void setSsiDisqualificationReason(String ssiDisqualificationReason) {
		this.ssiDisqualificationReason = ssiDisqualificationReason;
	}

	public int getWinMssTypeDisqualifyRuleId() {
		return winMssTypeDisqualifyRuleId;
	}

	public void setWinMssTypeDisqualifyRuleId(int winMssTypeDisqualifyRuleId) {
		this.winMssTypeDisqualifyRuleId = winMssTypeDisqualifyRuleId;
	}

	public int getWinMssTypeDisqualificationRuleId() {
		return winMssTypeDisqualificationRuleId;
	}

	public void setWinMssTypeDisqualificationRuleId(int winMssTypeDisqualificationRuleId) {
		this.winMssTypeDisqualificationRuleId = winMssTypeDisqualificationRuleId;
	}

}
