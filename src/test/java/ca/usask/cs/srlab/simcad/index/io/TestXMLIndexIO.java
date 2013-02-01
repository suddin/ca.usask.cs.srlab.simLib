package ca.usask.cs.srlab.simcad.index.io;

import java.io.File;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ca.usask.cs.srlab.simcad.SimCadConstants;
import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.memory.MemoryCloneIndexByJavaCollection;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.util.FileUtil;
import com.google.common.io.Files;

public class TestXMLIndexIO {
	static ICloneIndex cloneIndex;
	static CloneFragment cf1;
	static CloneFragment cf11;
	static CloneFragment cf2;
	static CloneFragment cf3;
	
	static DetectionSettings detectionSettings;
	
	static IndexIOConfiguration indexIOConfiguration;
	
	@BeforeClass
	public static void setup() {
		detectionSettings = new DetectionSettings(SimCadConstants.LANGUAGE_JAVA, CloneFragment.CLONE_GRANULARITY_FUNCTION, SimCadConstants.CLONE_SET_TYPE_GROUP, SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true, CloneSet.CLONE_TYPE_1);
		
		cloneIndex = new MemoryCloneIndexByJavaCollection("dummy", "java", detectionSettings.getCloneGranularity(), detectionSettings.getSourceTransformation());
		
		cf1 = new FunctionCloneFragment("file1", 1, 4, 1, "original1() {code1}");
		cf1.setSimhash1(1234l);
		cf1.setSimhash2(1234l);

		cf11 = new FunctionCloneFragment("file11", 1, 4, 2, "original1() {code1}");
		cf11.setSimhash1(1234l);
		cf11.setSimhash2(1234l);
		
		cf2 = new FunctionCloneFragment("file1", 11, 44, 3, "original3() {code2}");
		cf2.setSimhash1(5678l);
		cf2.setSimhash2(5678l);
		
		cf3 = new FunctionCloneFragment("file3", 11, 44, 4, "original() {code3\ncode4}");
		cf3.setSimhash1(3267l);
		cf3.setSimhash2(3267l);
		
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf11);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		
		File indexFileContainerFolder = Files.createTempDir();
		
		indexIOConfiguration = new XMLIndexIOConfiguration(indexFileContainerFolder.getAbsolutePath(),"dummy", detectionSettings);
	}

	@Test
	public void testIndexExport(){
		cloneIndex.exportIndex(indexIOConfiguration);
		
		Assert.assertEquals(cloneIndex.getAllKeys().size(), 3);
		Assert.assertEquals(cloneIndex.getAllEntries().size(), 4);
		
		Assert.assertTrue(cloneIndex.getAllEntries().contains(cf1));
		Assert.assertTrue(cloneIndex.getAllEntries().contains(cf11));
		Assert.assertTrue(cloneIndex.getAllEntries().contains(cf2));
		Assert.assertTrue(cloneIndex.getAllEntries().contains(cf3));
		
		/*
		String indexExportFileName = indexIOConfiguration.getIOLocation();
		try {
			BufferedReader br = new BufferedReader(new FileReader(indexExportFileName));
			String line = null;
			while( (line = br.readLine()) != null){
				System.out.println(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	
	@Test
	public void testIndexImport(){
		String indexExportFileName = indexIOConfiguration.getIOLocation();
				
		String attributes [] = indexExportFileName.split(XMLIndexIOConfiguration.ATTRIBUTE_SEPERATOR);
		
		String project = attributes[1];
		String language = attributes[2];
		String granularity = attributes[3];
		String transformation = attributes[4].replaceAll(".xml", "");
		
		ICloneIndex importedcloneIndex = new MemoryCloneIndexByJavaCollection(project, language, granularity, transformation);
		
		importedcloneIndex.importIndex(indexIOConfiguration);
		
		Assert.assertEquals(cloneIndex.getAllKeys().size(), importedcloneIndex.getAllKeys().size());
		Assert.assertEquals(cloneIndex.getAllEntries().size(), importedcloneIndex.getAllEntries().size());
		
		Assert.assertTrue(importedcloneIndex.getAllEntries().contains(cf1));
		Assert.assertTrue(importedcloneIndex.getAllEntries().contains(cf11));
		Assert.assertTrue(importedcloneIndex.getAllEntries().contains(cf2));
		Assert.assertTrue(importedcloneIndex.getAllEntries().contains(cf3));
	}
	
	
	@AfterClass
	public static void cleanup(){
		String indexExportFileName = indexIOConfiguration.getIOLocation();
		FileUtil.deleteDirectory(new File(indexExportFileName).getParent());
	}
	
}
