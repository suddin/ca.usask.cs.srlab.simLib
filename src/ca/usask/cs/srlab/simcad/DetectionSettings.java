package ca.usask.cs.srlab.simcad;

public class DetectionSettings {

	//final Integer simThreshold;
	final String cloneTypes[]; 
	final String cloneGranularity; //function or block
	final String cloneSetType; //group, pair
	
	public DetectionSettings(String cloneGranularity,
			String cloneSetType, String ... cloneTypes) {
		super();
		//this.simThreshold = simThreshold;
		this.cloneTypes = cloneTypes;
		this.cloneGranularity = cloneGranularity;
		this.cloneSetType = cloneSetType;
	}

//	public Integer getSimThreshold() {
//		return simThreshold;
//	}

	public String[] getCloneTypes(){
		return cloneTypes;
	}
	
	public String getCloneGranularity() {
		return cloneGranularity;
	}

	public String getCloneSetType() {
		return cloneSetType;
	}
	
	public String getTypeString(){
		return "type" + (containsType(Constants.CLONE_TYPE_1) ? "-1" : "")
				+ (containsType(Constants.CLONE_TYPE_2) ? "-2" : "")
				+ (containsType(Constants.CLONE_TYPE_3) ? "-3" : "");
	}
	
	public boolean containsType(String cloneType){
		for(String ct : cloneTypes){
			if(ct.equals(cloneType))
				return true;
		}
		return false;
	}
}
