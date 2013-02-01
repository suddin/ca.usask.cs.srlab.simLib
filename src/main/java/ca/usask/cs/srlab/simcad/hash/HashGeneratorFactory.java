package ca.usask.cs.srlab.simcad.hash;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;

public class HashGeneratorFactory {

	public static IRegularHashGenerator LoadRegularHashgenerator() {
		String className = Environment.getRegularHashGeneratorClass();
		if(className == null)
			return new DefaultRegularHashGenerator();
		else{
			try {
				return (IRegularHashGenerator) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			}
		}
	}

}
