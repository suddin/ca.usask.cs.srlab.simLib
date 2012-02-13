package ca.usask.cs.srlab.simcad.model;

import java.util.List;

/**
 * @author sharif
 * 
 */
public interface ICloneSet {

	List<CloneFragment> getCloneFragments();

	ICloneType getCloneSetType();
	
	void setCloneSetType(ICloneType cloneType);

	Integer getCloneSetId();
	
	//String getTitle();

	int size();

	ICloneFragment getMember(int i);

	boolean isSubsumed();

	void setSubsumed(boolean subsumed);
}
