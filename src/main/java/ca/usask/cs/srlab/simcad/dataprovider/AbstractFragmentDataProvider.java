package ca.usask.cs.srlab.simcad.dataprovider;

import ca.usask.cs.srlab.simcad.hash.HashGeneratorFactory;
import ca.usask.cs.srlab.simcad.hash.IRegularHashGenerator;
import ca.usask.cs.srlab.simcad.hash.SimhashGenerator;
import ca.usask.cs.srlab.simcad.model.BlockCloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.token.ITokenBuilder;
import ca.usask.cs.srlab.simcad.token.TokenBuilderFactory;

public abstract class AbstractFragmentDataProvider implements IFragmentDataProvider{

	protected IFragmentDataProviderConfiguration dataProviderConfig;
	protected SimhashGenerator simhashGenerator;
	
	protected AbstractFragmentDataProvider() {
	}
	
	protected AbstractFragmentDataProvider(IFragmentDataProviderConfiguration dataProviderConfig/*, DetectionSettings ds*/) {
		ITokenBuilder tokenBuilder;
//		if(ds != null )
//			tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(ds);
//		else
			tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		this.dataProviderConfig = dataProviderConfig;
		IRegularHashGenerator regularHashgenerator = HashGeneratorFactory.LoadRegularHashgenerator();
		this.simhashGenerator = new SimhashGenerator(tokenBuilder, regularHashgenerator);
	}
	
	//protected abstract Object applyDataTransformation();
	
	public CloneFragment createNewCloneFragment(String file, String startline,
			String endline, String originalContent, int index /*, long simhash1, long simhash2*/) {
		return createNewCloneFragment(file, startline, endline, originalContent, originalContent, index);
	}
	
	public CloneFragment createNewCloneFragment(String file, String startline,
			String endline, String originalContent, String transformedContent, int index /*, long simhash1, long simhash2*/) {
		
		CloneFragment cloneFragment;
		
		if(CloneFragment.CLONE_GRANULARITY_FUNCTION.equals(dataProviderConfig.getSourceFragmentType()))
			cloneFragment = new FunctionCloneFragment(file, Integer.valueOf(startline), Integer.valueOf(endline), index, originalContent/*, transformedlContent, simhash1, simhash2*/);
		else
			cloneFragment = new BlockCloneFragment(file, Integer.valueOf(startline), Integer.valueOf(endline), index, originalContent/*, transformedlContent, simhash1, simhash2*/);
	
		simhashGenerator.updateSimhash(cloneFragment, transformedContent);
		return cloneFragment;
	}
}
