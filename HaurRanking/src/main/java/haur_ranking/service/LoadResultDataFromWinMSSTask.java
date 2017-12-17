package haur_ranking.service;

public class LoadResultDataFromWinMSSTask implements Runnable {
	private String winMSSFilePath;

	public LoadResultDataFromWinMSSTask(String winMSSFilePath) {
		this.winMSSFilePath = winMSSFilePath;

	}

	@Override
	public void run() {
		WinMSSDataImportService.findNewResultsInWinMSSDatabase(winMSSFilePath);
	}
}
