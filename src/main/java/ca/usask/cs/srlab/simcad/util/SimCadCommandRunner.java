package ca.usask.cs.srlab.simcad.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.IScriptExecutionListener;

public final class  SimCadCommandRunner {

	public static SimCadCommandRunner INSTANCE = new SimCadCommandRunner();
	
	private IScriptExecutionListener scriptExecutionListener = new IScriptExecutionListener(){
		public void executionStarted() {}
		public void nextLineOfExecutionOutput(String line) {}
		public void executionEnded(int exitValue) {}
		};
		
	private SimCadCommandRunner(){}
	
	public int executeCommand(String cmd, boolean printOutput) throws IOException,
	InterruptedException {
		return executeCommand(new String[]{cmd},  printOutput);
	} 
	
	public int executeCommand(String[] cmd, boolean printOutput) throws IOException,
			InterruptedException {
		return executeCommand(cmd, printOutput, null);
	}
	
	public int executeCommand(String cmd, boolean printOutput, String context) throws IOException,
	InterruptedException {
		return executeCommand(new String[]{cmd},  printOutput, context);
	} 
	
	public int executeCommand(String[] cmd, boolean printOutput, String context) throws IOException,
	InterruptedException {

		scriptExecutionListener.executionStarted();

		Process proc = null;
		if(context == null)
			proc = Runtime.getRuntime().exec(cmd);
		else
			proc = Runtime.getRuntime().exec(cmd, new String[]{}, new File(context));
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			scriptExecutionListener.nextLineOfExecutionOutput(s);
			if (printOutput)
				System.out.println(s);
		}
		
		if (stdInput != null)
			stdInput.close();
		
		s = null;
		while ((s = stdError.readLine()) != null) {
			scriptExecutionListener.nextLineOfExecutionOutput(s);
			if (printOutput)
				System.out.println(s);
		}
		
		if (stdError != null)
			stdError.close();
		
		
		
		int exitVal = proc.waitFor();
		
		scriptExecutionListener.executionEnded(exitVal);
		
		return exitVal;
	}
	
	
	public int executeExtractAndRename(File source, File dest, DetectionSettings detectionSettings){
		int exitVal = 0;
		
		
		return exitVal;
	}
	
	
}
