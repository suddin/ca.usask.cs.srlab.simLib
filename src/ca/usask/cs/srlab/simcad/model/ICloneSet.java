package ca.usask.cs.srlab.simcad.model;

import java.util.List;

/**
 * @author sharif
 * 
 */
public interface ICloneSet {

	List<ICloneFragment> getCloneFragments();
	
	void setCloneFragments(List<? extends ICloneFragment> cloneFragments);
	
	String getCloneType();
	
	void setCloneType(String cloneType);

	Integer getCloneSetId();
	
	//String getTitle();

	int size();

	ICloneFragment getMember(int i);

	boolean isSubsumed();

	void setSubsumed(boolean subsumed);
}
