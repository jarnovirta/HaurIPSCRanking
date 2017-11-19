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
	Competitor competitor;
	int resultPercentage;
	double bestResultsAverage;
	int resultsCount;

	Integer previousRank;

	boolean improvedResult = false;

	public DivisionRankingLine() {
	}

	public DivisionRankingLine(Competitor competitor, double bestResultsAverage) {
		this.competitor = competitor;
		this.bestResultsAverage = bestResultsAverage;
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
		return 0;

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

	public int getResultPercentage() {
		return resultPercentage;
	}

	public int getResultsCount() {
		return resultsCount;
	}

	public boolean isImprovedResult() {
		return improvedResult;
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

	public void setResultPercentage(int resultPercentage) {
		this.resultPercentage = resultPercentage;
	}

	public void setResultsCount(int resultsCount) {
		this.resultsCount = resultsCount;
	}

}
