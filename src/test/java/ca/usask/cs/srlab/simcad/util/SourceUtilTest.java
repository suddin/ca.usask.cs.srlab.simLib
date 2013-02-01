package ca.usask.cs.srlab.simcad.util;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.SimCadConstants;
import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;

public class SourceUtilTest {

	static String testFile;
	static String testString;
	static DetectionSettings detectionSettings_function;
	static DetectionSettings detectionSettings_block;
	
	@BeforeClass
	public static void init(){
		testFile = ClassLoader.getSystemResource("ca/usask/cs/srlab/simcad/util/SourceUtilTestSample.txt").getFile();
		
		testString = FileUtil.getFileContents(testFile);
		
		detectionSettings_function = new DetectionSettings(SimCadConstants.LANGUAGE_JAVA, CloneFragment.CLONE_GRANULARITY_FUNCTION, SimCadConstants.CLONE_SET_TYPE_GROUP, SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true, CloneSet.CLONE_TYPE_1);
		
		detectionSettings_block = new DetectionSettings(SimCadConstants.LANGUAGE_JAVA, CloneFragment.CLONE_GRANULARITY_BLOCK, SimCadConstants.CLONE_SET_TYPE_GROUP, SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true, CloneSet.CLONE_TYPE_1);
	
	}
	
	@Test
	public void testGetCandidateFragmentFromFile(){
		Collection<CloneFragment> candidateFragments = SourceUtil.getCandidateFragmentsFromFile(new File(testFile), detectionSettings_function);
		Assert.assertEquals(2, candidateFragments.size());
	}

	@Test
	public void testGetCandidateFragmentFromFileWithLineRange(){
		Integer from = 24;
		Integer to = 30;
		Collection<CloneFragment> candidateFragments = SourceUtil.buildCandidateFragmentsFromFileWithRangeSelection(new File(testFile), from, to, detectionSettings_block);
		Assert.assertEquals(1, candidateFragments.size());
	}
	
	@Test
	public void testTopClip(){
		int actualLine = testString.split("\n").length;
		String result = SourceUtil.doTopClip(testString, 2);
		Assert.assertEquals(result.split("\n").length, actualLine-2);
	}
	
	@Test
	public void testBottomClip(){
		int actualLine = testString.split("\n").length;
		String result = SourceUtil.doBottomClip(testString, 5);
		Assert.assertEquals(result.split("\n").length, actualLine-5);
	}
	
}
