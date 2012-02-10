package ca.usask.cs.srlab.simcad.token;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;

public class TokenBuilderFactory {

	public static ITokenBuilder LoadTokenBuilder(DetectionSettings detectionSettings) {
		String className = Environment.getTokenBuilderClass();
		if(className == null)
			return new DefaultTokenBuilder(detectionSettings);
		else{
			try {
				Constructor<?> constructor = Class.forName(className).getConstructor(DetectionSettings.class);
				return (ITokenBuilder) constructor.newInstance(detectionSettings);
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new SimcadException("Unable to load RegularHashGenerator : "+ className , e);
			}
		}
		
	}

}
