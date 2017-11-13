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
	private Long winMssMatchId;
	private String name;
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
	private List<Stage> stages;
	
	public Match() { }
	
	public Match(String matchName, Long winMssMatchId, String winMssDateString) {
		this.name = matchName;
		this.winMssMatchId = winMssMatchId;
		this.winMssDateString = winMssDateString;
		
	}
	
	public Long getWinMssMatchId() {
		return winMssMatchId;
	}

	public void setWinMssMatchId(Long winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Stage> getStages() {
		return stages;
	}

	public void setStages(List<Stage> stages) {
		this.stages = stages;
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


}
