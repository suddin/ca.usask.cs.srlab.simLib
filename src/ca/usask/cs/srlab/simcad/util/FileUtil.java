package ca.usask.cs.srlab.simcad.util;

import java.io.File;
import java.io.IOException;

import ca.usask.cs.srlab.simcad.SimcadException;

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

	public static void createDirs(String dirFullPath) {
		if(!new File(dirFullPath).exists()){
			try {
				File outputDir = new File(dirFullPath);
				outputDir.mkdirs();
				outputDir.setReadable(true);
				outputDir.setWritable(true);
			} catch (Exception e) {
				throw new SimcadException("Error in ereating directory : "
						+ dirFullPath, e);
			}
		}
	}

}
