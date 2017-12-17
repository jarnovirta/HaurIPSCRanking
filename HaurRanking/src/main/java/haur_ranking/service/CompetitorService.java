package haur_ranking.service;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class CompetitorService {

	public static Competitor find(String firstName, String lastName, String winMSSComment) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		Competitor competitor = find(firstName, lastName, winMSSComment, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return competitor;
	}

	public static Competitor find(String firstName, String lastName, String winMSSComment,
			EntityManager entityManager) {
		return CompetitorRepository.find(firstName, lastName, winMSSComment, entityManager);

	}

	public static Competitor persist(Competitor competitor) {
		return CompetitorRepository.persist(competitor);
	}

	public static List<Competitor> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Competitor> competitors = CompetitorRepository.findAll(entityManager);
		entityManager.close();
		return competitors;
	}

	public static int getTotalCompetitorCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = CompetitorRepository.getTotalCompetitorCount(entityManager);
		entityManager.close();
		return count;
	}

	public static List<Competitor> getCompetitorTableDataPage(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Competitor> competitors = CompetitorRepository.getCompetitorListPage(page, pageSize, entityManager);
		for (Competitor competitor : competitors) {
			competitor.setResultCount(
					StageScoreSheetRepository.getCompetitorStageScoreSheetCount(competitor, entityManager));
		}
		entityManager.close();
		return competitors;
	}

	public static void deleteAll(List<Competitor> competitors) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		for (Competitor competitor : competitors) {
			StageScoreSheetService.removeStageScoreSheetsForCompetitor(competitor);
			RankingService.removeRankingDataForCompetitor(competitor);
			CompetitorRepository.delete(competitor, entityManager);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		RankingService.generateRanking();
	}

	public static int mergeCompetitorsInMatch(Match match) {
		int newCompetitorsCount = 0;
		for (Stage stage : match.getStages()) {
			if (stage.getStageScoreSheets() != null) {
				for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
					Competitor existingCompetitor = CompetitorService.find(sheet.getCompetitor().getFirstName(),
							sheet.getCompetitor().getLastName(), sheet.getCompetitor().getWinMSSComment());
					if (existingCompetitor != null) {
						sheet.setCompetitor(existingCompetitor);
					} else {
						sheet.setCompetitor(persist(sheet.getCompetitor()));
						newCompetitorsCount++;
					}
				}

			}
		}
		return newCompetitorsCount;
	}

}
