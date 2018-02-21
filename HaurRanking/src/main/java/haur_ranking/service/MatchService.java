package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;
import haur_ranking.utils.DateFormatUtils;

public class MatchService {

	private static MatchRepository matchRepository;

	public static void init(MatchRepository repository) {
		matchRepository = repository;
	}

	public static Match find(Match match, EntityManager entityManager) {
		return matchRepository.find(match);
	}

	public static void delete(Match match) {
		matchRepository.delete(match);
	}

	public static List<Match> getMatchTableData(int page, int pageSize) {
		List<Match> matches = matchRepository.getMatchListPage(page, pageSize);
		return matches;

	}

	public static List<Match> findAll() {
		List<Match> matches = null;
		matches = matchRepository.findAll();

		return matches;
	}

	public static int getTotalMatchCount() {
		int count = matchRepository.getMatchCount();
		return count;
	}

	public static void save(Match match) {

		// Save matches with new results. Check for existing matches.
		// Existing stage score sheets have already been
		// filtered out and therefore there is no need to check them or for
		// existing stages (all score sheets for a stage
		// are always treated together).

		Match existingMatch = matchRepository.find(match);
		if (existingMatch != null) {
			for (Stage stage : match.getStages()) {
				stage.setMatch(existingMatch);
			}
			existingMatch.getStages().addAll(match.getStages());
			match = existingMatch;
		}

		matchRepository.merge(match);
	}

	public static Match findLatestMatch() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		Match match = matchRepository.findNewestMatch();
		entityManager.close();
		return match;
	}

	public static List<String[]> getGUIImportedMatchesTableData() {
		List<String[]> tableRows = new ArrayList<String[]>();
		List<Match> matches = findAll();
		int matchCounter = 1;
		for (Match match : matches) {
			String[] rowData = new String[4];
			rowData[0] = (matchCounter++) + ".";
			rowData[1] = match.getName();
			rowData[2] = DateFormatUtils.calendarToDateString(match.getDate());
			if (match.getStages() == null || match.getStages().size() == 0)
				continue;
			rowData[3] = match.getStages().get(0).getName();
			tableRows.add(rowData);
			if (match.getStages().size() == 1)
				continue;
			for (int i = 1; i < match.getStages().size(); i++) {
				rowData = new String[4];
				rowData[0] = "";
				rowData[1] = "";
				rowData[2] = "";
				ClassifierStage classifier = match.getStages().get(i).getClassifierStage();
				if (classifier != null)
					rowData[3] = classifier.toString();
				tableRows.add(rowData);
			}
		}

		return tableRows;
	}

}
