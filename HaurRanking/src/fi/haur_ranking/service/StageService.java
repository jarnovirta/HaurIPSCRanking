package fi.haur_ranking.service;

import java.util.List;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.repository.haur_ranking_repository.StageRepository;

public class StageService {
	public static List<ClassifierStage> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		
		return StageRepository.getClassifierStagesWithTwoOrMoreResults(division);
	}
}
