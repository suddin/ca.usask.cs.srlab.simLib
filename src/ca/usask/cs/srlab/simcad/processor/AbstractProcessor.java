package ca.usask.cs.srlab.simcad.processor;


public abstract class AbstractProcessor implements IProcessor{

	public AbstractProcessor() {
		super();
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}