package ca.usask.cs.srlab.simcad.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	static public boolean deleteDirectory(String path) {
		return deleteDirectory(new File(path));
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
	public static boolean isDirExist(String dirname) {
		return (new File(dirname)).isDirectory();
	}

	public static boolean isFileExist(String filename) {
		return (new File(filename)).isFile();
	}

	public static boolean isFilenameValid(String file) {
		File f = new File(file);
		try {
			f.getCanonicalPath();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
