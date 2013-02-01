package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProvider;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class XMLSourceFragmentDataProvider extends AbstractFragmentDataProvider{
	
	@SuppressWarnings("unused")
	private XMLSourceFragmentDataProvider(){
	}
	
	public XMLSourceFragmentDataProvider(XMLFragmentDataProviderConfiguration dataProviderConfig){
		super(dataProviderConfig);
	}
	
	public List<CloneFragment> extractFragments(){
		
		String dataSource = applyDataTransformation();
		
		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();
		Integer minSizeOfGranularity = PropsUtil.getMinSizeOfGranularity();
		
		File fileName = new File(dataSource);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//Document doc = db.parse(fileName);
		//doc.getDocumentElement().normalize();
        InputStream inputStream= new FileInputStream(fileName);
        Reader reader = new InputStreamReader(inputStream,"UTF-8");
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");

        Document doc = db.parse(is);
        doc.getDocumentElement().normalize();
		
		Element root = doc.getDocumentElement();
		  
//		  NodeList nl = root.getElementsByTagName("name");
//		  if(nl.getLength()>0){
//			  //projectName = nl.item(0).getFirstChild().getNodeValue();
//		  }else{
//			  error = true;
//		  }
//		  
//		  nl = root.getElementsByTagName("description");
//		  if(nl.getLength()>0){
//			  //projectDesc = nl.item(0).getChildNodes().item(0).getNodeValue();
//		  }else{
//			  error = true;
//		  }
		  
		NodeList sourceList = root.getElementsByTagName("source");
		  
		  if(sourceList.getLength()>0){
			//NodeList sourceList = nl.item(0).getChildNodes();
			
			Integer items = 0;
			
			for(int i = 0; i < sourceList.getLength(); i++){
				 Node source = sourceList.item(i);
				 if (source.getNodeType() != Node.ELEMENT_NODE) 
					 continue;
					
					String file = source.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
					String startline = source.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
					String endline = source.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
					String content = source.getFirstChild().getTextContent().trim();
					
					if(CloneFragment.computeActualLineOfCode(content) < minSizeOfGranularity) continue;
					CloneFragment cloneFragment = createNewCloneFragment(file, startline, endline, content, items++);
					cloneFragmentList.add(cloneFragment);
			}

			//System.out.println("Total items processed: "+items);
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cloneFragmentList;
	}

	//@Override
	protected String applyDataTransformation() {
		List<IXMLFragmentDataProviderTransformer> dataTransformerList = ((XMLFragmentDataProviderConfiguration)dataProviderConfig).getDataTransformer();
		String dataSource = ((XMLFragmentDataProviderConfiguration)dataProviderConfig).getXmlFileName();
		for (IXMLFragmentDataProviderTransformer xmlFragmentDataProviderTransformer : dataTransformerList) {
			dataSource = xmlFragmentDataProviderTransformer.transform(dataSource);
		}
		return dataSource;
	}
	
}
