package ca.usask.cs.srlab.simcad.dataprovider;

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

import ca.usask.cs.srlab.simcad.engine.TypeMapper;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneGroup;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.model.ICloneType;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class XMLDataProvider extends AbstractDataProvider{
	
	private static final XMLDataProvider INSTANCE = new XMLDataProvider();
	private String xmlFileName;
	
	private XMLDataProvider(){
		super();
	}
	
	public void configure(String xmlFileName, DetectionSettings ds){
		configure(ds);
		this.xmlFileName = xmlFileName;
	}
	
	public List<CloneFragment> extractFragments(){
		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();

		File fileName = new File(xmlFileName);
		
		boolean error = false;
		
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
		  
		  NodeList nl = root.getElementsByTagName("name");
		  if(nl.getLength()>0){
			  //projectName = nl.item(0).getFirstChild().getNodeValue();
		  }else{
			  error = true;
		  }
		  
		  nl = root.getElementsByTagName("description");
		  if(nl.getLength()>0){
			  //projectDesc = nl.item(0).getChildNodes().item(0).getNodeValue();
		  }else{
			  error = true;
		  }
		  
		  nl = root.getElementsByTagName("source_elements");
		  
		  if(nl.getLength()>0){
			NodeList sourceList = nl.item(0).getChildNodes();
			
			long start = System.currentTimeMillis();
			long items =0;
			
			
			for(int i =0; i < sourceList.getLength(); i++){
				 Node source = sourceList.item(i);
				 if (source.getNodeType() != Node.ELEMENT_NODE) 
					 continue;
					
					String file = source.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
					String startline = source.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
					String endline = source.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
					String content = source.getFirstChild().getTextContent();
					
					int loc = computeLoc(content);
					
					if(loc < PropsUtil.getMinSizeOfGranularity())
						continue;
					
					long simhash[] = simhashGenerator.generateSimhash(content/*, simThreshold > 0*/);
					
					CloneFragment cloneFragment = createNewCloneFragment(file, startline, endline, content, content, i, simhash[0], simhash[1]);
					cloneFragmentList.add(cloneFragment);
					
					items++;
			}

			System.out.println("Total items processed: "+items);
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cloneFragmentList;
	}

	private CloneFragment createNewCloneFragment(String file, String startline,
			String endline, String originalContent, String transformedlContent, int index, long simhash1, long simhash2) {
		//TODO: take decision on function or block
		return new FunctionCloneFragment(file, Integer.valueOf(startline), Integer.valueOf(endline), index, originalContent, transformedlContent, simhash1, simhash2);
	}
	
	private int computeLoc(String content) {
		String []line = content.split("\n");
		int loc=0;
		for(String ln : line){
			if(ln.length() > 0)
				loc++;
		}
		
		return loc;
	}
	
}
