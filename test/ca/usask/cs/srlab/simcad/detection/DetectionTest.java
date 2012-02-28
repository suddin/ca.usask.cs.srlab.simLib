package ca.usask.cs.srlab.simcad.detection;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.IndexBuilder;
import ca.usask.cs.srlab.simcad.index.MemoryCloneIndexByGoogleCollection;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;

public class DetectionTest {

	protected static CloneDetector cloneDetector; 
	protected static ICloneIndex cloneIndex;
	protected static IndexBuilder indexBuilder;
	
	protected static DetectionSettings detectionSettings_type1_group;
	protected static DetectionSettings detectionSettings_type2_group;
	protected static DetectionSettings detectionSettings_type3_group;
	protected static DetectionSettings detectionSettings_type12_group;
	protected static DetectionSettings detectionSettings_type23_group;
	protected static DetectionSettings detectionSettings_type13_group;
	protected static DetectionSettings detectionSettings_type123_group;

	
	protected static DetectionSettings detectionSettings_nearmiss_group;
	
	protected static DetectionSettings detectionSettings_type1_pair;
	protected static DetectionSettings detectionSettings_nearmiss_pair;
	
	
	@BeforeClass
	public static void setup(){
		detectionSettings_type1_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP, Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true, CloneSet.CLONE_TYPE_1);
		detectionSettings_type2_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, false, CloneSet.CLONE_TYPE_2);
		detectionSettings_type3_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, false, CloneSet.CLONE_TYPE_3);
		detectionSettings_type12_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, false, CloneSet.CLONE_TYPE_12);
		detectionSettings_type23_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, false, CloneSet.CLONE_TYPE_23);
		detectionSettings_type13_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, false, CloneSet.CLONE_TYPE_13);
		detectionSettings_type123_group = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP,  Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true,  CloneSet.CLONE_TYPE_123);
		
		//detectionSettings_nearmiss_group = new DetectionSettings(13, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		//detectionSettings_type1_pair = new DetectionSettings(0, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_PAIR);
		//detectionSettings_nearmiss_pair = new DetectionSettings(13, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_PAIR);
		
		
		cloneIndex = new MemoryCloneIndexByGoogleCollection();
		
//		String sourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml");// DetectionTest.class.getClassLoader().getResource("test_functions.xml").getPath();
//		
//		XMLFragmentDataProviderConfiguration dataProviderConfig = new XMLFragmentDataProviderConfiguration(sourceFragmentFile, Constants.CLONE_GRANULARITY_FUNTIONS);
//		dataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
//		
//		ICloneFragmentDataProvider cloneFragmentDataProvider = new XMLCloneFragmentDataProvider(dataProviderConfig);
//		//ICloneFragmentDataProvider cloneFragmentDataProvider = new XMLCloneFragmentDataProvider(dataProviderConfig, detectionSettings_type1);
		
		String sourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml");
		String transformedSourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_transformed_functions.xml");
		
		XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(sourceFragmentFile, transformedSourceFragmentFile, Environment.getResourcePath(""), Constants.CLONE_GRANULARITY_FUNTION);
		dataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		
		IFragmentDataProvider cloneFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(dataProviderConfig);
		
		indexBuilder = new IndexBuilder(cloneFragmentDataProvider);
		
	}
	
	@Before
	public void subsetup(){
		cloneIndex.cleanup();
	}
	
	@Test
	public void testType1CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type1_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type1_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(2, result.get(0).size());
	}
	
	@Test
	public void testType2CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type2_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type2_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(3, result.get(0).size());
	}
	
	
	@Test
	public void testType3CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type3_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type3_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(3, result.get(0).size());
	}
	
	
	@Test
	public void testType12CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type12_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type12_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(3, result.get(0).size());
	}
	
	
	@Test
	public void testType23CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type23_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type23_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(3, result.get(0).size());
		Assert.assertEquals(3, result.get(1).size());
	}
	
	
	@Test
	public void testType123CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type123_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type123_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(3, result.get(0).size());
		Assert.assertEquals(3, result.get(1).size());
	}
	
	//@Test
	public void testNearMissCloneGroup(){
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_nearmiss_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(3, result.get(0).size());
		Assert.assertEquals(3, result.get(1).size());
	}
	
	//@Test
	public void testType1ClonePair(){
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type1_pair);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(3, result.size());
	}
	
	//@Test
	public void testNearMissClonePair(){
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_nearmiss_pair);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(6, result.size());
	}
	
}
