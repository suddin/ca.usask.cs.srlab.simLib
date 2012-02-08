package ca.usask.cs.srlab.simcad.model;

import java.util.List;

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

	public ClonePair(List<CloneFragment> cloneFragments, ICloneType cloneType,
			Integer pairId) {
		super(pairId, cloneFragments, cloneType);
	}
	
//	@Override
//	public String getTitle() {
//		return "ClonePair :" + getCloneSetId();
//	}
}
