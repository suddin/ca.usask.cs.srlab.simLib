package ca.usask.cs.srlab.simcad.dataprovider;


public abstract class AbstractFragmentDataProviderConfiguration  implements IFragmentDataProviderConfiguration {

	protected String sourceFragmentType; //granularity: function or block
	
	@SuppressWarnings("unused")
	private AbstractFragmentDataProviderConfiguration(){
	}
	
	protected AbstractFragmentDataProviderConfiguration(String sourceFragmentType) {
		this.sourceFragmentType = sourceFragmentType;
	}

	@Override
	public String getSourceFragmentType(){
		return sourceFragmentType;
	}
}