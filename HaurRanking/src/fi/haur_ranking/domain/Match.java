package fi.haur_ranking.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="Match")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String winMssDateString; 
	@Transient
	private int winMssTypeFirearmId;
	private Long winMssMatchId;
	private String matchName;
	@OneToMany(cascade = CascadeType.ALL)
	private List<StageScoreSheet> stageScoreSheets;
	
	public Match() { }
	
	public Match(String matchName, Long winMssMatchId, String winMssDateString, int winMssTypeFirearmId) {
		this.matchName = matchName;
		this.winMssMatchId = winMssMatchId;
		this.winMssDateString = winMssDateString;
		this.winMssTypeFirearmId = winMssTypeFirearmId;
	}
	
	public Long getWinMssMatchId() {
		return winMssMatchId;
	}

	public void setWinMssMatchId(Long winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWinMssDateString() {
		return winMssDateString;
	}

	public void setWinMssDateString(String winMssDateString) {
		this.winMssDateString = winMssDateString;
	}

	public int getWinMssTypeFirearmId() {
		return winMssTypeFirearmId;
	}

	public void setWinMssTypeFirearmId(int winMssTypeFirearmId) {
		this.winMssTypeFirearmId = winMssTypeFirearmId;
	}
}
