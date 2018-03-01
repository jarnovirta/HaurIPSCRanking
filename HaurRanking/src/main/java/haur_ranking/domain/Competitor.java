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
	private String firstName;
	private String lastName;

	private int winMSSTypeDisqualifyRuleId;
	private int winMSSTypeDisqualificationRuleId;

	@Transient
	private int resultCount;

	public Competitor() {
	}

	public Competitor(String firstName, String lastName) {
		String[] firstNameArray = firstName.split(" ");
		if (firstNameArray.length > 1) {
			firstName = firstNameArray[0];
		}
		this.firstName = firstName;
		this.lastName = lastName;

		// A hack for getting around inconsistency in WinMSS db data
		if (this.lastName.equals("Välimäki") && this.firstName.equals("Juha"))
			this.firstName = "Juha-Pekka";
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

		if (other.getFirstName().equals(this.firstName) && other.getLastName().equals(this.lastName))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 53 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		return hash;
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public int getWinMssTypeDisqualificationRuleId() {
		return winMSSTypeDisqualificationRuleId;
	}

	public int getWinMssTypeDisqualifyRuleId() {
		return winMSSTypeDisqualifyRuleId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setWinMssTypeDisqualificationRuleId(int winMssTypeDisqualificationRuleId) {
		this.winMSSTypeDisqualificationRuleId = winMssTypeDisqualificationRuleId;
	}

	public void setWinMssTypeDisqualifyRuleId(int winMssTypeDisqualifyRuleId) {
		this.winMSSTypeDisqualifyRuleId = winMssTypeDisqualifyRuleId;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

}
