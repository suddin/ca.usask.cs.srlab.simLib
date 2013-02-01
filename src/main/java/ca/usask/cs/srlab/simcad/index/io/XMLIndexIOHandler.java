package ca.usask.cs.srlab.simcad.index.io;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.memory.IndexKey;
import ca.usask.cs.srlab.simcad.index.memory.MemoryCloneIndexByJavaCollection;
import ca.usask.cs.srlab.simcad.model.BlockCloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;

public class XMLIndexIOHandler implements IndexIOHandler {
	/*
	 XML Schema
	 
	<CloneIndex project="" language="" fragmentType ="" transformationType="">	 
		<LineIndex lineNumber="10">
			<BitIndex bitNumber="1">
				<CloneFragment fileName="" fromLine="" toLine="" pcid="" simhash1="" simhash2="">
					//source
				<CloneFragment>
			</BitIndex>
		</LineIndex>
	<CloneIndex>	
	
	*/
	
	private XMLIndexIOConfiguration config;
	
	public XMLIndexIOHandler(XMLIndexIOConfiguration config) {
		super();
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see ca.usask.cs.srlab.simcad.index.io.IndexIOHandler#doExport(ca.usask.cs.srlab.simcad.index.ICloneIndex)
	 */
	@Override
	public void doExport(ICloneIndex cloneIndex){
		
		PrintWriter indexOutputWriter;
		
		String xmlFileName = config.getIOLocation();
		
		try {
			indexOutputWriter = new PrintWriter(new FileWriter(xmlFileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SimcadException("Can not create index output file", e);
		}
		
		indexOutputWriter.println("<CloneIndex project=\""+cloneIndex.getSourceProject()+"\" language=\""+cloneIndex.getSourceLanguage()+"\" fragmentType =\""+cloneIndex.getFragmentType()+"\" transformationType=\""+cloneIndex.getTransformationType()+"\">");
		
		if(cloneIndex instanceof MemoryCloneIndexByJavaCollection){
			
			MemoryCloneIndexByJavaCollection mjCloneIndex = (MemoryCloneIndexByJavaCollection)cloneIndex;
			
			Object[] allKeys = mjCloneIndex.getAllKeys().toArray();
			
			//if(allKeys.length > 0 )
			
			IndexKey previousKey = new IndexKey(0,0);
			
			for(int i = 0; i < allKeys.length; i++){
				IndexKey indexKey = (IndexKey) allKeys[i];
				
				if(previousKey.getLineKey().intValue() != indexKey.getLineKey().intValue()){
					indexOutputWriter.println("<LineIndex lineNumber=\""+indexKey.getLineKey().intValue()+"\">");
				}
				
				indexOutputWriter.println("<BitIndex bitNumber=\""+indexKey.getBitKey().intValue()+"\">");
				for(ICloneFragment cloneFragment : mjCloneIndex.getEntriesByIndex(indexKey)){
					indexOutputWriter.println("<CloneFragment fileName=\""+cloneFragment.getFileName()+"\" fromLine=\""+cloneFragment.getFromLine().toString()+"\" toLine=\""+cloneFragment.getToLine().toString()+"\" pcid=\""+cloneFragment.getProgramComponentId().toString()+"\" simhash1=\""+cloneFragment.getSimhash1()+"\" simhash2=\""+cloneFragment.getSimhash2()+"\"><![CDATA[");
					indexOutputWriter.println(cloneFragment.getOriginalCodeBlock());
					indexOutputWriter.println("]]></CloneFragment>");
				}
				indexOutputWriter.println("</BitIndex>");	
				
				if(i+1 == allKeys.length || ((IndexKey)allKeys[i+1]).getLineKey().intValue() != indexKey.getLineKey().intValue()){
					indexOutputWriter.println("</LineIndex>");
				}
				
				previousKey = indexKey;
			}
			
		}
		
		indexOutputWriter.print("</CloneIndex>");
		
		indexOutputWriter.close();
		
	}
	
	/* (non-Javadoc)
	 * @see ca.usask.cs.srlab.simcad.index.io.IndexIOHandler#doImport(ca.usask.cs.srlab.simcad.index.ICloneIndex)
	 */
	//@Override
	public void doImportX(ICloneIndex cloneIndex){

		String dataSource = config.getIOLocation();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			
	        InputStream inputStream= new FileInputStream(dataSource);
	        Reader reader = new InputStreamReader(inputStream,"UTF-8");
	        InputSource is = new InputSource(reader);
	        is.setEncoding("UTF-8");
	
	        Document doc = db.parse(is);
	        doc.getDocumentElement().normalize();
			
			Element cloneIndexRootNode = doc.getDocumentElement();
			
			if (cloneIndexRootNode != null){

				String project = getNodeAttributeValue(cloneIndexRootNode, "project");
				String language = getNodeAttributeValue(cloneIndexRootNode, "language");
				String fragmentType = getNodeAttributeValue(cloneIndexRootNode, "fragmentType");
				String transformationType = getNodeAttributeValue(cloneIndexRootNode, "transformationType");
				
				//cloneIndex.
				
				NodeList lineIndexNodes = cloneIndexRootNode.getChildNodes();
				
				for(int i = 0; i < lineIndexNodes.getLength();  i++){
					Node lineIndexNode = lineIndexNodes.item(i);
					if (lineIndexNode.getNodeType() != Node.ELEMENT_NODE) 
						 continue;
					
					String lineNumber = getNodeAttributeValue(lineIndexNode, "lineNumber");
					
					
					NodeList bitIndexNodes = lineIndexNode.getChildNodes();
					
					for(int j = 0; j < bitIndexNodes.getLength();  j++){
						Node bitIndexNode = bitIndexNodes.item(j);
						if (bitIndexNode.getNodeType() != Node.ELEMENT_NODE) 
							 continue;
						String bitNumber = getNodeAttributeValue(bitIndexNode, "bitNumber");
						
						NodeList fragmentNodes = bitIndexNode.getChildNodes();
						
						for(int k = 0; k < fragmentNodes.getLength();  k++){
							Node fragmentNode = fragmentNodes.item(k);
							
							if (fragmentNode.getNodeType() != Node.ELEMENT_NODE) 
								 continue;
							
							String fileName = getNodeAttributeValue(fragmentNode, "fileName");
							String fromLine = getNodeAttributeValue(fragmentNode, "fromLine");
							String toLine = getNodeAttributeValue(fragmentNode, "toLine");
							String simhash1 = getNodeAttributeValue(fragmentNode, "simhash1");
							String simhash2 = getNodeAttributeValue(fragmentNode, "simhash2");
							String pcid = getNodeAttributeValue(fragmentNode, "pcid");
							String sourceCode = getNodeContentValue(fragmentNode, true); 
							
							CloneFragment cloneFragemnt = null;
							
							if(fragmentType.equals(CloneFragment.CLONE_GRANULARITY_FUNCTION))
								cloneFragemnt = new FunctionCloneFragment(fileName, Integer.valueOf(fromLine), Integer.valueOf(toLine), Integer.valueOf(pcid), sourceCode);
							else
								cloneFragemnt = new BlockCloneFragment(fileName, Integer.valueOf(fromLine), Integer.valueOf(toLine), Integer.valueOf(pcid), sourceCode);
							
							cloneFragemnt.setSimhash1(Long.valueOf(simhash1));
							cloneFragemnt.setSimhash2(Long.valueOf(simhash2));
							cloneIndex.insert(cloneFragemnt);
							
						}
					
					}
					
				}
				
			}
				
		}catch (Exception e) {
			throw new SimcadException(e);
		}
		
	}
	
	
	@Override
	public void doImport(ICloneIndex cloneIndex){

		String dataSource = config.getIOLocation();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			
	        InputStream inputStream= new FileInputStream(dataSource);
	        Reader reader = new InputStreamReader(inputStream,"UTF-8");
	        InputSource is = new InputSource(reader);
	        is.setEncoding("UTF-8");
	
	        Document doc = db.parse(is);
	        doc.getDocumentElement().normalize();
			
			Element cloneIndexRootNode = doc.getDocumentElement();
			
			if (cloneIndexRootNode != null){

				String project = getNodeAttributeValue(cloneIndexRootNode, "project");
				String language = getNodeAttributeValue(cloneIndexRootNode, "language");
				String fragmentType = getNodeAttributeValue(cloneIndexRootNode, "fragmentType");
				String transformationType = getNodeAttributeValue(cloneIndexRootNode, "transformationType");
				
				NodeList fragmentNodes = cloneIndexRootNode.getElementsByTagName("CloneFragment");
				
				for(int k = 0; k < fragmentNodes.getLength();  k++){
					Node fragmentNode = fragmentNodes.item(k);
					
					if (fragmentNode.getNodeType() != Node.ELEMENT_NODE) 
						 continue;
					
					String fileName = getNodeAttributeValue(fragmentNode, "fileName");
					String fromLine = getNodeAttributeValue(fragmentNode, "fromLine");
					String toLine = getNodeAttributeValue(fragmentNode, "toLine");
					String simhash1 = getNodeAttributeValue(fragmentNode, "simhash1");
					String simhash2 = getNodeAttributeValue(fragmentNode, "simhash2");
					String pcid = getNodeAttributeValue(fragmentNode, "pcid");
					String sourceCode = getNodeContentValue(fragmentNode, true); 
					
					CloneFragment cloneFragemnt = null;
					
					if(fragmentType.equals(CloneFragment.CLONE_GRANULARITY_FUNCTION))
						cloneFragemnt = new FunctionCloneFragment(fileName, Integer.valueOf(fromLine), Integer.valueOf(toLine), Integer.valueOf(pcid), sourceCode);
					else
						cloneFragemnt = new BlockCloneFragment(fileName, Integer.valueOf(fromLine), Integer.valueOf(toLine), Integer.valueOf(pcid), sourceCode);
					
					cloneFragemnt.setSimhash1(Long.valueOf(simhash1));
					cloneFragemnt.setSimhash2(Long.valueOf(simhash2));
					cloneIndex.insert(cloneFragemnt);
					
				}
				
			}
				
		}catch (Exception e) {
			throw new SimcadException(e);
		}
		
	}
	
	private String getNodeAttributeValue(Node node, String attributeName) {
		if (node != null && node.getAttributes() != null) {
			Node namedItem = node.getAttributes().getNamedItem(attributeName);
			return getNodeContentValue(namedItem);
		}
		return null;
	}
	
	private String getNodeContentValue(Node node) {
		return getNodeContentValue(node, false);
	}
	
	private String getNodeContentValue(Node node, boolean isCdata) {
		if (node != null && node.getFirstChild() != null) {
			if (isCdata)
				return node.getFirstChild().getTextContent().trim();
			else	
				return node.getFirstChild().getNodeValue();
		}
		return null;
	}
	
}
