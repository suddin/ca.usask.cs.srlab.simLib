package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.util.ArrayList;
import java.util.List;

public class XMLMultiSourceFragmentDataProviderConfiguration extends AbstractFragmentDataProviderConfiguration{
	
	private String originalSourceXmlFileName;
	private String transformedSourceXmlFileName;
	private List<IXMLFragmentDataProviderTransformer> dataTransformer = new ArrayList<IXMLFragmentDataProviderTransformer>();

	public List<IXMLFragmentDataProviderTransformer> getDataTransformer() {
		return dataTransformer;
	}

	public void addDataTransformer(IXMLFragmentDataProviderTransformer dataTransformer){
		this.dataTransformer.add(dataTransformer);
	}
	
	public void removeTransformer(IXMLFragmentDataProviderTransformer dataTransformer){
		this.dataTransformer.remove(dataTransformer);
	}

	public XMLMultiSourceFragmentDataProviderConfiguration(String originalSourceXmlFileName, String transformedSourceXmlFileName, String cloneFragmentType) {
		super(cloneFragmentType);
		this.originalSourceXmlFileName = originalSourceXmlFileName;
		this.transformedSourceXmlFileName = transformedSourceXmlFileName;
	}

	public String getOriginalSourceXmlFileName() {
		return originalSourceXmlFileName;
	}

	public String getTransformedSourceXmlFileName() {
		return transformedSourceXmlFileName;
	}
	
}
