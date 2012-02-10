package ca.usask.cs.srlab.simcad.postprocess;

public class DetectionSettings {

	final Integer simThreshold;
	final String cloneGranularity;
	final String cloneSetType; //function or block
	
	public DetectionSettings(Integer simThreshold, String cloneGranularity,
			String cloneSetType) {
		super();
		this.simThreshold = simThreshold;
		this.cloneGranularity = cloneGranularity;
		this.cloneSetType = cloneSetType;
	}

	public Integer getSimThreshold() {
		return simThreshold;
	}

	public String getCloneGranularity() {
		return cloneGranularity;
	}

	public String getCloneSetType() {
		return cloneSetType;
	}
}
