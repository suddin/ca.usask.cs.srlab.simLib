package ca.usask.cs.srlab.simcad.dataprovider;


public abstract class AbstractFragmentDataProviderConfiguration  implements IFragmentDataProviderConfiguration {

	protected String sourceDaraRootUrl;
	protected String sourceFragmentType; //granularity: function or block
	
	@SuppressWarnings("unused")
	private AbstractFragmentDataProviderConfiguration(){
	}
	
	protected AbstractFragmentDataProviderConfiguration(String sourceDaraRootUrl, String sourceFragmentType) {
		this.sourceDaraRootUrl = sourceDaraRootUrl;
		this.sourceFragmentType = sourceFragmentType;
	}

	@Override
	public String getSourceDaraRootUrl() {
		return sourceDaraRootUrl;
	}
	
	@Override
	public String getSourceFragmentType(){
		return sourceFragmentType;
	}
}