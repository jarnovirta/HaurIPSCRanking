package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.MatchRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSStageRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;

public class MatchService {
	private static EntityManager entityManager;
	
	public static int getTotalMatchCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = MatchRepository.getTotalMatchCount(entityManager);
		entityManager.close();
		return count;
	}
	public static Match persist(Match match) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		Match dbMatch = MatchRepository.save(match, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return dbMatch;
	}
	public static void importWinMssDatabase(String winMssDbLocation) {
		
		System.out.println("\n*** STARTING IMPORT");
		entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		
		// Find all matches from WinMSS database
		List<Match> winMssMatches = WinMSSMatchRepository.findAll(winMssDbLocation);
		List<Match> matchesWithNewResults = new ArrayList<Match>();

		List<StageScoreSheet> newStageScoreSheets = new ArrayList<StageScoreSheet>();
		
		// Filter matches to only include matches and stages with results which are not yet in ranking database.
		for (Match match : winMssMatches) {
			List<Stage> stagesWithNewResults = new ArrayList<Stage>();
			
			// Find all stages in WinMSS database and check whether stage results are already in ranking database
			match.setStages(WinMSSStageRepository.findStagesForMatch(match));
			
			for (Stage stage : match.getStages()) {
				stage.setMatch(match);
				if (ClassifierStage.contains(stage.getName())) stage.setClassifierStage(ClassifierStage.parseString(stage.getName()));
				///// TESTING -- ONLY CLASSIFIER STAGES
				 if (stage.getClassifierStage() == null) continue;
				 /////
				 if (StageService.find(stage, entityManager) != null) continue;
				 
				findNewWinMssStageScoreSheets(stage);
				if (stage.getStageScoreSheets() != null && stage.getStageScoreSheets().size() > 0) {
					newStageScoreSheets.addAll(stage.getStageScoreSheets());
					stagesWithNewResults.add(stage);
				}
			}
			if (stagesWithNewResults.size() > 0) matchesWithNewResults.add(match);
		}
		persist(matchesWithNewResults, entityManager);
		System.out.println("FOUND NEW RESULTS FOR " + matchesWithNewResults.size() + " MATCHES");
		entityManager.getTransaction().commit();
		entityManager.close();
		
		if (newStageScoreSheets.size() > 0) StageScoreSheetService.removeOldClassifierResults(newStageScoreSheets);
		System.out.println("\n*** IMPORT DONE");
	}
	public static void persist(List<Match> matches, EntityManager entityManager) {
		
		// Persist matches with new results. Check for existing matches. Existing stage score sheets have already been
		// filtered out and therefore there is no need to check them or for existing stages (all score sheets for a stage
		// are always treated together).
		for (Match newMatch : matches) {
			Match existingMatch = find(newMatch, entityManager);
			if (existingMatch != null) {
				for (Stage stage : newMatch.getStages()) {
					stage.setMatch(existingMatch);
				}
				existingMatch.getStages().addAll(newMatch.getStages());
			}
			else {
				System.out.println("MATCH SERVICE: Saving new match " + newMatch.getName());
				MatchRepository.save(newMatch, entityManager);
			}
		}
		
	}
	public static Match find(Match match, EntityManager entityManager) {
		return MatchRepository.find(match, entityManager);
	}
	
	private static void findNewWinMssStageScoreSheets(Stage stage) {
			stage.setStageScoreSheets(WinMSSStageScoreSheetRepository.find(stage.getMatch(), stage));
			for (StageScoreSheet sheet : stage.getStageScoreSheets()) sheet.setStage(stage);
			StageScoreSheetService.filterStageScoreSheetsExistingInDatabase(stage.getStageScoreSheets(), entityManager);
			// Handle new stage score sheets 
			if (stage.getStageScoreSheets().size() > 0) {
				StageScoreSheetService.setCompetitorsToStageScoreSheets(stage.getStageScoreSheets(), entityManager);
			
			}
		}
	}
