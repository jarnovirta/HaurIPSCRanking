package haur_ranking.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Match")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String winMssDateString;
	private Long winMssMatchId;
	private String name;
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Stage> stages;

	public Match() {
	}

	public Match(String matchName, Long winMssMatchId, String winMssDateString) {
		this.name = matchName;
		this.winMssMatchId = winMssMatchId;
		this.winMssDateString = winMssDateString;

	}

	public Match(String matchName, String winMssDateString) {
		this.name = matchName;
		this.winMssDateString = winMssDateString;
	}

	public Date getDate() {
		return date;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Stage> getStages() {
		return stages;
	}

	public String getWinMssDateString() {
		return winMssDateString;
	}

	public Long getWinMssMatchId() {
		return winMssMatchId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStages(List<Stage> stages) {
		this.stages = stages;
	}

	public void setWinMssDateString(String winMssDateString) {
		this.winMssDateString = winMssDateString;
	}

	public void setWinMssMatchId(Long winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
	}

}
