package ca.usask.cs.srlab.simcad;

import java.io.InputStream;
import java.net.URL;

import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class Environment {

	

	public static boolean isTokenFrequencyNormalizationOn() {
		// TODO Auto-generated method stub
		return false;
	}

	public static int getTokenFrequencyNormalizationThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static int getTokenFrequencyNormalizationOverThresholdValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static String getRegularHashGeneratorClass() {
		return PropsUtil.getProperty(Constants.REGULAR_HASH_PROVIDER);
	}

	public static String getTokenBuilderClass() {
		return PropsUtil.getProperty(Constants.TOKEN_BUILDER);
	}
	
	public static int getType3cloneSimthreshold() {
		return PropsUtil.getType3cloneSimthreshold();
	}
	
	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
			// if(stream != null)
			// System.out.println(resource+
			// " got by Thread.currentThread().getContextClassLoader()");
		}
		if (stream == null) {
			stream = Environment.class.getResourceAsStream(resource);
			// if(stream != null)
			// System.out.println(resource+ " got by Environment.class");
		}
		if (stream == null) {
			stream = Environment.class.getClassLoader().getResourceAsStream(
					stripped);
			// if(stream != null)
			// System.out.println(resource+
			// " got by Environment.class.getClassLoader()");
		}
		return stream;
	}
	
	public static String getResourcePath(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		URL resourceURL = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			resourceURL = classLoader.getResource(stripped);
			// if(stream != null)
			// System.out.println(resource+
			// " got by Thread.currentThread().getContextClassLoader()");
		}
		if (resourceURL == null) {
			resourceURL = Environment.class.getResource(resource);
			// if(stream != null)
			// System.out.println(resource+ " got by Environment.class");
		}
		if (resourceURL == null) {
			resourceURL = Environment.class.getClassLoader().getResource(stripped);
			// if(stream != null)
			// System.out.println(resource+
			// " got by Environment.class.getClassLoader()");
		}
		return resourceURL !=null ? resourceURL.getPath() : null;
	}

}
