package haur_ranking.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Competitor")
public class Competitor {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Long ssiPrimaryKey;
	private Long winMSSCompetitorId;
	private Long winMSSMemberId;
	private String firstName;

	private String lastName;
	// winMSSComment is Comment column in WinMSSTable tblMember.
	// It commonly holds the competitor's middle initial.
	private String winMSSComment;
	private String ICS;
	private String email;
	private String ssiDisqualificationReason = "no";
	private int winMSSTypeDisqualifyRuleId;
	private int winMSSTypeDisqualificationRuleId;

	@Transient
	private int resultCount;

	public Competitor() {
	}

	public Competitor(String firstName, String lastName, String winMSSComment) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this)
			return true;
		if (!(obj instanceof Competitor))
			return false;

		final Competitor other = (Competitor) obj;
		if (firstName.equals("Heli")) {
			System.out.println(firstName + " " + lastName + " " + winMSSComment);
			if (winMSSComment != null)
				System.out.println(winMSSComment + winMSSComment.length());
		}
		if (other.getFirstName().equals("Heli")) {
			System.out.println(other.getFirstName() + " " + other.getFirstName() + " " + other.getFirstName());
			if (winMSSComment != null)
				System.out.println(winMSSComment + winMSSComment.length());
		}
		boolean commentsEqual;
		if (other.getWinMSSComment() != null && other.getWinMSSComment().equals(this.winMSSComment)) {
			commentsEqual = true;
		} else {
			if (other.getWinMSSComment() == null && this.winMSSComment == null) {
				commentsEqual = true;
			} else {
				commentsEqual = false;
			}
		}
		if (other.getFirstName().equals(this.firstName) && other.getLastName().equals(this.lastName) && commentsEqual)
			return true;
		else
			return false;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getICS() {
		return ICS;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSsiDisqualificationReason() {
		return ssiDisqualificationReason;
	}

	public Long getSsiPrimaryKey() {
		return ssiPrimaryKey;
	}

	public Long getWinMssCompetitorId() {
		return winMSSCompetitorId;
	}

	public Long getWinMssMemberId() {
		return winMSSMemberId;
	}

	public int getWinMssTypeDisqualificationRuleId() {
		return winMSSTypeDisqualificationRuleId;
	}

	public int getWinMssTypeDisqualifyRuleId() {
		return winMSSTypeDisqualifyRuleId;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 53 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		hash = 53 * hash + (this.winMSSComment != null ? this.winMSSComment.hashCode() : 0);
		return hash;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setICS(String iCS) {
		ICS = iCS;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSsiDisqualificationReason(String ssiDisqualificationReason) {
		this.ssiDisqualificationReason = ssiDisqualificationReason;
	}

	public void setSsiPrimaryKey(Long ssiPrimaryKey) {
		this.ssiPrimaryKey = ssiPrimaryKey;
	}

	public void setWinMssCompetitorId(Long winMssCompetitorId) {
		this.winMSSCompetitorId = winMssCompetitorId;
	}

	public void setWinMssMemberId(Long winMssMemberId) {
		this.winMSSMemberId = winMssMemberId;
	}

	public void setWinMssTypeDisqualificationRuleId(int winMssTypeDisqualificationRuleId) {
		this.winMSSTypeDisqualificationRuleId = winMssTypeDisqualificationRuleId;
	}

	public void setWinMssTypeDisqualifyRuleId(int winMssTypeDisqualifyRuleId) {
		this.winMSSTypeDisqualifyRuleId = winMssTypeDisqualifyRuleId;
	}

	public String getWinMSSComment() {
		return winMSSComment;
	}

	public void setWinMSSComment(String winMSSComment) {
		this.winMSSComment = winMSSComment;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

}
