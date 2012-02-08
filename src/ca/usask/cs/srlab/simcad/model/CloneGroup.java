package ca.usask.cs.srlab.simcad.model;

import java.util.List;

public class CloneGroup extends CloneSet {

	private Long id;

	public Long getId() {
		return id;
	}

	public Integer getGroupId() {
		return getCloneSetId();
	}

	public CloneGroup(List<CloneFragment> cloneFragments, ICloneType cloneType,
			Integer groupId) {
		super(groupId, cloneFragments, cloneType);
	}

//	@Override
//	public String getTitle() {
//		return "CloneGroup :" + getCloneSetId();
//	}

}
