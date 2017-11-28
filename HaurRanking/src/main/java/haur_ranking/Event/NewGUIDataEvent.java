package haur_ranking.Event;

import java.util.List;

import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Ranking;

public class NewGUIDataEvent {
	private Ranking ranking;
	private DatabaseStatistics databaseStatistics;
	private List<String[]> importedMatchesTableData;

	public NewGUIDataEvent(Ranking ranking, DatabaseStatistics databaseStatistics,
			List<String[]> importedMatchesTableData) {
		this.ranking = ranking;
		this.databaseStatistics = databaseStatistics;
		this.importedMatchesTableData = importedMatchesTableData;
	}

	public Ranking getRanking() {
		return ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	public DatabaseStatistics getDatabaseStatistics() {
		return databaseStatistics;
	}

	public void setDatabaseStatistics(DatabaseStatistics databaseStatistics) {
		this.databaseStatistics = databaseStatistics;
	}

	public List<String[]> getImportedMatchesTableData() {
		return importedMatchesTableData;
	}

	public void setImportedMatchesTableData(List<String[]> importedMatchesTableData) {
		this.importedMatchesTableData = importedMatchesTableData;
	}

}
