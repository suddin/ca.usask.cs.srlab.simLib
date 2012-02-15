package ca.usask.cs.srlab.simcad.token;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;

public class TokenBuilderFactory {

//	public static ITokenBuilder LoadTokenBuilder(DetectionSettings ds) {
//		String className = Environment.getTokenBuilderClass();
//		if(className == null)
//			if(ds.getSimThreshold() == 0)
//				return new DefaultTokenBuilder();
//			else
//				return new TokenBuilderForType1Clone();
//		else{
//			return buildInstance(className);
//		}
//	}
	
	public static ITokenBuilder LoadTokenBuilder() {
		String className = Environment.getTokenBuilderClass();
		if(className == null)
			return new DefaultTokenBuilder();
		else{
			return buildInstance(className);
		}
	}
	
	public static ITokenBuilder LoadTokenBuilder(String tokenTokenBuilderClassName){
		return buildInstance(tokenTokenBuilderClassName);
	}
	
	private static ITokenBuilder buildInstance(String className) {
		try {
			return (ITokenBuilder) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to load TokenBuilder : "+ className , e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to load TokenBuilder : "+ className , e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to load TokenBuilder : "+ className , e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to load TokenBuilder : "+ className , e);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to load TokenBuilder : "+ className , e);
		}
	}

}
