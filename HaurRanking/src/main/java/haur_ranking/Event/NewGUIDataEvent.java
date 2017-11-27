package haur_ranking.Event;

import java.util.List;

import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;

public class NewGUIDataEvent {
	private Ranking ranking;
	private DatabaseStatistics databaseStatistics;
	private List<Match> databaseMatchList;

	public NewGUIDataEvent(Ranking ranking, DatabaseStatistics databaseStatistics, List<Match> databaseMatchList) {
		this.ranking = ranking;
		this.databaseStatistics = databaseStatistics;
		this.databaseMatchList = databaseMatchList;
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

	public List<Match> getDatabaseMatchList() {
		return databaseMatchList;
	}

	public void setDatabaseMatchList(List<Match> databaseMatchList) {
		this.databaseMatchList = databaseMatchList;
	}

}
