package fi.haur_ranking.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Stage")
public class Stage {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Long winMssId;
	
	private String name;
	private Long winMssMatchId;
	
	private Match match;
	private List<StageScoreSheet> stageScoreSheets;
	
	private ClassifierStage classifierStage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getWinMssId() {
		return winMssId;
	}
	public void setWinMssId(Long winMssId) {
		this.winMssId = winMssId;
	}
	public Long getWinMssMatchId() {
		return winMssMatchId;
	}
	public void setWinMssMatchId(Long winMssMatchId) {
		this.winMssMatchId = winMssMatchId;
	}
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public List<StageScoreSheet> getStageScoreSheets() {
		return stageScoreSheets;
	}
	public void setStageScoreSheets(List<StageScoreSheet> stageScoreSheets) {
		this.stageScoreSheets = stageScoreSheets;
	}
	public ClassifierStage getClassifierStage() {
		return classifierStage;
	}
	public void setClassifierStage(ClassifierStage classifierStage) {
		this.classifierStage = classifierStage;
	}
}	
