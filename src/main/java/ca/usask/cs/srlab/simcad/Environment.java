package ca.usask.cs.srlab.simcad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;

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
		return PropsUtil.getProperty(SimCadConstants.REGULAR_HASH_PROVIDER);
	}

	public static String getTokenBuilderClass() {
		return PropsUtil.getProperty(SimCadConstants.TOKEN_BUILDER);
	}

	public static String getIndexHolderClass() {
		return PropsUtil.getIndexHolderClassName();
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
		if (classLoader != null) {
			stream = ClassLoader.getSystemClassLoader().getResourceAsStream(stripped);
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
		
		if(stream == null){
			//System.out.println(resource + " not available in classpath, checking outside classpath...");
			String path = getExternalResourcePath(stripped);
			if(path!=null)
				try {
					stream = new FileInputStream(path);
				} catch (FileNotFoundException e) {
				}
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
	
	public static String getExternalResourcePath(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
		String resourceURL = null;
		
		URL baseUrl = Environment.class.getProtectionDomain().getCodeSource().getLocation();
		if(baseUrl == null){
			baseUrl = ClassLoader.getSystemClassLoader().getResource(".");
			if(baseUrl == null){
				baseUrl = Environment.class.getClassLoader().getResource(".");
				if(baseUrl == null){
					return null;
				}
			}
		}
		
		File base = new File(baseUrl.getFile());
		
		do{
			resourceURL = base.getPath().concat(System.getProperty("file.separator") + stripped);
			if(new File(resourceURL).exists())
				return resourceURL;
			else{
				if(base.getParent()==null || base.getParent().isEmpty())
					break;
				else{
					base = new File(base.getParent());
					//System.out.println(base.getPath());
				}
			}
		}while(true);
		
		return null;
	}

//	CodeSource src = VehicleFactory.class.getProtectionDomain().getCodeSource();
//	if (src == null) {
//	 return null;
//	}
//	return new File(src.getLocation().getFile()).getParent();
	
	public static String getSimLibRoot() {
		try {
			CodeSource codeSource = Environment.class.getProtectionDomain().getCodeSource();
			if (codeSource == null) {
			 return null;
			}
			return new File(codeSource.getLocation().getFile()).getParent();
//			File root = new File(codeSource.getLocation().toURI().getPath());
//			if(root.isDirectory())
//				return root.getAbsolutePath();
//			else
//				return root.getParent();
		} catch (/*URISyntax*/Exception e) {
			e.printStackTrace();
			throw new SimcadException(e);
		}
		//return new File(".").getAbsoluteFile().getParent();
	}
	
}
