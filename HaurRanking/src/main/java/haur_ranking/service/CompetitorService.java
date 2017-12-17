package haur_ranking.service;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;

public class CompetitorService {

	private static CompetitorRepository competitorRepository;

	public static void init(CompetitorRepository competitorRepo) {
		competitorRepository = competitorRepo;
	}

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
		return competitorRepository.find(firstName, lastName, winMSSComment, entityManager);

	}

	public static Competitor persist(Competitor competitor) {
		return competitorRepository.persist(competitor);
	}

	public static List<Competitor> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Competitor> competitors = competitorRepository.findAll(entityManager);
		entityManager.close();
		return competitors;
	}

	public static int getTotalCompetitorCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = competitorRepository.getTotalCompetitorCount(entityManager);
		entityManager.close();
		return count;
	}

	public static List<Competitor> getCompetitorTableDataPage(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Competitor> competitors = competitorRepository.getCompetitorListPage(page, pageSize, entityManager);
		for (Competitor competitor : competitors) {
			competitor.setResultCount(StageScoreSheetService.getCompetitorStageScoreSheetCount(competitor));
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
			competitorRepository.delete(competitor, entityManager);
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
