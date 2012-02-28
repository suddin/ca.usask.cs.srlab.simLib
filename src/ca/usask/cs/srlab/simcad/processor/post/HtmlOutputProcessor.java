package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

public class HtmlOutputProcessor  extends AbstractProcessor {

	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		
		
		outputCloneSets.addAll(inputCloneSets);
		return false;
	}

}