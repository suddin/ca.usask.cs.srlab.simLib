package ca.usask.cs.srlab.simcad.event;

import java.util.EventObject;

public class DetectionStartEvent extends EventObject {

	private static final long serialVersionUID = -1456518832133968826L;

	private int inputSize;
	
	public DetectionStartEvent(Object source, int inputSize) {
		super(source);
		this.inputSize = inputSize;
	}

	public int getInputSize() {
		return inputSize;
	}
}
