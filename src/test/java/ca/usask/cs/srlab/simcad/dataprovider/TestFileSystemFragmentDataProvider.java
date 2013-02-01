package ca.usask.cs.srlab.simcad.dataprovider;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.SimCadConstants;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.model.CloneFragment;


public class TestFileSystemFragmentDataProvider {

	static IFragmentDataProvider cloneFragmentDataProvider ;
	
	@BeforeClass
	public static void setup(){
		String sourceFolder = "/Users/sharif/Documents/workspaces/pmd/MyLib";// DetectionTest.class.getClassLoader().getResource("test_functions.xml").getPath();
		FileSystemFragmentDataProviderConfiguration dataProviderConfig = new FileSystemFragmentDataProviderConfiguration(sourceFolder, sourceFolder+SimCadConstants.DEFAULT_OUTPUT_FOLDER_SUFFIX, SimCadConstants.LANGUAGE_JAVA, SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS, CloneFragment.CLONE_GRANULARITY_FUNCTION);
		cloneFragmentDataProvider = new FileSystemFragmentDataProvider(dataProviderConfig);
	}
	
	@Test
	public void testExtractFragments(){
		
		List<CloneFragment> extractedFragments = cloneFragmentDataProvider.extractFragments();
		
		for (CloneFragment cloneFragment : extractedFragments) {
			System.out.println(cloneFragment);
		}
	}
}
