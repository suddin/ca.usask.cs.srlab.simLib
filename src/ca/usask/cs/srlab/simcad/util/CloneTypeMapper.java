package ca.usask.cs.srlab.simcad.util;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;

public class CloneTypeMapper {

	private CloneTypeMapper(){};
	
	public static void mapTypeFor(CloneSet cloneSet) {
		String cloneType = Constants.CLONE_TYPE_1;

		int type2_vote = 0;
		int type3_vote = 0;

		ICloneFragment cloneFragment_i = cloneSet.getMember(0);
         
        for (int j = 1; j < cloneSet.size(); j++) {
        	ICloneFragment cloneFragment_j = cloneSet.getMember(j);
        	 
        	if (cloneFragment_i.getLineOfCode() != cloneFragment_j.getLineOfCode()
        			|| !cloneFragment_i.getSimhash1().equals(cloneFragment_j.getSimhash1())) {
        		 //if difference in line or hash code (note: line might be same in case of type 3 where a line has been replaced by another)
        		cloneType = Constants.CLONE_TYPE_3;
        		type3_vote++;
                break;
            }else {
            	//either type 1 (equal simhash and equal original source) or 2 (equal simhash but dissimilar original source)
            	if (cloneFragment_i.getSimhash1().equals(cloneFragment_j.getSimhash1())
            			&& FastStringComparator.INSTANCE.compare(cloneFragment_i.getOriginalCodeBlock(), cloneFragment_j.getOriginalCodeBlock()) != 0) {
            		type2_vote++;
            	}
            }
         } //for
         
         if (type3_vote == 0 && type2_vote > 0) {
             cloneType = Constants.CLONE_TYPE_2;
         }
         
         cloneSet.setCloneType(cloneType);
	}

}
