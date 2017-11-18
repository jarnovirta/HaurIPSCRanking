package fi.haur_ranking.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DivisionRanking")
public class DivisionRanking {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Enumerated(EnumType.STRING)
	IPSCDivision division;

	@OneToMany(fetch = FetchType.EAGER)
	List<DivisionRankingLine> rankingLines;

	public DivisionRanking() {
	}

	public DivisionRanking(IPSCDivision division) {
		this.division = division;
	}

	public IPSCDivision getDivision() {
		return division;
	}

	public Long getId() {
		return id;
	}

	public List<DivisionRankingLine> getRankingLines() {
		return rankingLines;
	}

	public void setDivision(IPSCDivision division) {
		this.division = division;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRankingLines(List<DivisionRankingLine> rankingLines) {
		this.rankingLines = rankingLines;
	}

}
