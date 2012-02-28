package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

		scriptExecutionListener.executionStarted();

		Process proc = Runtime.getRuntime().exec(cmd);
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String s = null;
		while ((s = stdInput.readLine()) != null) {
			scriptExecutionListener.nextLineOfExecutionOutput(s);
			if (printOutput)
				System.out.println(s);
		}

		if (stdInput != null)
			stdInput.close();

		int exitVal = proc.waitFor();

		scriptExecutionListener.executionEnded(exitVal);

		return exitVal;
	}
	
	public int executeCommand(String cmd, boolean printOutput, String context) throws IOException,
	InterruptedException {
		return executeCommand(new String[]{cmd},  printOutput, context);
	} 
	
	public int executeCommand(String[] cmd, boolean printOutput, String context) throws IOException,
	InterruptedException {

		scriptExecutionListener.executionStarted();
		
		Process proc = Runtime.getRuntime().exec(cmd, new String[]{}, new File(context));
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			scriptExecutionListener.nextLineOfExecutionOutput(s);
			if (printOutput)
				System.out.println(s);
		}
		
		if (stdInput != null)
			stdInput.close();
		
		int exitVal = proc.waitFor();
		
		scriptExecutionListener.executionEnded(exitVal);
		
		return exitVal;
	}
	
}
