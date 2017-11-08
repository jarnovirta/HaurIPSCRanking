package fi.haur_ranking.domain;

public class Stage {

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
}	
