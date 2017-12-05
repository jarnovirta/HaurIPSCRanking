package haur_ranking.gui.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PdfFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return false;
		}

		String fileName = file.getName().toLowerCase();
		return fileName.endsWith(".pdf");
	}

	@Override
	public String getDescription() {
		return "*.pdf";
	}
}
