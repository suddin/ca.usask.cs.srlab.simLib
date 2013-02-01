package ca.usask.cs.srlab.simcad.processor;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;


public abstract class AbstractProcessor implements IProcessor{

	protected String message;

	public AbstractProcessor() {
		super();
	}

	public AbstractProcessor(String message) {
		super();
		this.message = message;
	}

	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		
		if(message!=null)
			System.out.println(message+"\n");
			
		return true;
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}