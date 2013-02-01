package ca.usask.cs.srlab.simcad.processor;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;

public interface IProcessor {

	public boolean process(Collection<CloneSet> inputCloneSets, Collection<CloneSet> outputCloneSets);

	public String getName();

}
