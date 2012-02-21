package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class FileSystemFragmentDataProvider extends AbstractFragmentDataProvider{
	
	XMLMultiSourceFragmentDataProvider xmlMultiSourceFragmentDataProvider;
	
	@SuppressWarnings("unused")
	private FileSystemFragmentDataProvider(){
	}
	
	public FileSystemFragmentDataProvider(FileSystemFragmentDataProviderConfiguration dataProviderConfig){
		super(dataProviderConfig);
	}
	
	private void initDataSource(){
		String dataSource[] = extractDataFromFilesystem();
		XMLMultiSourceFragmentDataProviderConfiguration xmlDataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(dataSource[0], dataSource[1],  this.dataProviderConfig.getSourceFragmentType());
		xmlDataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		xmlMultiSourceFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(xmlDataProviderConfig);
	}

	public List<CloneFragment> extractFragments(){
		initDataSource();
		return xmlMultiSourceFragmentDataProvider.extractFragments();
	}
	
	private String[] extractDataFromFilesystem() {
		String classpathRoot = Environment.getResourcePath("");
		String simcadInstallationUrl = PropsUtil.getSimcadInstallUrl();
		String output_dir=null;
		int exitVal = 0;
		
		FileSystemFragmentDataProviderConfiguration config = (FileSystemFragmentDataProviderConfiguration) dataProviderConfig;
		
		try {
		    output_dir = config.getOutputFolder();
		    if(!new File(output_dir).exists()){
		    	new File(output_dir).mkdir();
		    }
			// Execute a command with an argument that contains a space
		    String[] cmd = {
					"/bin/sh",
					"-c",
					"cd "+classpathRoot+
					"\ncd "+simcadInstallationUrl+
					"\n ./scripts/ExtractAndRename "+config.getSourceFragmentType()+" "+config.getLanguage()+" "+config.getTransformation()+" "+config.getSourceFolder()+" "+output_dir
					};

		    Process proc = Runtime.getRuntime().exec(cmd);
		    
		    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            // read the output from the command
		    String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            stdInput.close();
		    exitVal = proc.waitFor();
	        
		} catch (IOException e) {
			throw new SimcadException("Error occured during extraction...", e);
		}catch (InterruptedException e) {
			e.printStackTrace();
			throw new SimcadException("Error occured during extraction...", e);
		}	
		
		if(exitVal > 0){
			throw new SimcadException("Error occured during extraction...");
		}
		
		String sourceFragmentFileName = output_dir+System.getProperty("file.separator")+config.getSourceFragmentType()+"s.xml";
		String transformedFragmentFileName = output_dir+System.getProperty("file.separator")+config.getSourceFragmentType()+"s-"+config.getTransformation()+".xml";
		
		return new String[]{sourceFragmentFileName, transformedFragmentFileName};
	}
}
