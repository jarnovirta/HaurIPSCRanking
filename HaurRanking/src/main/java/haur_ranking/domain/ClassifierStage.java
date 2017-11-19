package haur_ranking.domain;

// IPSC classification stage listing.
// http://www.ipsc.org/classification/icsStages.php
public enum ClassifierStage {
	CLC01, CLC02, CLC03, CLC04, CLC05, CLC06, CLC07, CLC08, CLC09, CLC10, CLC11, CLC12, CLC13, CLC14, CLC15, CLC16, CLC17, CLC18, CLC19, CLC20, CLC21, CLC22, CLC23, CLC24, CLC25, CLC26, CLC27, CLC28, CLC29, CLC30, CLC31, CLC32, CLC33, CLC34, CLC35, CLC36, CLC37, CLC38, CLC39, CLC40, CLC41, CLC42, CLC43, CLC44, CLC45, CLC46, CLC47, CLC48, CLC49, CLC50, CLC51, CLC52, CLC53, CLC54, CLC55, CLC56, CLC57, CLC58, CLC59, CLC60, CLC61, CLC62, CLC63, CLC64, CLC65, CLC66, CLC67, CLC68, CLC69, CLC70, CLC71, CLC72, CLC73, CLC74, CLC75, CLC76, CLC77, CLC78, CLC79, CLC80;
	public static boolean contains(String testString) {
		for (ClassifierStage stage : ClassifierStage.values()) {
			if (stage.toString().equals(testString))
				return true;
		}
		return false;
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
