package ca.usask.cs.srlab.simcad.util;

import java.io.File;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileUtilTest {

	static String testFile;
	
	@BeforeClass
	public static void init(){
		testFile = ClassLoader.getSystemResource("ca/usask/cs/srlab/simcad/util/FileUtilTest.txt").getFile();
	}
	
	@Test
	public void testGetFileContents(){
		
		String content = null;
		
		content = FileUtil.getFileContents(testFile, -1, 0);
		Assert.assertEquals(content, "");

		content = FileUtil.getFileContents(testFile, 2, 0);
		Assert.assertEquals(content, "");

		content = FileUtil.getFileContents(testFile, 1, 1);
		Assert.assertEquals(content, "line 1");
		
		content = FileUtil.getFileContents(testFile, 2, 3);
		Assert.assertEquals(content, "line 2\nline 3");
		
		content = FileUtil.getFileContents(testFile, 5, 9);
		Assert.assertEquals(content, "line 5\n\nline 7\n\nline 9");
		
		content = FileUtil.getFileContents(testFile, 5, 10);
		Assert.assertEquals(content, "");
		
	}
	
	
	@Test
	public void testCreateTempFileWithContent(){
		String content = "line 1\nline 2\nline 3\n\nline 5";
		
		File tempFile = FileUtil.createTempJavaFileWithContent(content);
		try{
			String tempContent = FileUtil.getFileContents(tempFile, 1, 5);
			Assert.assertEquals(content, tempContent);
		}
		finally{
			FileUtil.deleteDirectory(tempFile.getParent());
		}
	}
	
	
}
