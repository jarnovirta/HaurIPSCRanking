package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.exception.DatabaseException;
import haur_ranking.repository.haur_ranking_repository.MatchRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;
import haur_ranking.utils.DateFormatUtils;

public class MatchService {

	private static MatchRepository matchRepository;

	public static void init(MatchRepository repository) {
		matchRepository = repository;
	}

	public static Match find(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		match = matchRepository.find(match, entityManager);
		entityManager.close();
		return match;
	}

	public static void delete(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			matchRepository.delete(match, entityManager);
			entityManager.getTransaction().commit();
		} catch (DatabaseException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}
	}

	public static List<Match> getMatchTableData(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<Match> matches = matchRepository.getMatchListPage(page, pageSize, entityManager);
		entityManager.close();
		return matches;
	}

	public static List<Match> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<Match> matches = matchRepository.findAll(entityManager);
		entityManager.close();
		return matches;
	}

	public static int getTotalMatchCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		int count = matchRepository.getMatchCount(entityManager);
		entityManager.close();
		return count;
	}

	public static void save(Match match) {

		// Save matches with new results. Check for existing matches.
		// Existing stage score sheets have already been
		// filtered out and therefore there is no need to check them or for
		// existing stages (all score sheets for a stage
		// are always treated together).
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		try {
			entityManager.getTransaction().begin();
			Match existingMatch = find(match);
			if (existingMatch != null) {
				for (Stage stage : match.getStages()) {
					stage.setMatch(existingMatch);
				}
				existingMatch.getStages().addAll(match.getStages());
				match = existingMatch;
			}
			matchRepository.persist(match, entityManager);
			entityManager.getTransaction().commit();
		} catch (DatabaseException e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}
	}

	public static Match findLatestMatch() {

		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		Match match = matchRepository.findNewestMatch(entityManager);
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
