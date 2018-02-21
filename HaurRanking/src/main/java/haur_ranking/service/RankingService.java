package haur_ranking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.pdf.PdfGenerator;
import haur_ranking.repository.haur_ranking_repository.RankingRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;

public class RankingService {

	private static RankingRepository rankingRepository;

	public static void init(RankingRepository rankingRepo) {
		rankingRepository = rankingRepo;
	}

	public static List<Ranking> getPreviousRankingsTableData(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Ranking> rankings = rankingRepository.getRankingsListPage(page, pageSize);
		entityManager.close();
		return rankings;
	}

	public static int getRankingsCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		int count = rankingRepository.getCount();
		entityManager.close();
		return count;
	}

	private static DivisionRankingRow generateDivisionRankingRow(Competitor competitor, DivisionRanking divisionRanking,
			Map<ClassifierStage, Double> classifierStageTopResultAverages) {

		List<StageScoreSheet> scoreSheetsForValidClassifiers = StageScoreSheetService.find(competitor.getFirstName(),
				competitor.getLastName(), divisionRanking.getDivision(), classifierStageTopResultAverages.keySet());
		if (scoreSheetsForValidClassifiers.size() == 0)
			return null;

		if (scoreSheetsForValidClassifiers.size() < 4)
			return new DivisionRankingRow(competitor, divisionRanking, false, scoreSheetsForValidClassifiers.size());
		int resultCount = scoreSheetsForValidClassifiers.size();
		if (scoreSheetsForValidClassifiers.size() > 8)
			scoreSheetsForValidClassifiers = scoreSheetsForValidClassifiers.subList(0, 8);

		List<Double> competitorRelativeScores = new ArrayList<Double>();

		// Calculate competitor relative scores
		for (StageScoreSheet sheet : scoreSheetsForValidClassifiers) {

			ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
			if (classifierStageTopResultAverages.keySet().contains(classifierStage)) {
				double classifierStageTopTwoResultsAverage = classifierStageTopResultAverages.get(classifierStage);
				competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
			}
		}

		Collections.sort(competitorRelativeScores);
		Collections.reverse(competitorRelativeScores);
		if (competitorRelativeScores.size() > 4) {
			competitorRelativeScores.subList(4, competitorRelativeScores.size()).clear();

		}

		Double competitorTopScoresAverage;
		double scoreSum = 0;

		for (int i = 0; i < 4; i++) {
			scoreSum += competitorRelativeScores.get(i);

		}

		competitorTopScoresAverage = scoreSum / 4;
		DivisionRankingRow line = new DivisionRankingRow(competitor, divisionRanking, true, competitorTopScoresAverage,
				StageScoreSheetService.getCompetitorHitFactorAverage(competitor, divisionRanking.getDivision()),
				resultCount);

		return line;

	}

	// Expects resultList to be ordered, top score first.
	private static void convertAverageScoresToPercentage(List<DivisionRankingRow> resultList) {
		if (resultList != null && resultList.size() > 0 && resultList.get(0).getBestResultsAverage() > 0) {
			double topScore = resultList.get(0).getBestResultsAverage();
			for (DivisionRankingRow line : resultList) {
				line.setResultPercentage(line.getBestResultsAverage() / topScore * 100);
			}
		}
	}

	private static DivisionRanking getDivisionRanking(IPSCDivision division) {
		DivisionRanking divisionRanking = new DivisionRanking(division);
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		// Get valid classifiers with two or more results, and top two hit
		// scores average for each.
		Map<ClassifierStage, Double> classifierStageTopResultAverages = StageService
				.getClassifierStagesWithTwoOrMoreResults(division);

		if (classifierStageTopResultAverages.keySet().isEmpty())
			return divisionRanking;
		for (ClassifierStage classifier : classifierStageTopResultAverages.keySet()) {
			if (!divisionRanking.getValidClassifiers().contains(classifier)) {
				divisionRanking.getValidClassifiers().add(classifier);
			}
		}
		List<DivisionRankingRow> resultList = new ArrayList<DivisionRankingRow>();
		List<Competitor> competitors = CompetitorService.findAll();

		for (Competitor competitor : competitors) {
			// Get competitor score sheets and limit number to eight.
			DivisionRankingRow row = generateDivisionRankingRow(competitor, divisionRanking,
					classifierStageTopResultAverages);
			if (row != null)
				resultList.add(row);

		}

		Collections.sort(resultList);
		Collections.reverse(resultList);
		convertAverageScoresToPercentage(resultList);
		divisionRanking.setDivisionRankingRows(resultList);
		entityManager.close();
		return divisionRanking;
	}

	public static Ranking generateRanking() {
		Ranking ranking = new Ranking();
		for (IPSCDivision division : IPSCDivision.values()) {
			DivisionRanking divRanking = getDivisionRanking(division);
			if (divRanking.getDivisionRankingRows().size() > 0) {
				ranking.getDivisionRankings().add(divRanking);
			}
		}
		ranking.setTotalCompetitorsAndResultsCounts();
		Match latestIncludedMatch = MatchService.findLatestMatch();
		if (latestIncludedMatch != null) {
			ranking.setLatestIncludedMatchDate(latestIncludedMatch.getDate());
			ranking.setLatestIncludedMatchName(latestIncludedMatch.getName());
		} else {
			ranking.setLatestIncludedMatchName("--");
		}
		persist(ranking);
		return ranking;
	}

	public static void createPdfRankingFile(Ranking ranking, Ranking compareToRanking, String pdfFilePath) {
		setImprovedRankingResults(ranking, compareToRanking);
		PdfGenerator.createPdfRankingFile(ranking, compareToRanking, pdfFilePath);
	}

	public static void setImprovedRankingResults(Ranking ranking, Ranking compareToRanking) {
		if (compareToRanking == null)
			return;
		// Check for improved positions in the ranking compared to an older
		// ranking. Those having improved their position
		// are shown in bold in the ranking pdf.
		for (DivisionRanking divisionRanking : ranking.getDivisionRankings()) {
			DivisionRanking compareToDivisionRanking = null;
			for (DivisionRanking compare : compareToRanking.getDivisionRankings()) {
				if (compare.getDivision().equals(divisionRanking.getDivision())) {
					compareToDivisionRanking = compare;
				}
			}
			if (compareToDivisionRanking == null)
				continue;
			for (DivisionRankingRow row : divisionRanking.getDivisionRankingRows()) {
				if (!row.isRankedCompetitor()) {
					row.setImprovedResult(false);
					continue;
				}
				row.setImprovedResult(true);
				Competitor competitor = row.getCompetitor();
				for (DivisionRankingRow compareToRow : compareToDivisionRanking.getDivisionRankingRows()) {
					if (compareToRow.getCompetitor().equals(competitor)) {
						int newPosition = divisionRanking.getDivisionRankingRows().indexOf(row);
						int oldPosition = compareToDivisionRanking.getDivisionRankingRows().indexOf(compareToRow);
						if (newPosition > oldPosition || newPosition == oldPosition) {
							row.setImprovedResult(false);
						}
					}
				}

			}
		}
	}

	public static Ranking findCurrentRanking() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		Ranking ranking = rankingRepository.findCurrentRanking();
		entityManager.close();
		return ranking;
	}

	public static void persist(Ranking ranking) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		rankingRepository.save(ranking);
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public static void delete(List<Ranking> rankings) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		for (Ranking ranking : rankings) {
			rankingRepository.delete(ranking);
		}
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public static void removeRankingDataForCompetitor(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		boolean commitTransaction = false;
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
			commitTransaction = true;
		}
		rankingRepository.removeRankingRowsForCompetitor(competitor);

		if (commitTransaction) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}
}
