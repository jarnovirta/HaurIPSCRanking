package haur_ranking.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Ranking")
public class Ranking {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DivisionRanking> divisionRankings = new ArrayList<DivisionRanking>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private int totalResultsCount;
	private int competitorsWithRank;
	private int validClassifiersCount;

	public Ranking() {
		date = new Date();
	}

	public void setTotalCompetitorsAndResultsCounts() {
		totalResultsCount = 0;
		validClassifiersCount = 0;
		competitorsWithRank = 0;
		Set<ClassifierStage> classifiers = new HashSet<ClassifierStage>();
		Set<Competitor> competitors = new HashSet<Competitor>();
		for (DivisionRanking divisionRanking : divisionRankings) {
			for (ClassifierStage classifier : divisionRanking.getValidClassifiers()) {
				if (!classifiers.contains(classifier))
					classifiers.add(classifier);
			}
			for (DivisionRankingLine line : divisionRanking.getRankingLines()) {
				totalResultsCount += line.getResultsCount();
				if (line.isRankedCompetitor() && !competitors.contains(line.getCompetitor()))
					competitors.add(line.getCompetitor());
			}
		}
		validClassifiersCount = classifiers.size();
		competitorsWithRank = competitors.size();
	}

	public Date getDate() {
		return date;
	}

	public String getDateString() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
		return DATE_FORMAT.format(this.date);
	}

	public List<DivisionRanking> getDivisionRankings() {
		return divisionRankings;
	}

	public Long getId() {
		return id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDivisionRankings(List<DivisionRanking> divisionRankings) {
		this.divisionRankings = divisionRankings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalResultsCount() {
		return totalResultsCount;
	}

	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	public int getCompetitorsWithRank() {
		return competitorsWithRank;
	}

	public void setCompetitorsWithRank(int competitorsWithRank) {
		this.competitorsWithRank = competitorsWithRank;
	}

	public int getValidClassifiersCount() {
		return validClassifiersCount;
	}

	public void setValidClassifiersCount(int validClassifiersCount) {
		this.validClassifiersCount = validClassifiersCount;
	}

}
