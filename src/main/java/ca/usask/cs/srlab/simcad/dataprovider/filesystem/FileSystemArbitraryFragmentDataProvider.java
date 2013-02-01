package ca.usask.cs.srlab.simcad.dataprovider.filesystem;

import ca.usask.cs.srlab.simcad.dataprovider.xml.ArbitraryFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProviderConfiguration;

public class FileSystemArbitraryFragmentDataProvider extends FileSystemFragmentDataProvider{
	
	Integer selectedTextFromLine; 
	Integer selectedTextToLine;
	
	public FileSystemArbitraryFragmentDataProvider(FileSystemFragmentDataProviderConfiguration dataProviderConfig, Integer selectedTextFromLine, 
	Integer selectedTextToLine){
		super(dataProviderConfig);
		this.selectedTextFromLine = selectedTextFromLine; 
		this.selectedTextToLine = selectedTextToLine;
	}
	
	@Override
	protected void initDataSource(){
		String dataSource[] = super.extractDataFromFilesystem();
		XMLMultiSourceFragmentDataProviderConfiguration xmlDataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(dataSource[0], dataSource[1], this.dataProviderConfig.getSourceDataRootUrl(), this.dataProviderConfig.getSourceFragmentType());
		xmlDataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		xmlMultiSourceFragmentDataProvider = new ArbitraryFragmentDataProvider(xmlDataProviderConfig, selectedTextFromLine, selectedTextToLine);
	}
	
}
