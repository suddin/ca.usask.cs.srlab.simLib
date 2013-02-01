package ca.usask.cs.srlab.simcad.dbobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.model.CloneSet;

@Entity
public class DetectionResultObject {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@Cascade(value=org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private DetectionSettings detectionSettings;
	
	@OneToMany
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	private List<CloneSet> outputClones;
	
	protected DetectionResultObject(){}
	
	public DetectionResultObject(DetectionSettings detectionSettings,
			Collection<CloneSet> inputCloneSets) {
		this.detectionSettings = detectionSettings;
		this.outputClones = new ArrayList<CloneSet>(inputCloneSets);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DetectionSettings getDetectionSettings() {
		return detectionSettings;
	}
	public void setDetectionSettings(DetectionSettings detectionSettings) {
		this.detectionSettings = detectionSettings;
	}
	public List<CloneSet> getOutputClones() {
		return outputClones;
	}
	public void setOutputClones(List<CloneSet> outputClones) {
		this.outputClones = outputClones;
	}
	public void clearOutputClones() {
		if(this.outputClones != null)
			this.outputClones.clear();
	}

	public void addAllOutputClones(Collection<CloneSet> inputCloneSets) {
		if(this.outputClones == null)
			this.outputClones = new ArrayList<CloneSet>(inputCloneSets);
		else
			this.outputClones.addAll(inputCloneSets);
	}
}
