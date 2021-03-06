package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class WinMSSStageScoreSheetRepositoryImpl implements WinMSSStageScoreSheetRepository {

	private WinMSSCompetitorRepositoryImpl competitorRepository;

	public WinMSSStageScoreSheetRepositoryImpl() {
		competitorRepository = new WinMSSCompetitorRepositoryImpl();
	}

	@Override
	public int getScoreSheetCountForStage(Match match, Stage stage) {
		int count = -1;
		Connection connection = WinMSSDatabaseUtil.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT COUNT(MatchId) AS count FROM tblMatchStageScore WHERE MatchId="
					+ match.getWinMssMatchId() + " AND StageId = " + stage.getWinMssId());
			while (resultSet.next()) {
				count = resultSet.getInt("count");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return count;
	}

	@Override
	public List<StageScoreSheet> find(Match match, Stage stage) {

		List<StageScoreSheet> resultScoreSheets = new ArrayList<StageScoreSheet>();
		Connection connection = WinMSSDatabaseUtil.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblMatchStageScore WHERE MatchId="
					+ match.getWinMssMatchId() + " AND StageId = " + stage.getWinMssId());
			while (resultSet.next()) {
				StageScoreSheet sheet = new StageScoreSheet();
				sheet.setStage(stage);
				sheet.setWinMssStageId(resultSet.getLong(2));
				sheet.setWinMssMemberId(resultSet.getLong(3));
				Competitor competitor = competitorRepository.findCompetitor(sheet.getWinMssMemberId(),
						match.getWinMssMatchId());
				if (competitorRepository.isDisqualified(sheet.getWinMssMemberId(), match.getWinMssMatchId()))
					continue;
				sheet.setCompetitor(competitor);
				sheet.setaHits(resultSet.getInt(4));
				sheet.setbHits(resultSet.getInt(5));
				sheet.setcHits(resultSet.getInt(6));
				sheet.setdHits(resultSet.getInt(7));
				sheet.setMisses(resultSet.getInt(8));
				sheet.setPenalties(resultSet.getInt(9));
				sheet.setProcedurals(resultSet.getInt(10));
				sheet.setTime(resultSet.getDouble(11));
				sheet.setScoresZeroedForStage(resultSet.getBoolean(12));
				sheet.setDisqualified(resultSet.getBoolean(15));
				sheet.setHitFactor(resultSet.getDouble(19));
				sheet.setLastModifiedInWinMSSDatabaseString(resultSet.getString(21));
				resultScoreSheets.add(sheet);
			}
			statement.close();
			resultSet.close();

			List<StageScoreSheet> removeScoreSheets = new ArrayList<StageScoreSheet>();
			// Add IPSC division and remove sheets for competitors who failed
			// power factor. Also remove sheets
			// with last modification date null in WinMSS database. These are
			// blank score sheets where no result has
			// been entered at any point.
			for (StageScoreSheet sheet : resultScoreSheets) {
				statement = connection.createStatement();
				resultSet = statement
						.executeQuery("SELECT TypeDivisionId, FailedPowerFactor FROM tblMatchCompetitor WHERE MemberId="
								+ sheet.getWinMssMemberId() + " AND MatchId=" + match.getWinMssMatchId());
				if (resultSet.next()) {
					sheet.setIpscDivision(IPSCDivision.getDivisionByWinMSSTypeId(resultSet.getInt(1)));
					if (resultSet.getBoolean(2))
						removeScoreSheets.add(sheet);
				}
				if (sheet.getLastModifiedInWinMSSDatabaseString() == null)
					removeScoreSheets.add(sheet);
			}
			resultScoreSheets.removeAll(removeScoreSheets);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return resultScoreSheets;
	}
}
