package haur_ranking.gui.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

//FileChooser filter which only accepts WinMSS database files (WinMSS.mdb)
public class WinMSSFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		if (file.getName().equals("WinMSS.mdb"))
			return true;
		return false;
	}

	@Override
	public String getDescription() {
		return "WinMSS Database";
	}

}
