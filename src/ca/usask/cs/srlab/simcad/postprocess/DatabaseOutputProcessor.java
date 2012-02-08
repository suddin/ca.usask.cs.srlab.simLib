package ca.usask.cs.srlab.simcad.postprocess;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.ICloneSet;
import ca.usask.cs.srlab.simcad.processor.IProcessor;

public class DatabaseOutputProcessor implements IProcessor {

	@Override
	public boolean process(Collection<ICloneSet> inputCloneSets,
			Collection<ICloneSet> outputCloneSets) {
		
		
		outputCloneSets.addAll(inputCloneSets);
		return false;
	}

	@Override
	public String getNmae() {
		return this.getClass().getSimpleName();
	}

}
