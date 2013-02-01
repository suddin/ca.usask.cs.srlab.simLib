package ca.usask.cs.srlab.simcad.event;

import java.util.EventObject;

public class DetectionProgressEvent extends EventObject {

	private static final long serialVersionUID = 700224477704216602L;

	private Integer currentPosition;

	public DetectionProgressEvent(Object source, Integer currentPosition) {
		super(source);
		this.currentPosition = currentPosition;
	}

	public Integer getCurrentPosition() {
		return currentPosition;
	}
}
