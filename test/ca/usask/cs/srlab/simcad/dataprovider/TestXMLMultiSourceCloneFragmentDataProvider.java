package ca.usask.cs.srlab.simcad.dataprovider;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.model.CloneFragment;


public class TestXMLMultiSourceCloneFragmentDataProvider {

	static IFragmentDataProvider cloneFragmentDataProvider ;
	
	@BeforeClass
	public static void setup(){
		String sourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml");// DetectionTest.class.getClassLoader().getResource("test_functions.xml").getPath();
		String transformedSourceFragmentFile = Environment.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_transformed_functions.xml");// DetectionTest.class.getClassLoader().getResource("test_functions.xml").getPath();
		
		XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(sourceFragmentFile, transformedSourceFragmentFile, Constants.CLONE_GRANULARITY_FUNTION);
		dataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		
		cloneFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(dataProviderConfig);
	}
	
	@Test
	public void testExtractFragments(){
		
		List<CloneFragment> extractedFragments = cloneFragmentDataProvider.extractFragments();
		
		for (CloneFragment cloneFragment : extractedFragments) {
			System.out.println(cloneFragment);
		}
	}
}
