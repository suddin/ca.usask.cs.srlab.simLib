package ca.usask.cs.srlab.simcad.dataprovider;

import ca.usask.cs.srlab.simcad.hash.HashGeneratorFactory;
import ca.usask.cs.srlab.simcad.hash.IRegularHashGenerator;
import ca.usask.cs.srlab.simcad.hash.SimhashGenerator;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.token.ITokenBuilder;
import ca.usask.cs.srlab.simcad.token.TokenBuilderFactory;

public abstract class AbstractDataProvider implements ICloneFragmentDataProvider{

	protected SimhashGenerator simhashGenerator;
	
	protected AbstractDataProvider(){
	}
	
	protected void configure(DetectionSettings ds) {
		ITokenBuilder tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(ds);
		IRegularHashGenerator regularHashgenerator = HashGeneratorFactory.LoadRegularHashgenerator();
		this.simhashGenerator = new SimhashGenerator(tokenBuilder, regularHashgenerator);
	}
	
	
}
