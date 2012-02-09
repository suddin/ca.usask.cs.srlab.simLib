package ca.usask.cs.srlab.simcad;

public class Constants {

	public static final String VERSION = "1.0";
	
	public static final String EXTERNAL_CONFIGURATION_FILE = "simcad.cfg.xml";
	public static final String LOCAL_CONFIGURATION_FILE = "default.cfg.xml";
	
	public static final String STRICT_ON_MEMBERSHIP ="simcad.settings.advance.strictOnMembership";
	public static final String CLUSTER_MEMBERSHIP_RATIO ="simcad.settings.advance.clusterMembershipRatio";
	public static final String LOC_TOLERANCE ="simcad.settings.advance.locTolerance";

	public static final String MIN_CLUSTER_SIZE ="simcad.settings.general.minClusterSize";
	public static final String MIN_SIZE_OF_GRANULARITY ="simcad.settings.general.minSizeOfGranularity";

	public static final String REGULAR_HASH_PROVIDER ="simcad.settings.general.preprocess.regularhash_class";
	public static final String TOKEN_BUILDER ="simcad.settings.general.preprocess.tokenbuilder_class";
	public static final String INDEX_BUILDER ="simcad.settings.general.index.indexbuilder_class";
	public static final String TXL_SCRIPT_URL ="simcad.settings.general.preprocess.txlscript.url";
	
}
