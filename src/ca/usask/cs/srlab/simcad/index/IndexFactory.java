package ca.usask.cs.srlab.simcad.index;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;

public class IndexFactory {

	public static ICloneIndex LoadIndexHolder() {
		String className = Environment.getIndexHolderClass();
		if(className == null)
			return new MemoryCloneIndexByJavaCollection();
		else{
			try {
				return (ICloneIndex) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load CloneIndex : "+ className , e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load CloneIndex : "+ className , e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load CloneIndex : "+ className , e);
			}
		}
	}

}
