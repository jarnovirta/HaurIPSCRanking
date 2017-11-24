package haur_ranking.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DivisionRankingLine")
public class DivisionRankingLine implements Comparable<DivisionRankingLine> {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	private Competitor competitor;
	private double resultPercentage;
	private double bestResultsAverage;
	private double bestHitFactorsAverage;
	private boolean rankedCompetitor;
	private int resultsCount;

	private Integer previousRank;

	private boolean improvedResult = false;

	public DivisionRankingLine() {
	}

	public DivisionRankingLine(Competitor competitor, boolean rankedCompetitor, double bestResultsAverage,
			double bestHitFactorsAverage, int resultsCount) {
		this.competitor = competitor;
		this.rankedCompetitor = rankedCompetitor;
		this.bestResultsAverage = bestResultsAverage;
		this.bestHitFactorsAverage = bestHitFactorsAverage;
		this.resultsCount = resultsCount;
	}

	public DivisionRankingLine(Competitor competitor, boolean rankedCompetitor, int resultsCount) {
		this.competitor = competitor;
		this.rankedCompetitor = rankedCompetitor;
		this.resultsCount = resultsCount;
	}

	public DivisionRankingLine(Competitor competitor, int resultPercentage, int resultsCount, Integer previousRank,
			boolean improvedResult) {
		this.competitor = competitor;
		this.resultsCount = resultsCount;
		this.previousRank = previousRank;
		this.improvedResult = improvedResult;
	}

	@Override
	public int compareTo(DivisionRankingLine compareToLine) {
		double compareToAverage = compareToLine.getBestResultsAverage();
		/* For Ascending order */
		if (this.bestResultsAverage < compareToAverage)
			return -1;
		if (this.bestResultsAverage > compareToAverage)
			return 1;
		int compareToResultsCount = compareToLine.getResultsCount();
		if (this.resultsCount < compareToResultsCount)
			return -1;
		if (this.resultsCount > compareToResultsCount)
			return 1;
		return 0;

	}

	public double getBestHitFactorsAverage() {
		return bestHitFactorsAverage;
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

	public void setBestHitFactorsAverage(double bestHitFactorsAverage) {
		this.bestHitFactorsAverage = bestHitFactorsAverage;
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

}
