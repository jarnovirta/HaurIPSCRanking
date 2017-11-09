package fi.haur_ranking.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Match")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Long winMssMatchId;
	private String matchName;
	@OneToMany(cascade = CascadeType.ALL)
	private List<StageScoreSheet> stageScoreSheets;
	
	public Match() { }
	
	public Match(String matchName, Long winMssMatchId) {
		this.matchName = matchName;
		this.winMssMatchId = winMssMatchId;
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
}
