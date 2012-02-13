package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.util.ArrayList;
import java.util.List;

public class XMLFragmentDataProviderConfiguration extends AbstractFragmentDataProviderConfiguration{
	private String xmlFileName;
	private List<IXMLFragmentDataProviderTransformer> dataTransformer = new ArrayList<IXMLFragmentDataProviderTransformer>();

	public String getXmlFileName() {
		return xmlFileName;
	}

	public List<IXMLFragmentDataProviderTransformer> getDataTransformer() {
		return dataTransformer;
	}

	public void addDataTransformer(IXMLFragmentDataProviderTransformer dataTransformer){
		this.dataTransformer.add(dataTransformer);
	}
	
	public void removeTransformer(IXMLFragmentDataProviderTransformer dataTransformer){
		this.dataTransformer.remove(dataTransformer);
	}

	public XMLFragmentDataProviderConfiguration(String xmlFileName, String cloneFragmentType) {
		super(cloneFragmentType);
		this.xmlFileName = xmlFileName;
	}
	
}