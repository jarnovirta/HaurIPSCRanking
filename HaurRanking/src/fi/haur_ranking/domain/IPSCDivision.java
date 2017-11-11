package fi.haur_ranking.domain;

public enum IPSCDivision {
	PRODUCTION, STANDARD, OPEN, CLASSIC, REVOLVER;
	
	@Override
	public String toString() {
		return Character.toUpperCase(this.name().charAt(0)) 
				+ this.name().substring(1).toLowerCase();
	}
}
