package ca.usask.cs.srlab.simcad.index;

import java.util.List;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProvider;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class IndexBuilder {
	
	private IFragmentDataProvider cloneFragmentDataProvider;
	
	public IndexBuilder(IFragmentDataProvider cloneFragmentDataProvider){
		this.cloneFragmentDataProvider = cloneFragmentDataProvider;
	}
	
	public void buildCloneIndex(ICloneIndex cloneIndex, DetectionSettings detectionSettings){
		long startTime = System.currentTimeMillis();
		
		List<CloneFragment> extractedFragments = cloneFragmentDataProvider.extractFragments();
		
		detectionSettings.getDetectionReport().setnfCodeFragment(extractedFragments.size());
		
		//if(detectionSettings.isVerbose())
		System.out.println("Building clone index...\n");
		for(CloneFragment cloneFragment : extractedFragments)
			cloneIndex.insert(cloneFragment);
		
		long endTime = System.currentTimeMillis();
		long preprocessingTime  = endTime - startTime;
		detectionSettings.getDetectionReport().setPreprocessingTime(preprocessingTime);
	}
	
}
