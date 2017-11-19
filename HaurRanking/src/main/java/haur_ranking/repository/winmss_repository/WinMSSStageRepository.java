package haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;

public class WinMSSStageRepository {
	public static List<Stage> findAllHandgunStages() {
		List<Stage> stages = new ArrayList<Stage>();
		Connection connection = WinMssDatabaseUtil.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT MatchId, StageId, StageName FROM tblMatchStage WHERE TypeFirearmId=1");
			while (resultSet.next()) {
				Stage stage = new Stage();
				stage.setWinMssMatchId(resultSet.getLong(1));
				stage.setWinMssId(resultSet.getLong(2));
				stage.setName(resultSet.getString(3));
				stages.add(stage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMssDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return stages;
	}

	public static List<Stage> findStagesForMatch(Match match) {
		List<Stage> stages = new ArrayList<Stage>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMssDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT MatchId, StageId, StageName FROM tblMatchStage WHERE MatchId=" + match.getWinMssMatchId());
			while (resultSet.next()) {
				Stage stage = new Stage();
				stage.setMatch(match);
				stage.setWinMssMatchId(resultSet.getLong(1));
				stage.setWinMssId(resultSet.getLong(2));
				stage.setName(resultSet.getString(3));
				stages.add(stage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMssDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return stages;
	}
}
