package haur_ranking.domain;

public enum IPSCDivision {
	PRODUCTION, PRODUCTION_OPTICS, STANDARD, OPEN, CLASSIC, REVOLVER;

	public static IPSCDivision getDivisionByWinMSSTypeId(int winMSSDivisionTypeId) {
		if (winMSSDivisionTypeId == 1)
			return IPSCDivision.OPEN;
		if (winMSSDivisionTypeId == 2)
			return IPSCDivision.STANDARD;
		if (winMSSDivisionTypeId == 4)
			return IPSCDivision.PRODUCTION;
		if (winMSSDivisionTypeId == 5)
			return IPSCDivision.REVOLVER;
		if (winMSSDivisionTypeId == 18)
			return IPSCDivision.CLASSIC;
		if (winMSSDivisionTypeId == 24)
			return IPSCDivision.PRODUCTION_OPTICS;
		else
			return null;
	}

	@Override
	public String toString() {
		if (this == IPSCDivision.PRODUCTION_OPTICS)
			return "Production optics";
		return Character.toUpperCase(this.name().charAt(0)) + this.name().substring(1).toLowerCase();
	}

}
