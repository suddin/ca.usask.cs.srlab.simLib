package ca.usask.cs.srlab.simcad.util;

import java.io.File;

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

}
