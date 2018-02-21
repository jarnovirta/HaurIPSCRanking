package haur_ranking.domain;

import java.util.Calendar;
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
import javax.persistence.Transient;

@Entity
@Table(name = "Match")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	@Transient
	private Long winMssMatchId;
	private String name;
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Stage> stages;

	public Match() {
	}

	public Match(String matchName, Long winMssMatchId, Calendar date) {
		this.name = matchName;
		this.winMssMatchId = winMssMatchId;
		this.date = date;
	}

	public Match(String matchName) {
		this.name = matchName;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
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

	public Long getWinMssMatchId() {
		return winMssMatchId;
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

	public void setWinMssMatchId(Long winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
	}
}
