package fi.haur_ranking.domain;

public enum IPSCDivision {
	PRODUCTION, STANDARD, OPEN, CLASSIC, REVOLVER;
	
	@Override
	public String toString() {
		return Character.toUpperCase(this.name().charAt(0)) 
				+ this.name().substring(1).toLowerCase();
	}
	public static IPSCDivision getDivisionByWinMSSTypeId(int winMSSDivisionTypeId) {
		if (winMSSDivisionTypeId == 1) return IPSCDivision.OPEN;
		if (winMSSDivisionTypeId == 2) return IPSCDivision.STANDARD;
		if (winMSSDivisionTypeId == 4) return IPSCDivision.PRODUCTION;
		if (winMSSDivisionTypeId == 5) return IPSCDivision.REVOLVER;
		if (winMSSDivisionTypeId == 18) return IPSCDivision.CLASSIC;
		else return null;
	}
}
