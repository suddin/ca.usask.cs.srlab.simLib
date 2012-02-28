package ca.usask.cs.srlab.simcad.model;

import java.util.Arrays;

/**
 * @author sharif
 *
 */
public class ClonePair extends CloneSet {

	public Integer getPairId() {
		return getCloneSetId();
	}

	public ClonePair(ICloneFragment cloneFragment1, ICloneFragment cloneFragment2, String cloneType,
			Integer pairId) {
		super(pairId, Arrays.asList(cloneFragment1, cloneFragment2), cloneType);
	}
	
}
