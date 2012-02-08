package ca.usask.cs.srlab.simcad.processor;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.ICloneSet;

public interface IProcessor {

	public boolean process(Collection<ICloneSet> inputCloneSets, Collection<ICloneSet> outputCloneSets);

	public String getNmae();

}
