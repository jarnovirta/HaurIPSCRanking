package haur_ranking.gui.filters;

import java.io.File;

public class FileFilterUtils {
	public static String getExtension(File file) {
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public static String getName(File f) {
		String fname = null;
		String s = f.getName();
		int i = s.length() - s.lastIndexOf('.');
		fname = s.substring(0, s.length() - i);

		return fname;
	}

}
