package ca.usask.cs.srlab.simcad.dataprovider;


public abstract class AbstractFragmentDataProviderConfiguration  implements IFragmentDataProviderConfiguration {

	protected boolean forceExtract;

	protected String sourceDaraRootUrl;
	protected String sourceFragmentType; //granularity: function or block
	
	@SuppressWarnings("unused")
	private AbstractFragmentDataProviderConfiguration(){
	}
	
	protected AbstractFragmentDataProviderConfiguration(String sourceDaraRootUrl, String sourceFragmentType){
		this(sourceDaraRootUrl, sourceFragmentType, false);
	}
	
	protected AbstractFragmentDataProviderConfiguration(String sourceDaraRootUrl, String sourceFragmentType, boolean forceExtract) {
		this.forceExtract = forceExtract;
		if(sourceDaraRootUrl!=null && sourceDaraRootUrl.endsWith("/"))
			sourceDaraRootUrl = sourceDaraRootUrl.substring(0, sourceDaraRootUrl.length()-1);		
		this.sourceDaraRootUrl = sourceDaraRootUrl;
		this.sourceFragmentType = sourceFragmentType;
	}
	
	@Override
	public boolean isForceExtract() {
		return forceExtract;
	}

	@Override
	public String getSourceDataRootUrl() {
		return sourceDaraRootUrl;
	}
	
	@Override
	public String getSourceFragmentType(){
		return sourceFragmentType;
	}
}