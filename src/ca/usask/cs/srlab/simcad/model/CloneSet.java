package ca.usask.cs.srlab.simcad.model;

import java.util.List;

import ca.usask.cs.srlab.simcad.util.CloneTypeMapper;

/**
 * @author sharif
 *
 */
public abstract class CloneSet implements ICloneSet {

	private Integer cloneSetId;
	private List<CloneFragment> cloneFragments;
	private String cloneType;	
	
	public Integer getCloneSetId(){
		return cloneSetId;
	}
	
	public void reSetCloneSetId(int cloneSetId) {
		this.cloneSetId = cloneSetId;
	}
	
	public CloneSet(Integer cloneSetId, List<CloneFragment> cloneFragments,
			String cloneType) {
		super();
		this.cloneFragments = cloneFragments;
		this.cloneType = cloneType;
		this.cloneSetId = cloneSetId;
		
		CloneTypeMapper.mapTypeFor(this);
	}

	@Override
	public List<CloneFragment> getCloneFragments() {
		return cloneFragments;
	}

	@Override
	public String getCloneType() {
		return cloneType;
	}
	
	@Override
	public void setCloneType(String cloneType){
		this.cloneType = cloneType;
	}
	
	@Override
	public int size(){
		return cloneFragments.size();
	}

	@Override
	public ICloneFragment getMember(int i) {
		return cloneFragments.get(i);
	}

	private transient boolean subsumed;
	
	@Override
	public boolean isSubsumed() {
		return subsumed;
	}

	@Override
	public void setSubsumed(boolean subsumed) {
		this.subsumed = subsumed;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("clone "+ (this instanceof CloneGroup ? "group" : "pair") +": "+ cloneSetId+"\n");
		sb.append("number of fragments: "+ cloneFragments.size()+"\n");
		sb.append("clone type: "+ cloneType+"\n");
		for(CloneFragment cf : cloneFragments){
			sb.append("\n");
			sb.append(cf.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
