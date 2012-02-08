package ca.usask.cs.srlab.simcad.listener;

import java.util.EventListener;

import ca.usask.cs.srlab.simcad.event.CloneFoundEvent;
import ca.usask.cs.srlab.simcad.event.DetectionEndEvent;
import ca.usask.cs.srlab.simcad.event.DetectionProgressEvent;
import ca.usask.cs.srlab.simcad.event.DetectionStartEvent;

public interface ICloneDetectionListener extends EventListener {

	public void foundClone(CloneFoundEvent event);

	public void progressDetection(DetectionProgressEvent event);

	public void startDetection(DetectionStartEvent event);

	public void endDetection(DetectionEndEvent event);
}
