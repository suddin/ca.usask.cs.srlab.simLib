package ca.usask.cs.srlab.simcad.dataprovider.xml;

import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProviderConfiguration;

public abstract class AbstractFragmentDataProviderConfiguration  implements IFragmentDataProviderConfiguration {

	protected String cloneFragmentType;
	
	@SuppressWarnings("unused")
	private AbstractFragmentDataProviderConfiguration(){
	}
	
	protected AbstractFragmentDataProviderConfiguration(String cloneFragmentType) {
		this.cloneFragmentType = cloneFragmentType;
	}

	@Override
	public String getSourceFragmentType(){
		return cloneFragmentType;
	}
}