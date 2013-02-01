package ca.usask.cs.srlab.simcad.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

import ca.usask.cs.srlab.simcad.SimcadException;

public class FileUtil {

	static public boolean deleteDirectory(String path) {
		return deleteDirectory(new File(path));
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists() && path.isDirectory()) {
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

	public static File createTempFileWithContent(String content, String fineName, String ext){
		File file = null;
		try{
			File tmpDir = createTempDirectory();
			
			file = File.createTempFile(fineName, ext, tmpDir);
			FileWriter fileWritter = new FileWriter(file);
		    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		    bufferWritter.write(content);
		    bufferWritter.close();
		}catch (Exception e) {
			throw new SimcadException(e);
		}
	    return file;
	}

	public static File createTempDirectory() {
		File tmpDir = Files.createTempDir();
		
		if(tmpDir.getAbsolutePath().contains("-")){
			tmpDir.delete();
			tmpDir = new File(tmpDir.getAbsolutePath().replaceAll("-", "__"));
			tmpDir.mkdir();
		}
		return tmpDir;
	}
	
	public static String getFileName(File item_to_search) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getFileExt(File item_to_search) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getFileContents(String fileName){
		return getFileContents(new File(fileName));
	}

	public static String getFileContents(String fileName, int fromLine, int toLine){
		return getFileContents(new File(fileName), fromLine, toLine);
	}
	
	private static String copyContent(List <String> lines, int fromLine, int toLine){
		StringBuffer sb = new StringBuffer();
			if(fromLine >= 1 && fromLine <= toLine && lines.size() >= toLine){
				for (int l = fromLine -1 ; l < toLine; l++){
					sb.append(lines.get(l));
					if(l < toLine - 1)
						sb.append("\n");
				}
			}
		return sb.toString();
	}
	
	public static String getFileContents(File file){
		try {
			List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
			return copyContent(lines, 1, lines.size());
		} catch (IOException e) {
			throw new SimcadException(e);
		}
	}
	
	public static String getFileContents(File file, int fromLine, int toLine) {
		try {
			List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
			return copyContent(lines, fromLine, toLine);
		} catch (IOException e) {
			throw new SimcadException(e);
		}
	}

	public static File createTempJavaFileWithContent(String content) {
		String fileName = "temp";
		String ext = ".java";
		return createTempFileWithContent(content, fileName, ext);
	}

}
