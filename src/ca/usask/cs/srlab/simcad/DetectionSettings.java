package ca.usask.cs.srlab.simcad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import ca.usask.cs.srlab.simcad.model.CloneSet;

@Entity
public class DetectionSettings {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	//final Integer simThreshold;
	@Column(length=10)
	private String language;
	
	@IndexColumn(name = "cloneTypeIndex")
	@CollectionOfElements
	private String[] cloneTypes; 
	
	@Column (length=10)
	private String cloneGranularity; //function or block
	
	@Column (length=10)
	private String cloneSetType; //group, pair

	@Column (length=10)
	private String sourceTransformation;

	@Transient
	private boolean verbose;
	
	@Transient
	private DetectionReport detectionReport;
	
	protected DetectionSettings(){};
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DetectionSettings) {
			DetectionSettings another = (DetectionSettings) obj;
			return another.getLanguage().equals(language)
					&& another.getCloneGranularity().equals(cloneGranularity)
					&& another.getCloneSetType().equals(cloneSetType)
					&& another.getTypeString().equals(getTypeString())
					&& another.getSourceTransformation().equals(sourceTransformation);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int h = language.hashCode();
		h = (int) (31 * h + cloneGranularity.hashCode());
		h = (int) (31 * h + cloneSetType.hashCode());
		h = (int) (31 * h + getTypeString().hashCode());
		h = 31 * h + sourceTransformation.hashCode();
		return h;
	}
	
}
