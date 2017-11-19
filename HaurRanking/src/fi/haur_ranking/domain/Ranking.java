package fi.haur_ranking.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	List<DivisionRanking> divisionRankings = new ArrayList<DivisionRanking>();

	@Temporal(TemporalType.TIMESTAMP)
	Date date;

	public Ranking() {
		date = new Date();
	}

	public Date getDate() {
		return date;
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
}
