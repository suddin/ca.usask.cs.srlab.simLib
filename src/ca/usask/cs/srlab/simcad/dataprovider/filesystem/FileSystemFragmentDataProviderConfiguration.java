package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProviderConfiguration;

public class FileSystemFragmentDataProviderConfiguration extends AbstractFragmentDataProviderConfiguration{
	private String sourceFolder;
	private String outputFolder;
	private String language;
	private String transformation;

	public FileSystemFragmentDataProviderConfiguration(String sourceFolder, String outputFolder, String language, String transformation, String sourceFragmentType) {
		super(sourceFragmentType);
		this.sourceFolder = sourceFolder;
		this.outputFolder = outputFolder;
		this.language = language;
		this.transformation = transformation;
	}

	public String getSourceFolder() {
		return sourceFolder;
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
}
