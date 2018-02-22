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
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<Ranking> rankings = rankingRepository.getRankingsListPage(page, pageSize, entityManager);
		entityManager.close();
		return rankings;
	}

	public static int getRankingsCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		int count = rankingRepository.getCount(entityManager);
		entityManager.close();
		return count;
	}

	private static DivisionRankingRow generateDivisionRankingRow(Competitor competitor, DivisionRanking divisionRanking,
			Map<ClassifierStage, Double> classifierStageTopResultAverages) {

		// Find competitor's scores for valid classifiers. Return a null row if
		// no results
		List<StageScoreSheet> scoreSheetsForValidClassifiers = StageScoreSheetService.find(competitor.getFirstName(),
				competitor.getLastName(), divisionRanking.getDivision(), classifierStageTopResultAverages.keySet());
		if (scoreSheetsForValidClassifiers.size() == 0)
			return null;

		// Calculate result data for min. 4 and max. 8 latest results
		if (scoreSheetsForValidClassifiers.size() < 4)
			return new DivisionRankingRow(competitor, divisionRanking, false, scoreSheetsForValidClassifiers.size());
		int resultCount = scoreSheetsForValidClassifiers.size();
		if (scoreSheetsForValidClassifiers.size() > 8)
			scoreSheetsForValidClassifiers = scoreSheetsForValidClassifiers.subList(0, 8);

		List<Double> competitorRelativeScores = new ArrayList<Double>();

		// Calculate competitor relative scores for classifiers. Relative score
		// is competitor's hit factor
		// divided by the average of top 2 hit factors for the classifier.
		for (StageScoreSheet sheet : scoreSheetsForValidClassifiers) {
			ClassifierStage classifierStage = sheet.getStage().getClassifierStage();
			if (classifierStageTopResultAverages.keySet().contains(classifierStage)) {
				double classifierStageTopTwoResultsAverage = classifierStageTopResultAverages.get(classifierStage);
				competitorRelativeScores.add(sheet.getHitFactor() / classifierStageTopTwoResultsAverage);
			}
		}

		// Calculate average of competitor's 4 best classifier scores. This is
		// used
		// to determine the competitor's rank.
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

	private static void convertAverageScoresToPercentage(List<DivisionRankingRow> resultList) {
		// Set competitor ranking percentages relative to top competitor in the
		// division.
		if (resultList != null && resultList.size() > 0 && resultList.get(0).getBestResultsAverage() > 0) {
			double topScore = resultList.get(0).getBestResultsAverage();
			for (DivisionRankingRow line : resultList) {
				line.setResultPercentage(line.getBestResultsAverage() / topScore * 100);
			}
		}
	}

	private static DivisionRanking getDivisionRanking(IPSCDivision division) {
		DivisionRanking divisionRanking = new DivisionRanking(division);
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		// Get valid classifiers with two or more results, and top two hit
		// scores average for each.
		Map<ClassifierStage, Double> classifierStageTopResultAverages = StageService
				.getClassifierStagesWithTwoOrMoreResults(division);

		// If no valid classifiers exist for the division, return null
		if (classifierStageTopResultAverages.keySet().isEmpty())
			return null;

		// Add all valid classifiers (min. 2 results in division) to
		// DivisionRanking
		for (ClassifierStage classifier : classifierStageTopResultAverages.keySet()) {
			if (!divisionRanking.getValidClassifiers().contains(classifier)) {
				divisionRanking.getValidClassifiers().add(classifier);
			}
		}
		List<DivisionRankingRow> resultList = new ArrayList<DivisionRankingRow>();
		List<Competitor> competitors = CompetitorService.findAll();

		for (Competitor competitor : competitors) {
			// Add ranking data rows for competitors. Get max 8 latest
			// competitor score sheets in division and for valid classifiers.
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

		// Generate a division ranking for each IPSC division
		// for which valid results exist in database
		for (IPSCDivision division : IPSCDivision.values()) {
			DivisionRanking divRanking = getDivisionRanking(division);
			if (divRanking != null && divRanking.getDivisionRankingRows().size() > 0) {
				ranking.getDivisionRankings().add(divRanking);
			}
		}
		if (ranking.getDivisionRankings().size() == 0)
			return null;
		// Set other ranking data and save to database.
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
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		Ranking ranking = rankingRepository.findCurrentRanking(entityManager);
		entityManager.close();
		return ranking;
	}

	public static void persist(Ranking ranking) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		rankingRepository.save(ranking, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public static void delete(List<Ranking> rankings) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		for (Ranking ranking : rankings) {
			rankingRepository.delete(ranking, entityManager);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static void removeRankingDataForCompetitor(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		rankingRepository.removeRankingRowsForCompetitor(competitor, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();

	}
}
