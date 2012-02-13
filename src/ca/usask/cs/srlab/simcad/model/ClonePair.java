package ca.usask.cs.srlab.simcad.model;

import java.util.Arrays;

/**
 * @author sharif
 *
 */
public class ClonePair extends CloneSet {

	private Long id;

	public Long getId() {
		return id;
	}

	public Integer getPairId() {
		return getCloneSetId();
	}

	public ClonePair(CloneFragment cloneFragment1, CloneFragment cloneFragment2, String cloneType,
			Integer pairId) {
		super(pairId, Arrays.asList(cloneFragment1, cloneFragment2), cloneType);
	}
	
}
