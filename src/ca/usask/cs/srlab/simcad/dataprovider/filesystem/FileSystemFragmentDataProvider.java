package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import java.io.File;
import java.io.IOException;
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
		XMLMultiSourceFragmentDataProviderConfiguration xmlDataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(dataSource[0], dataSource[1], this.dataProviderConfig.getSourceDaraRootUrl(), this.dataProviderConfig.getSourceFragmentType());
		xmlDataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		xmlMultiSourceFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(xmlDataProviderConfig);
	}

	public List<CloneFragment> extractFragments(){
		initDataSource();
		return xmlMultiSourceFragmentDataProvider.extractFragments();
	}
	
	private String[] extractDataFromFilesystem() {
		
		String simLibRoot = Environment.getSimLibRoot();
		String simcadInstallationUrl = PropsUtil.getSimcadInstallUrl();
		
//		System.err.println("simlib container root: "+simLibRoot);
//		System.err.println("simcad installation: "+simcadInstallationUrl);
		
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
					"cd "+simLibRoot+
					"\ncd "+simcadInstallationUrl+
					"\n ./scripts/ExtractAndRename "+config.getSourceFragmentType()+" "+config.getLanguage()+" "+config.getTransformationAction()+" "+config.getSourceFolder()+" "+output_dir
					};

		    exitVal = SimCadCommandRunner.INSTANCE.executeCommand(cmd, true);
	        
		} catch (IOException e) {
			throw new SimcadException("Error occured during extraction...see log in :" +config.getOutputFolder(), e);
		}catch (InterruptedException e) {
			e.printStackTrace();
			throw new SimcadException("Error occured during extraction...see log in :" +config.getOutputFolder(), e);
		}	
		
		if(exitVal > 0){
			throw new SimcadException("Error occured during extraction...see log in :" +config.getOutputFolder());
		}
		
		String sourceFragmentFileName = output_dir+System.getProperty("file.separator")+config.getSourceFragmentType()+"s.xml";
		String transformedFragmentFileName = output_dir+System.getProperty("file.separator")+config.getSourceFragmentType()+"s-"+config.getTransformationAction()+".xml";
		
		return new String[]{sourceFragmentFileName, transformedFragmentFileName};
	}
}
