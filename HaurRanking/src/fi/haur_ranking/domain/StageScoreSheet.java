package fi.haur_ranking.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="StageScoreSheet")
public class StageScoreSheet {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Competitor competitor;
	private String ssiModel = "match_ipsc.ipscscorecard";
	private Long winMssPrimaryKey;
	private Long winMssStageId;
	private Long winMssMemberId;
	private int aHits;
	private int bHits;
	private int cHits;
	private int dHits;
	private int misses = 0;
	private int penalties = 0;
	private int deductedPoints = 0;
	private int specialPenalty;
	private double time;
	private String timeString;
	private boolean disqualified;
	private double hitfactor;
	private IPSCDivision ipscDivision;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedInWinMSSDatabase;
	@Transient
	private String lastModifiedInWinMSSDatabaseString;
	
	private int procedurals;
	boolean scoresZeroedForStage = false;
	private String comment = "";
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSsiModel() {
		return ssiModel;
	}
	public void setSsiModel(String ssiModel) {
		this.ssiModel = ssiModel;
	}
	public Long getWinMssPrimaryKey() {
		return winMssPrimaryKey;
	}
	public void setWinMssPrimaryKey(Long winMssPrimaryKey) {
		this.winMssPrimaryKey = winMssPrimaryKey;
	}
	public Long getWinMssStageId() {
		return winMssStageId;
	}
	public void setWinMssStageId(Long winMssStageId) {
		this.winMssStageId = winMssStageId;
	}
	public Long getWinMssMemberId() {
		return winMssMemberId;
	}
	public void setWinMssMemberId(Long winMssMemberId) {
		this.winMssMemberId = winMssMemberId;
	}
	public int getaHits() {
		return aHits;
	}
	public void setaHits(int aHits) {
		this.aHits = aHits;
	}
	public int getbHits() {
		return bHits;
	}
	public void setbHits(int bHits) {
		this.bHits = bHits;
	}
	public int getcHits() {
		return cHits;
	}
	public void setcHits(int cHits) {
		this.cHits = cHits;
	}
	public int getdHits() {
		return dHits;
	}
	public void setdHits(int dHits) {
		this.dHits = dHits;
	}
	public int getMisses() {
		return misses;
	}
	public void setMisses(int misses) {
		this.misses = misses;
	}
	public int getPenalties() {
		return penalties;
	}
	public void setPenalties(int penalties) {
		this.penalties = penalties;
	}
	public int getDeductedPoints() {
		return deductedPoints;
	}
	public void setDeductedPoints(int deductedPoints) {
		this.deductedPoints = deductedPoints;
	}
	public int getSpecialPenalty() {
		return specialPenalty;
	}
	public void setSpecialPenalty(int specialPenalty) {
		this.specialPenalty = specialPenalty;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	public int getProcedurals() {
		return procedurals;
	}
	public void setProcedurals(int procedurals) {
		this.procedurals = procedurals;
	}
	public boolean isScoresZeroedForStage() {
		return scoresZeroedForStage;
	}
	public void setScoresZeroedForStage(boolean scoresZeroedForStage) {
		this.scoresZeroedForStage = scoresZeroedForStage;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Competitor getCompetitor() {
		return competitor;
	}
	public void setCompetitor(Competitor competitor) {
		this.competitor = competitor;
	}
	public IPSCDivision getIpscDivision() {
		return ipscDivision;
	}
	public void setIpscDivision(IPSCDivision ipscDivision) {
		this.ipscDivision = ipscDivision;
	}
	public Date getLastModifiedInWinMSSDatabase() {
		return lastModifiedInWinMSSDatabase;
	}
	public void setLastModifiedInWinMSSDatabase(Date lastModifiedInWinMSSDatabase) {
		this.lastModifiedInWinMSSDatabase = lastModifiedInWinMSSDatabase;
	}
	public String getLastModifiedInWinMSSDatabaseString() {
		return lastModifiedInWinMSSDatabaseString;
	}
	public void setLastModifiedInWinMSSDatabaseString(String lastModifiedInWinMSSDatabaseString) {
		this.lastModifiedInWinMSSDatabaseString = lastModifiedInWinMSSDatabaseString;
	}
	public double getHitfactor() {
		return hitfactor;
	}
	public void setHitfactor(double hitfactor) {
		this.hitfactor = hitfactor;
	}
	public boolean isDisqualified() {
		return disqualified;
	}
	public void setDisqualified(boolean disqualified) {
		this.disqualified = disqualified;
	}

	
}
