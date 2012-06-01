package ca.usask.cs.srlab.simcad.model;

import java.util.Arrays;

import javax.persistence.Entity;

/**
 * @author sharif
 *
 */
@Entity
public class ClonePair extends CloneSet {

	@SuppressWarnings("unused")
	private ClonePair(){};
	
	public Integer getPairId() {
		return getCloneSetId();
	}

	public ClonePair(ICloneFragment cloneFragment1, ICloneFragment cloneFragment2, String cloneType,
			Integer pairId) {
		super(pairId, Arrays.asList(cloneFragment1, cloneFragment2), cloneType);
	}
	
	public int getPairHammingDistance() {
		return this.getCloneFragments() != null
				&& this.getCloneFragments().size() < 2 ? Long.bitCount(this
				.getCloneFragments().get(0).getSimhash1()
				^ this.getCloneFragments().get(1).getSimhash1()) : -1;
	}
}
