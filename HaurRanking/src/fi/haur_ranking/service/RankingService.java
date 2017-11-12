package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Stage;

public class RankingService {
	public static Map<IPSCDivision, List<String[]>> generateRanking() {
		Map<IPSCDivision, List<String[]>> divisionRankings = new HashMap<IPSCDivision, List<String[]>>();
		for (IPSCDivision division : IPSCDivision.values()) {
			divisionRankings.put(division, generateRankingForDivision(division));
		}
	
		return divisionRankings;
	}
	private static List<String[]> generateRankingForDivision(IPSCDivision division) {
		List<String[]> divisionRankingLineItems = new ArrayList<String[]>();
		List<ClassifierStage> stagesWithEnoughResults = StageService.getClassifierStagesWithTwoOrMoreResults(division);
		System.out.println("Got " + stagesWithEnoughResults.size() + " classifier stages with at least two results for " + division.toString());
		for (ClassifierStage stage : stagesWithEnoughResults) {
			System.out.println(stage.toString());
		}
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