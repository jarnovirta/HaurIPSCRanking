package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;

public class RankingService {
	public static Map<IPSCDivision, List<String[]>> generateRanking() {
		System.out.println("\n*** HAUR VARJORANKING ***");
		Map<IPSCDivision, List<String[]>> divisionRankings = new HashMap<IPSCDivision, List<String[]>>();
		for (IPSCDivision division : IPSCDivision.values()) {
			divisionRankings.put(division, generateRankingForDivision(division));
		}
	
		return divisionRankings;
	}
	private static List<String[]> generateRankingForDivision(IPSCDivision division) {
		System.out.println("\nDIVISION: " + division.toString());
		
		List<String[]> divisionRankingLineItems = new ArrayList<String[]>();
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		
		// Get a list of classifier stages for which results will be taken into account in ranking 
		// (stages with min. 2 results). Get average of two best results for each.
		
		Map<ClassifierStage, Double> classifierStageTopResultAvgerages = StageService.getClassifierStagesWithTwoOrMoreResults(division);
		List<Object[]> resultList = CompetitorRepository.getCompetitorRankingScoresForClassifierStages(classifierStageTopResultAvgerages, division, entityManager);
		int position = 1;
		for (Object[] rankingItem : resultList) {
			Competitor competitor = (Competitor) rankingItem[0];
			Double result = (Double) rankingItem[1];
			System.out.println(position + ". " + competitor.getFirstName() + " " + competitor.getLastName() + " / score : " + result);
			position++;
			
		}
		
		// Generate competitor ranking scores. Get stage comparison numbers (competitors hit factor divided by average of two 
		// best hit factors for the stage, max value 2) for 8 most recent results for competitors with min. 4 results
		// for valid classifier stages in classifierStageTopResultAvgerages. Calculate average of 4 best comparison numbers.
		// Competitors' position in ranking is based on this average.
		
		
		// Generate ranking list for division based on competitors' score.
		
		// Calculate hit factor average for competitor (for 4 best results?) which does not affect ranking. Calculate percentages
		// (100% for first, others based on ranking score divided by the score for the top competitor)
			entityManager.close();
			
		return divisionRankingLineItems;
	}
}

//Laskenta tapahtuu seuraavasti: Suorituksesta saatu osumakerroin (HF) merkit‰‰n kyseisen 
//classifier-aseman luokkakohtaiseen HF-luetteloon, jonka kahdesta parhaasta tuloksesta otetaan 
//keskiarvo. Kilpailjan HF jaetaan n‰in saadulla keskiarvolla ja tulos on kilpailijan luokittelutulos 
//(score) kyseiselt‰ asemalta (yleens‰ v‰lill‰ 0,5-1,1). Kilpailijan ampumista viimeisest‰ 8 
//classifier-suorituksesta nelj‰n parhaan tuloksen keskiarvon on kilpailijan ranking-tulos, 
//jota verrataan muihin luokan ampujiin. Parhaan ranking-tuloksen saanut sijoittuu ensimm‰iseksi 
//ja toiseksi parhaan saanut toiseksi, vertailuprosentin ollessa toiseksi parhaan ranking-tulos 
//jaettuna parhaan ranking-tuloksella. Kolmanneksi sijoitetun vertailuprosentin ollessa niin ik‰‰n 
//ranking-tuloksensa jaettuna parhaan ranking-tuloksella.
//
//Lis‰ksi lasketaan luokitukseen vaikuttamaton HF-keskiarvo, jolla on arvoa l‰hinn‰ luokkien 
//v‰lisess‰ vertailussa ja on suuntaa antava, riippuen pitk‰lti siit‰, kuinka nopeita asemia 
//vertailtavat luokitellut ampujat ovat ampuneet.