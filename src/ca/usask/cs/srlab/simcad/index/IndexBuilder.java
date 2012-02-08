package ca.usask.cs.srlab.simcad.index;

import ca.usask.cs.srlab.simcad.dataprovider.ICloneFragmentDataProvider;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class IndexBuilder {
	
	private ICloneFragmentDataProvider cloneFragmentDataProvider;
	
	public IndexBuilder(ICloneFragmentDataProvider cloneFragmentDataProvider){
		this.cloneFragmentDataProvider = cloneFragmentDataProvider;
	}
	
	public void buildCloneIndex(ICloneIndex cloneIndex){
		for(CloneFragment cloneFragment : cloneFragmentDataProvider.extractFragments())
			cloneIndex.insert(cloneFragment);
	}
	
}
