package fi.haur_ranking.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;

public class MatchService {
	Connection connection;
	
	public Match readMatchFromDatabase(int matchId) {
		connection = AccessDatabaseUtil.connectToAccessDatabase();
		List<Competitor> competitors = findAllCompetitors();
		for (Competitor competitor : competitors) {
			System.out.println(competitor.getFirstName() + " " + competitor.getLastName());
		}
		AccessDatabaseUtil.closeConnecion(connection);
		return null;
	}
	
	public List<Competitor> findAllCompetitors() {
		Statement statement = null;
		ResultSet resultSet = null;
		List<Competitor> competitors = new ArrayList<Competitor>();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblMember");
			while (resultSet.next()) {
				competitors.add(new Competitor(resultSet.getString(3), resultSet.getString(2), resultSet.getString(1)));
			}
			resultSet.close();
			statement.close();
			return competitors;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
