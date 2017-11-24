package haur_ranking.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "DivisionRanking")
public class DivisionRanking {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Enumerated(EnumType.STRING)
	private IPSCDivision division;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<DivisionRankingLine> rankingLines = new ArrayList<DivisionRankingLine>();

	@Transient
	private Set<ClassifierStage> validClassifiers = new HashSet<ClassifierStage>();

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

	public Set<ClassifierStage> getValidClassifiers() {
		return validClassifiers;
	}

	public void setValidClassifiers(Set<ClassifierStage> validClassifiers) {
		this.validClassifiers = validClassifiers;
	}

}
