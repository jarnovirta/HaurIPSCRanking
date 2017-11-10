package fi.haur_ranking.domain;

// IPSC classification stage listing.
// http://www.ipsc.org/classification/icsStages.php
public enum ClassifierStage {
	CLC01, CLC07, CLC03, CLC05, CLC11,CLC15,CLC19,
	CLC21, CLC25, CLC31, CLC33, CLC35, CLC37, 
	CLC41, CLC45, CLC47, CLC49, CLC51, CLC53, 
	CLC55, CLC57, CLC59,CLC63, CLC65, CLC67, CLC69, 
	CLC71, CLC73;
	
	public String toString() {
		return this.name().substring(0, 2) + "-" + this.name().substring(3); 
	}
}
