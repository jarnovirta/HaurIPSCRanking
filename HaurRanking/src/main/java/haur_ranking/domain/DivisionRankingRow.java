package haur_ranking.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DivisionRankingRow")
public class DivisionRankingRow implements Comparable<DivisionRankingRow> {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn
	private DivisionRanking divisionRanking;

	@ManyToOne
	private Competitor competitor;
	private double resultPercentage;
	private double bestResultsAverage;
	private double hitFactorAverage;
	private boolean rankedCompetitor;
	private int resultsCount;

	private Integer previousRank;

	private boolean improvedResult = false;

	public DivisionRankingRow() {
	}

	public DivisionRankingRow(Competitor competitor, DivisionRanking divisionRanking, boolean rankedCompetitor,
			double bestResultsAverage, double bestHitFactorsAverage, int resultsCount) {
		this.divisionRanking = divisionRanking;
		this.competitor = competitor;
		this.rankedCompetitor = rankedCompetitor;
		this.bestResultsAverage = bestResultsAverage;
		this.hitFactorAverage = bestHitFactorsAverage;
		this.resultsCount = resultsCount;
	}

	public DivisionRankingRow(Competitor competitor, DivisionRanking divisionRanking, boolean rankedCompetitor,
			int resultsCount) {
		this.divisionRanking = divisionRanking;
		this.competitor = competitor;
		this.rankedCompetitor = rankedCompetitor;
		this.resultsCount = resultsCount;
	}

	public DivisionRankingRow(Competitor competitor, DivisionRanking divisionRanking, int resultPercentage,
			int resultsCount, Integer previousRank, boolean improvedResult) {
		this.divisionRanking = divisionRanking;
		this.competitor = competitor;
		this.resultsCount = resultsCount;
		this.previousRank = previousRank;
		this.improvedResult = improvedResult;
	}

	@Override
	public int compareTo(DivisionRankingRow compareToLine) {
		double compareToAverage = compareToLine.getBestResultsAverage();
		/* For Ascending order */
		if (this.bestResultsAverage < compareToAverage)
			return -1;
		if (this.bestResultsAverage > compareToAverage)
			return 1;
		if (competitor == null || compareToLine == null || compareToLine.getCompetitor() == null)
			return 0;
		int lastNameResult = compareToLine.getCompetitor().getLastName().compareTo(this.getCompetitor().getLastName());
		if (lastNameResult != 0)
			return lastNameResult;
		return compareToLine.getCompetitor().getFirstName().compareTo(this.getCompetitor().getLastName());

	}

	public double getHitFactorAverage() {
		return hitFactorAverage;
	}

	public double getBestResultsAverage() {
		return bestResultsAverage;
	}

	public Competitor getCompetitor() {
		return competitor;
	}

	public Long getId() {
		return id;
	}

	public int getPreviousRank() {
		return previousRank;
	}

	public double getResultPercentage() {
		return resultPercentage;
	}

	public int getResultsCount() {
		return resultsCount;
	}

	public boolean isImprovedResult() {
		return improvedResult;
	}

	public boolean isRankedCompetitor() {
		return rankedCompetitor;
	}

	public void setHitFactorAverage(double bestHitFactorsAverage) {
		this.hitFactorAverage = bestHitFactorsAverage;
	}

	public void setBestResultsAverage(double bestResultsAverage) {
		this.bestResultsAverage = bestResultsAverage;
	}

	public void setCompetitor(Competitor competitor) {
		this.competitor = competitor;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImprovedResult(boolean improvedResult) {
		this.improvedResult = improvedResult;
	}

	public void setPreviousRank(Integer previousRank) {
		this.previousRank = previousRank;
	}

	public void setRankedCompetitor(boolean rankedCompetitor) {
		this.rankedCompetitor = rankedCompetitor;
	}

	public void setResultPercentage(double resultPercentage) {
		this.resultPercentage = resultPercentage;
	}

	public void setResultsCount(int resultsCount) {
		this.resultsCount = resultsCount;
	}

	public DivisionRanking getDivisionRanking() {
		return divisionRanking;
	}

	public void setDivisionRanking(DivisionRanking divisionRanking) {
		this.divisionRanking = divisionRanking;
	}

}
