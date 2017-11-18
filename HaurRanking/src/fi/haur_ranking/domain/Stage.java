package fi.haur_ranking.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private Match match;
	
	@OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
	private List<StageScoreSheet> stageScoreSheets;
	
	@Enumerated(EnumType.STRING)
	private ClassifierStage classifierStage;
	
	public Stage() { }
	
	public Stage(Match match, String name, ClassifierStage classifierStage) {
		this.match = match;
		this.name = name;
		this.classifierStage = classifierStage;
	}
	
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
