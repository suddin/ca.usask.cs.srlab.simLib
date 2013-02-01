
package ca.usask.cs.srlab.simcad.model;

import java.util.ArrayList;
import java.util.List;

import ca.usask.cs.srlab.simcad.DetectionSettings;

public class CloneFamily {

    Integer id;
    
    DetectionSettings detectionSettings;
    
    List<CloneSet> cloneSets;
    
    public CloneFamily(DetectionSettings detectionSettings) {
        this.detectionSettings = detectionSettings;
    }

    public List<CloneSet> getCloneSets() {
        return cloneSets;
    }

    public void setCloneSets(List<CloneSet> cloneSets) {
        this.cloneSets = cloneSets;
    }

    public DetectionSettings getDetectionSettings() {
        return detectionSettings;
    }

    public void setDetectionSettings(DetectionSettings detectionSettings) {
        this.detectionSettings = detectionSettings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addCloneSet(CloneSet cloneSet){
        if(this.cloneSets == null)
        	this.cloneSets = new ArrayList<CloneSet>();
        this.cloneSets.add(cloneSet);
    }

}
