package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

public interface IScriptExecutionListener {

	public void executionStarted();
	
	public void nextLineOfExecutionOutput(String line);
	
	public void executionEnded(int exitValue); //return exit value
	
}
