package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

public class DatabaseOutputProcessor extends AbstractProcessor {

	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		super.process(inputCloneSets, outputCloneSets);
		
		outputCloneSets.addAll(inputCloneSets);
		return false;
	}


}
