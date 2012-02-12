package ca.usask.cs.srlab.simcad.dataprovider;

import ca.usask.cs.srlab.simcad.hash.HashGeneratorFactory;
import ca.usask.cs.srlab.simcad.hash.IRegularHashGenerator;
import ca.usask.cs.srlab.simcad.hash.SimhashGenerator;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.token.ITokenBuilder;
import ca.usask.cs.srlab.simcad.token.TokenBuilderFactory;

public abstract class AbstractDataProvider implements ICloneFragmentDataProvider{

	protected IFragmentDataProviderConfiguration dataProviderConfig;
	protected SimhashGenerator simhashGenerator;
	
	protected AbstractDataProvider() {
	}
	
	protected AbstractDataProvider(IFragmentDataProviderConfiguration dataProviderConfig, DetectionSettings ds) {
		ITokenBuilder tokenBuilder;
		if(ds != null )
			tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(ds);
		else
			tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		this.dataProviderConfig = dataProviderConfig;
		IRegularHashGenerator regularHashgenerator = HashGeneratorFactory.LoadRegularHashgenerator();
		this.simhashGenerator = new SimhashGenerator(tokenBuilder, regularHashgenerator);
	}
	
	protected abstract Object applyDataTransformation();
}
