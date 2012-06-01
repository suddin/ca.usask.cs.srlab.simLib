package ca.usask.cs.srlab.simcad.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;

public final class PropsUtil {
		
	private static Properties properties = new Properties();
	
	static {
		InputStream userConfig = Environment.getResourceAsStream(Constants.EXTERNAL_CONFIGURATION_FILE);
		InputStream localConfig = Environment.getResourceAsStream(Constants.LOCAL_CONFIGURATION_FILE);
			if(localConfig == null)
				throw new RuntimeException("Unable to load default simcad configuration file :"+Constants.LOCAL_CONFIGURATION_FILE);

		try {
			properties.loadFromXML(localConfig);
			localConfig.close();

			// overrides the local configuration
			if (userConfig != null) {
				try{
					System.out.println("Found external simcad configuration, overriding defaults...\n");
					properties.loadFromXML(userConfig);
					
					validateRuntimeConfiguration();
					
					userConfig.close();
					
				}catch (Exception e) {
					throw new SimcadException("Unable to load external simcad configuration", e);
				}
			}else{
				System.out.println("No external simcad configuration available, using default configuration...\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new SimcadException(e);
		}

		//validateRuntimeConfiguration();
	}	
	
	private PropsUtil(){}
	
	private static void validateRuntimeConfiguration() {
		if(getType3cloneSimthreshold() > Constants.TYPE3CLONE_SIMTHRESHOLD_MAX_VAL
				|| getType3cloneSimthreshold() < Constants.TYPE3CLONE_SIMTHRESHOLD_MIM_VAL)
			throw new SimcadException("Invalid value for property : " + Constants.TYPE3CLONE_SIMTHRESHOLD);
		
		String simcadInstallationUrl = PropsUtil.getSimcadInstallUrl();
		String simLibRoot = Environment.getSimLibRoot();//Environment.getResourcePath("");//Environment.getSimLibRoot();
		
		simLibRoot = simLibRoot.replace(" ", "\\ ");
		
		String[] cmd = {
				"/bin/sh",
				"-c",
				"cd "+simLibRoot +
				"\ncd "+simcadInstallationUrl+
				"\n ./simcad2 -version"
				};
		try {
			int exitVal = SimCadCommandRunner.INSTANCE.executeCommand(cmd, false);
			
			if(exitVal == 127){
				throw new SimcadException("Invalid simcad location on configuration file, location must be absolute or relative to simcad2.jar");
			}
			
			if(exitVal > 99){
				throw new SimcadException("Incomplete simcad installation, please review the installation");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new SimcadException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new SimcadException(e);
		}
		
	}

	public static boolean isStrictOnMembership(){
		return Boolean.valueOf(properties.getProperty(Constants.STRICT_ON_MEMBERSHIP, "false"));
	}
	
	public static Double getClusterMembershipRatio(){
		return Double.valueOf(properties.getProperty(Constants.CLUSTER_MEMBERSHIP_RATIO, "0.5"));
	}
	
	public static Double getLocTolerance(){
		return Double.valueOf(properties.getProperty(Constants.LOC_TOLERANCE, "1.0"));
	}
	
	public static Integer getMinClusterSize(){
		return Integer.valueOf(properties.getProperty(Constants.MIN_CLUSTER_SIZE, "2"));
	}
	
	public static Integer getMinSizeOfGranularity(){
		return Integer.valueOf(properties.getProperty(Constants.MIN_SIZE_OF_GRANULARITY, "5"));
	}
	
	public static Boolean getIsFragmentFileRelativeURL(){
		return Boolean.valueOf(properties.getProperty(Constants.FRAGMENT_FILE_RELATIVEURL, "true"));
	}
	
	public static Integer getType3cloneSimthreshold(){
		return Integer.valueOf(properties.getProperty(Constants.TYPE3CLONE_SIMTHRESHOLD));
	}
	
	public static String getTokenBuilderClassName(){
		return properties.getProperty(Constants.TOKEN_BUILDER);
	}
	
	public static String getRegularHashProviderClassName(){
		return properties.getProperty(Constants.REGULAR_HASH_PROVIDER);
	}
	
	public static String getIndexHolderClassName(){
		return properties.getProperty(Constants.INDEX_HOLDER);
	}
	
	public static String getSimcadInstallUrl(){
		return properties.getProperty(Constants.SIMCAD_INSTALL_URL);
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}

	public static Double thresholdStabilizationValueForGreedyTransform() {
		return Double.valueOf(properties.getProperty(Constants.THRESHOLD_STABILIZATION_VALUE_FOR_GREEDY_TRANSFORM, "0.3"));
	}
	
	public static Integer thresholdStabilizationApplyOnThresholdOver() {
		return Integer.valueOf(properties.getProperty(Constants.THRESHOLD_STABILIZATION_APPLY_ON_THRESHOLD_OVER, "5"));
	}

	public static boolean isUnicodeFilterOn() {
		return Boolean.valueOf(properties.getProperty(Constants.UNICODE_FILTER_ON, "false"));
	}
	
	public static String getDbConfigFileUrl(){
		return properties.getProperty(Constants.DATABASE_CONFIG_URL);
	}
	
}
