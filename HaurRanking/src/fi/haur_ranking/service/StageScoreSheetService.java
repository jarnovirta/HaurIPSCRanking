package fi.haur_ranking.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSStageScoreSheetRepository;
import fi.haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;


// Reads score data from a WinMSS .mdb file (Microsoft Access database)
public class StageScoreSheetService {
	
	Connection connection;
	
	StageScoreSheetRepository stageScoreSheetRepository = new StageScoreSheetRepository();
	WinMSSStageScoreSheetRepository winMSSStageScoreSheetRepository = new WinMSSStageScoreSheetRepository();
	
	public StageScoreSheet saveToHaurRankingDB(StageScoreSheet sheet) {
		return stageScoreSheetRepository.save(sheet);
		
	}
	public List<StageScoreSheet> findAllFromHaurRankingDB() {
		return stageScoreSheetRepository.findAll();
	}
}
