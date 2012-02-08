package ca.usask.cs.srlab.simcad.event;

import java.util.EventObject;

import ca.usask.cs.srlab.simcad.model.ICloneSet;

public class CloneFoundEvent extends EventObject {

	private static final long serialVersionUID = 6314347002276126276L;

	private ICloneSet foundCloneSet;
	
	public CloneFoundEvent(Object source, ICloneSet foundCloneSet) {
		super(source);
		this.foundCloneSet = foundCloneSet;
	}

	public ICloneSet getFoundCloneSet() {
		return foundCloneSet;
	}
}
