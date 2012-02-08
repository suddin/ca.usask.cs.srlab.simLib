package ca.usask.cs.srlab.simcad.dataprovider;

import java.util.List;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public interface ICloneFragmentDataProvider {

	//public ListIterator<CloneFragment> iterator();

	//public List<CloneFragment> getElements();
	
	public List<CloneFragment> extractFragments();
}
