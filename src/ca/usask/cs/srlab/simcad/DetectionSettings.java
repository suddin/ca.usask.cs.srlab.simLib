package ca.usask.cs.srlab.simcad;

import ca.usask.cs.srlab.simcad.model.CloneSet;

public class DetectionSettings {

	//final Integer simThreshold;
	final String language;
	final String cloneTypes[]; 
	final String cloneGranularity; //function or block
	final String cloneSetType; //group, pair
	final String sourceTransformation;
	final boolean verbose;
	DetectionReport detectionReport;
	
	public DetectionSettings(String language, String cloneGranularity,
			String cloneSetType, String sourceTransformation, boolean verbose, String ... cloneTypes) {
		super();
		this.language = language;
		this.cloneTypes = cloneTypes;
		this.cloneGranularity = cloneGranularity;
		this.cloneSetType = cloneSetType;
		this.sourceTransformation = sourceTransformation;
		this.verbose = verbose;
		detectionReport = new DetectionReport();
	}

//	public Integer getSimThreshold() {
//		return simThreshold;
//	}
	
	public String getLanguage() {
		return language;
	}

	public String[] getCloneTypes(){
		return cloneTypes;
	}
	
	public String getCloneGranularity() {
		return cloneGranularity;
	}

	public String getCloneSetType() {
		return cloneSetType;
	}
	
	public boolean isVerbose() {
		return verbose;
	}

	public DetectionReport getDetectionReport() {
		return detectionReport;
	}

	public String getSourceTransformation() {
		return sourceTransformation;
	}
	
	public String getTypeString(){
		return "type" + (containsType(CloneSet.CLONE_TYPE_1) ? "-1" : "")
				+ (containsType(CloneSet.CLONE_TYPE_2) ? "-2" : "")
				+ (containsType(CloneSet.CLONE_TYPE_3) ? "-3" : "");
	}
	
	public String getTypeStringDetail(){
		return (containsType(CloneSet.CLONE_TYPE_1) ? "Type-1" : "")
				+ (containsType(CloneSet.CLONE_TYPE_2) ? ", Type-2" : "")
				+ (containsType(CloneSet.CLONE_TYPE_3) ? ", Type-3" : "");
	}
	
	/**
	 * 
	 * @param cloneType : Type-1, Type-2, Type-3
	 * @return
	 */
	public boolean containsType(String cloneType){
		if(!cloneType.startsWith("Type"))
			new SimcadException("Invalid type string : "+ cloneType);
		for(String ct : cloneTypes){
			if(ct.equals(cloneType))
				return true;
		}
		return false;
	}
}
