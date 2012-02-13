package ca.usask.cs.srlab.simcad.postprocess;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.IProcessor;

public class DatabaseOutputProcessor implements IProcessor {

	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		
		
		outputCloneSets.addAll(inputCloneSets);
		return false;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
