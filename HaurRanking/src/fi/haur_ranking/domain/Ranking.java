package fi.haur_ranking.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ranking {
	Map<IPSCDivision, List<String[]>> divisionRankings = new HashMap<IPSCDivision, List<String[]>>();

	public Map<IPSCDivision, List<String[]>> getDivisionRankings() {
		return divisionRankings;
	}

	public void setDivisionRankings(Map<IPSCDivision, List<String[]>> divisionRankings) {
		this.divisionRankings = divisionRankings;
	}
}
