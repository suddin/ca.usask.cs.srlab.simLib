package ca.usask.cs.srlab.simcad.installation;

import java.io.IOException;

import org.junit.Test;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.SimCadCommandRunner;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class ValidateInstallation {

	
	@Test
	public void testInstallation(){
			
			String simcadInstallationUrl = PropsUtil.getSimcadInstallUrl();
			String simLibRoot = Environment.getResourcePath("");//Environment.getResourcePath("");//Environment.getSimLibRoot();
			String[] cmd = {
					"/bin/sh",
					"-c",
					"ls -l "+
					"\ncd "+"/usr/local"+
					"\npwd"
					};
			
			try {
				int exitVal = SimCadCommandRunner.INSTANCE.executeCommand(cmd, true);
				
				if(exitVal == 127){
					throw new SimcadException("Invalid simcad location on configuration file, location must be absolute ot relative to simcad2.jar");
				}
				if(exitVal > 0){
					throw new SimcadException("Simcad not insalled correctly");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new SimcadException(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new SimcadException(e);
			}
	}
	
}
