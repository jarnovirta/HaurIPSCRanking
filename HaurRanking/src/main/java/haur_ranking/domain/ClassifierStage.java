package haur_ranking.domain;

// IPSC classification stage listing.
// http://www.ipsc.org/classification/icsStages.php
public enum ClassifierStage {
	CLC01, CLC03, CLC05, CLC07, CLC09, CLC11, CLC13, CLC15, CLC17, CLC19, CLC21, CLC23, CLC25, CLC27, CLC29, CLC31, CLC33, CLC35, CLC37, CLC39, CLC41, CLC43, CLC45, CLC47, CLC49, CLC51, CLC53, CLC55, CLC57, CLC59, CLC61, CLC63, CLC65, CLC67, CLC69, CLC71, CLC73, CLC75, CLC77, CLC79;

	public static boolean contains(String testString) {
		for (ClassifierStage stage : ClassifierStage.values()) {
			if (stage.toString().equals(testString))
				return true;
		}
		return false;
	}

	public static ClassifierStage getClassifierStageByWinMSSStandardStageType(int winMSSStandardStageType) {
		String typeString = String.valueOf(winMSSStandardStageType);
		if (typeString.length() == 1) {
			typeString = "0" + typeString;
		}

		for (ClassifierStage classifier : ClassifierStage.values()) {
			String stageString = classifier.toString();
			if (stageString.substring(4, stageString.length()).equals(typeString)) {
				return classifier;
			}
		}
		return null;
	}

	public static ClassifierStage parseString(String testString) {
		for (ClassifierStage stage : ClassifierStage.values()) {
			if (stage.toString().equals(testString))
				return stage;
		}
		return null;
	}

	@Override
	public String toString() {
		return this.name().substring(0, 3) + "-" + this.name().substring(3);
	}
}
