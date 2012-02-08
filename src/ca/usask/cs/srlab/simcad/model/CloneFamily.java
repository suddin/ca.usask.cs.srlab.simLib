
package ca.usask.cs.srlab.simcad.model;

import java.util.ArrayList;
import java.util.List;

public class CloneFamily {

    public static final String GRANULARITY_TYPE_FUNCTION = "F";
    public static final String GRANULARITY_TYPE_BLOCK = "B";

    public static final String TRANSFORMATION_TYPE_CONSISTENT_RENAME = "CR";
    public static final String TRANSFORMATION_TYPE_BLIND_RENAME = "BR";
    public static final String TRANSFORMATION_TYPE_NONE = "NO";


    Integer id;
    
    String granularityType;
    
    String transformationType;

    String threshold;
    
    List<ICloneSet> cloneSets;

    public CloneFamily() {
    }

    public CloneFamily(String granularityType, String transformationType, String threshold) {
        this.granularityType = granularityType;
        this.transformationType = transformationType;
        this.threshold = threshold;
    }

    public List<ICloneSet> getCloneSets() {
        return cloneSets;
    }

    public void setCloneGroups(List<ICloneSet> cloneSets) {
        this.cloneSets = cloneSets;
    }

    public String getGranularityType() {
        return granularityType;
    }

    public void setGranularityType(String granularityType) {
        this.granularityType = granularityType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getTransformationType() {
        return transformationType;
    }

    public void setTransformationType(String transformationType) {
        this.transformationType = transformationType;
    }

    public void addCloneSet(ICloneSet cloneSet){
        if(this.cloneSets == null)
        	this.cloneSets = new ArrayList<ICloneSet>();
        this.cloneSets.add(cloneSet);
    }

}
