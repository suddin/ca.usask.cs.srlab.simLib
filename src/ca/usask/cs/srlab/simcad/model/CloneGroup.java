package ca.usask.cs.srlab.simcad.model;

import java.util.List;

public class CloneGroup extends CloneSet {

	public Integer getGroupId() {
		return getCloneSetId();
	}

	public CloneGroup(List<ICloneFragment> cloneFragments, String cloneType,
			Integer groupId) {
		super(groupId, cloneFragments, cloneType);
	}

//	@Override
//	public String getTitle() {
//		return "CloneGroup :" + getCloneSetId();
//	}

}
