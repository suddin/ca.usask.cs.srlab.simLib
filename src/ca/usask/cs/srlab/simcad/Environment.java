package ca.usask.cs.srlab.simcad;

import java.io.InputStream;

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

}
