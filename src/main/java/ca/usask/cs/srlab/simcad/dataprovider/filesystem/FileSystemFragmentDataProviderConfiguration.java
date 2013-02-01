package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import ca.usask.cs.srlab.simcad.SimCadConstants;
import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProviderConfiguration;

public class FileSystemFragmentDataProviderConfiguration extends AbstractFragmentDataProviderConfiguration{
	//private String sourceFolder;
	private String outputFolder;
	private String language;
	private String transformation;

	public FileSystemFragmentDataProviderConfiguration(String sourceFolder, String outputFolder, String language, String transformation, String sourceFragmentType) {
		this(sourceFolder, outputFolder, language, transformation, sourceFragmentType, false);
	}		
	
	public FileSystemFragmentDataProviderConfiguration(String sourceFolder, String outputFolder, String language, String transformation, String sourceFragmentType, boolean forceExtract) {
		super(sourceFolder.endsWith(SimCadConstants.FILE_SEPARATOR) ? sourceFolder.substring(0, sourceFolder.length()-1):sourceFolder, sourceFragmentType, forceExtract);
		//this.sourceFolder = sourceFolder.endsWith(Constants.FILE_SEPARATOR) ? sourceFolder.substring(0, sourceFolder.length()-1):sourceFolder;
		this.outputFolder = outputFolder.endsWith(SimCadConstants.FILE_SEPARATOR) ? outputFolder.substring(0, outputFolder.length()-1):outputFolder;
		this.language = language;
		this.transformation = transformation;
	}

	public String getSourceFolder() {
		return getSourceDataRootUrl();
	}
	
	public String getOutputFolder() {
		return outputFolder;
	}

	public String getLanguage() {
		return language;
	}

	public String getTransformation() {
		return transformation;
	}

	public String getTransformationAction() {
		if(transformation.equals(SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS))
			return SimCadConstants.SOURCE_TRANSFORMATION_ACTION_CONSISTENT;
		else
			return SimCadConstants.SOURCE_TRANSFORMATION_ACTION_BLIND;
	}
}
