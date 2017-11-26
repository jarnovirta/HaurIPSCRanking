package haur_ranking.gui;

import haur_ranking.service.MatchService.ImportStatus;

public class ImportProgressEvent {
	private int progressPercent;
	private ImportStatus importStatus;

	public ImportProgressEvent(ImportStatus importStatus, int progressPercent) {
		this.importStatus = importStatus;
		this.progressPercent = progressPercent;
	}

	public int getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}

	public ImportStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(ImportStatus importStatus) {
		this.importStatus = importStatus;
	}
}
