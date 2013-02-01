package ca.usask.cs.srlab.simcad;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class SimCadConstants {

	public static final String VERSION = "1.0";

	public static final String EXTERNAL_CONFIGURATION_FILE 	= "simcad.cfg.xml";
	public static final String LOCAL_CONFIGURATION_FILE 	= "default.cfg.xml";
	public static final String DEFAULT_OUTPUT_FOLDER_SUFFIX = "_simcad_clones";
	
	public static final String STRICT_ON_MEMBERSHIP 	= "simcad.settings.advance.strictOnMembership";
	public static final String CLUSTER_MEMBERSHIP_RATIO = "simcad.settings.advance.clusterMembershipRatio";
	public static final String LOC_TOLERANCE 			= "simcad.settings.advance.locTolerance";
	public static final String THRESHOLD_STABILIZATION_VALUE_FOR_GREEDY_TRANSFORM 	= "simcad.settings.advance.thresholdStabilizationValueForGreedyTransform";
	public static final String THRESHOLD_STABILIZATION_APPLY_ON_THRESHOLD_OVER 		= "simcad.settings.advance.thresholdStabilizationApplyOnThresholdOver";
	
	public static final String TYPE3CLONE_SIMTHRESHOLD 	= "simcad.settings.advance.type3clone.simthreshold";
	
	public static final int TYPE3CLONE_SIMTHRESHOLD_MAX_VAL = 13;
	public static final int TYPE3CLONE_SIMTHRESHOLD_MIM_VAL = 1;

	public static final String MIN_CLUSTER_SIZE 			= "simcad.settings.general.minClusterSize";
	public static final String MIN_SIZE_OF_GRANULARITY 		= "simcad.settings.general.minSizeOfGranularity";
	public static final String FRAGMENT_FILE_RELATIVEURL 	= "simcad.settings.general.fragment.file.relativeURL";
	public static final String UNICODE_FILTER_ON 			= "simcad.settings.general.fragment.unicodeFilterOn";

	public static final String REGULAR_HASH_PROVIDER 	= "simcad.settings.general.preprocess.regularhash_class";
	public static final String TOKEN_BUILDER	 		= "simcad.settings.general.preprocess.tokenbuilder_class";
	public static final String INDEX_HOLDER 			= "simcad.settings.general.index.indexholder_class";
	public static final String SIMCAD_INSTALL_URL 		= "simcad.settings.general.install.url";
	
	public static final String DATABASE_CONFIG_URL 		= "simcad.settings.general.dbconfig.url";

	public static final String CLONE_GRANULARITY_FUNCTION 	= "function";
	public static final String CLONE_GRANULARITY_BLOCK 	= "block";

	public static final String CLONE_SET_TYPE_GROUP = "group";
	public static final String CLONE_SET_TYPE_PAIR 	= "pair";

	public static final String LANGUAGE_JAVA = "java";
	public static final String LANGUAGE_C = "c";
	public static final String LANGUAGE_CS = "cs";
	public static final String LANGUAGE_PYTHON = "py";
	
	public static final Multimap<String, String> LANG_EXTENSION_MAP = ImmutableMultimap
			.of(LANGUAGE_JAVA, "java", LANGUAGE_C, "c", LANGUAGE_C, "cpp", LANGUAGE_CS, "cs", LANGUAGE_PYTHON, "py");

	public static final String SOURCE_TRANSFORMATION_APPROACH_GREEDY = "greedy";
	public static final String SOURCE_TRANSFORMATION_APPROACH_GENEROUS = "generous";

	public static final String SOURCE_TRANSFORMATION_ACTION_BLIND = "blind";
	public static final String SOURCE_TRANSFORMATION_ACTION_CONSISTENT = "consistent";

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

}
