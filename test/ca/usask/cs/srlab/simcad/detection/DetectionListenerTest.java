package ca.usask.cs.srlab.simcad.detection;

import java.util.Collection;
import java.util.List;

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

public class DetectionListenerTest {
	
	protected static CloneDetector cloneDetector; 
	protected static IndexBuilder indexBuilder;
	protected static ICloneIndex cloneIndex;
	protected static DetectionSettings detectionSettings;
	
	@BeforeClass
	public static void setup(){
		detectionSettings = new DetectionSettings(Constants.LANGUAGE_JAVA, Constants.CLONE_GRANULARITY_FUNTION, Constants.CLONE_SET_TYPE_GROUP, Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, true, CloneSet.CLONE_TYPE_1);
		cloneIndex = new MemoryCloneIndexByGoogleCollection();
		
		String sourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml");
		String transformedSourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_transformed_functions.xml");
		
		XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(sourceFragmentFile, transformedSourceFragmentFile,Environment.getResourcePath(""), Constants.CLONE_GRANULARITY_FUNTION);
		dataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		
		IFragmentDataProvider cloneFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(dataProviderConfig);
		
		indexBuilder = new IndexBuilder(cloneFragmentDataProvider);
	}
	
	
	@Test
	public void testType1CloneGroup(){
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
//		for (CloneSet cloneSet : result) {
//			System.out.println(cloneSet);
//		}
		
	}
}
