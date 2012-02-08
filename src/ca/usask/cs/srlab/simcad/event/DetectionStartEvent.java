package ca.usask.cs.srlab.simcad.event;

import java.util.EventObject;

public class DetectionStartEvent extends EventObject {

	private static final long serialVersionUID = -1456518832133968826L;

	public DetectionStartEvent(Object source) {
		super(source);
	}
}
