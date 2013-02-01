package ca.usask.cs.srlab.simcad.index.io;

import ca.usask.cs.srlab.simcad.DetectionSettings;

public class XMLIndexIOConfiguration implements IndexIOConfiguration {

	private String indexFileContainerFolder; 
	private String projectName; 
	private DetectionSettings detectionSettings;
	
	static final String ATTRIBUTE_SEPERATOR = "@!#";
	
	static final String SIMCAD_EXPORT_FILE_TYPE_CLONE_INDEX = "simcad_clone_index";
	
	public XMLIndexIOConfiguration(String indexFileContainerFolder, String projectName,
			DetectionSettings detectionSettings) {
		super();
		this.indexFileContainerFolder = indexFileContainerFolder;
		this.projectName = projectName;
		this.detectionSettings = detectionSettings;
	}

	@Override
	public String getIOLocation() {
		return indexFileContainerFolder + System.getProperty("file.separator")
				+ SIMCAD_EXPORT_FILE_TYPE_CLONE_INDEX + ATTRIBUTE_SEPERATOR
				+ projectName.replaceAll(" ", "_") + ATTRIBUTE_SEPERATOR
				+ detectionSettings.getLanguage() + ATTRIBUTE_SEPERATOR
				+ detectionSettings.getCloneGranularity() + ATTRIBUTE_SEPERATOR
				+ detectionSettings.getSourceTransformation() + ".xml";
	}

}
