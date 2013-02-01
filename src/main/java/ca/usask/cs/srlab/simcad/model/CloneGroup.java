package ca.usask.cs.srlab.simcad.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class CloneGroup extends CloneSet {

	@SuppressWarnings("unused")
	private CloneGroup(){}
	
	public Integer getGroupId() {
		return getCloneSetId();
	}

	public CloneGroup(List<ICloneFragment> cloneFragments, String cloneType,
			Integer groupId) {
		super(groupId, cloneFragments, cloneType);
	}
}
