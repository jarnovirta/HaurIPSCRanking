package fi.haur_ranking.domain;

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
	private int ssiStageNumber;
	private String name;
	private int ssiPrimaryKey;
	public int getSsiStageNumber() {
		return ssiStageNumber;
	}
	public void setSsiStageNumber(int ssiStageNumber) {
		this.ssiStageNumber = ssiStageNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSsiPrimaryKey() {
		return ssiPrimaryKey;
	}
	public void setSsiPrimaryKey(int ssiPrimaryKey) {
		this.ssiPrimaryKey = ssiPrimaryKey;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}	
