package ca.usask.cs.srlab.simcad.detection;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneType;

public class TypeMapper {

	public static void mapTypeFor(CloneSet cloneSet) {
		ICloneType cloneType = null;
		//TODO: determine type 
		cloneSet.setCloneSetType(cloneType);
	}

}
