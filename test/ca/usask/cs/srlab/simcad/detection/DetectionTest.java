package ca.usask.cs.srlab.simcad.detection;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.dataprovider.ICloneFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLCloneFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.IndexBuilder;
import ca.usask.cs.srlab.simcad.index.MemoryCloneIndexByJavaCollection;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;

public class DetectionTest {

	static CloneDetector cloneDetector; 
	static ICloneIndex cloneIndex;
	static IndexBuilder indexBuilder;
	
	static DetectionSettings detectionSettings_type1_group;
	static DetectionSettings detectionSettings_nearmiss_group;
	
	static DetectionSettings detectionSettings_type1_pair;
	static DetectionSettings detectionSettings_nearmiss_pair;
	
	
	@BeforeClass
	public static void setup(){
		detectionSettings_type1_group = new DetectionSettings(0, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		detectionSettings_nearmiss_group = new DetectionSettings(13, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		
		detectionSettings_type1_pair = new DetectionSettings(0, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_PAIR);
		detectionSettings_nearmiss_pair = new DetectionSettings(13, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_PAIR);
		
		
		cloneIndex = new MemoryCloneIndexByJavaCollection();
		
		String sourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml");// DetectionTest.class.getClassLoader().getResource("test_functions.xml").getPath();
		
		XMLFragmentDataProviderConfiguration dataProviderConfig = new XMLFragmentDataProviderConfiguration(sourceFragmentFile, Constants.CLONE_GRANULARITY_FUNTIONS);
		dataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		
		ICloneFragmentDataProvider cloneFragmentDataProvider = new XMLCloneFragmentDataProvider(dataProviderConfig);
		//ICloneFragmentDataProvider cloneFragmentDataProvider = new XMLCloneFragmentDataProvider(dataProviderConfig, detectionSettings_type1);
		
		indexBuilder = new IndexBuilder(cloneFragmentDataProvider);
		
	}
	
	@Before
	public void subsetup(){
		cloneIndex.cleanup();
		indexBuilder.buildCloneIndex(cloneIndex);
	}
	
	@Test
	public void testType1CloneGroup(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_type1_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(3, result.get(0).size());
	}
	
	@Test
	public void testNearMissCloneGroup(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_nearmiss_group);
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
	public void testType1ClonePair(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_type1_pair);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(3, result.size());
	}
	
	@Test
	public void testNearMissClonePair(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_nearmiss_pair);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
		Assert.assertEquals(6, result.size());
	}
	
}
