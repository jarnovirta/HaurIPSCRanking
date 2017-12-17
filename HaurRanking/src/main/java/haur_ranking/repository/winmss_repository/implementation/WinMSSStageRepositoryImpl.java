package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;

public class WinMSSStageRepositoryImpl implements WinMSSStageRepository {
	public List<Stage> findAllHandgunStages() {
		List<Stage> stages = new ArrayList<Stage>();
		Connection connection = WinMSSDatabaseUtil.getConnection();
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
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return stages;
	}

	public List<Stage> findStagesForMatch(Match match) {
		List<Stage> stages = new ArrayList<Stage>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMSSDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT MatchId, StageId, StageName, TypeStdStageSetupId, TrgtPaper, TrgtPopper, TrgtPlates, TrgtPenalty, MinRounds FROM tblMatchStage WHERE MatchId="
							+ match.getWinMssMatchId());
			while (resultSet.next()) {
				Stage stage = new Stage();
				stage.setMatch(match);
				stage.setWinMssMatchId(resultSet.getLong(1));
				stage.setWinMssId(resultSet.getLong(2));
				stage.setName(resultSet.getString(3));
				stage.setWinMSSStandardStageSetupType(resultSet.getInt(4));
				stage.setPaperTargetCount(resultSet.getInt(5));
				stage.setPopperCount(resultSet.getInt(6));
				stage.setPlateCount(resultSet.getInt(7));
				stage.setPenaltyTargetCount(resultSet.getInt(8));
				stage.setMinShotsCount(resultSet.getInt(9));
				stages.add(stage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return stages;
	}

	public Map<ClassifierStage, Stage> getValidClassifiers() {
		Map<ClassifierStage, Stage> validStages = new HashMap<ClassifierStage, Stage>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMSSDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblTypeStdStageSetup");
			while (resultSet.next()) {
				Stage stage = new Stage();
				stage.setWinMSSStandardStageSetupType(resultSet.getInt(1));
				stage.setClassifierStage(ClassifierStage.parseString(resultSet.getString(2)));
				stage.setPaperTargetCount(resultSet.getInt(6));
				stage.setPopperCount(resultSet.getInt(7));
				stage.setPlateCount(resultSet.getInt(8));
				stage.setPenaltyTargetCount(resultSet.getInt(10));
				stage.setMinShotsCount(resultSet.getInt(11));
				validStages.put(stage.getClassifierStage(), stage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return validStages;
	}
}
