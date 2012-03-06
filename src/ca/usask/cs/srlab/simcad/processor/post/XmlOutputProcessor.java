package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.model.CloneGroup;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;
import ca.usask.cs.srlab.simcad.util.XMLUtil;

public class XmlOutputProcessor extends AbstractProcessor {
	private String outputFolderName;
	private DetectionSettings detectionSettings;
	
	@SuppressWarnings("unused")
	private XmlOutputProcessor(){}
	
	public XmlOutputProcessor(DetectionSettings detectionSettings, String outputFolderName) {
		super();
		this.detectionSettings = detectionSettings;
		this.outputFolderName = outputFolderName;
	}

	@Override
	public boolean process(Collection<CloneSet> inputCloneSets, Collection<CloneSet> outputCloneSets) {
		super.process(inputCloneSets, outputCloneSets);	
		
		//if(detectionSettings.isVerbose())
		System.out.println("Generating output xml file...\n");
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = null;
		try {
			docBuilder = dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
		Document doc = docBuilder.newDocument();
	    //with source
		writeXmlDocument(inputCloneSets, doc, true);
	    
	    doc = docBuilder.newDocument();
	    //without source
	    writeXmlDocument(inputCloneSets, doc, false);
	   
		outputCloneSets.addAll(inputCloneSets);
		return true;
	}

	public void writeXmlDocument(Collection<CloneSet> inputCloneSets, Document doc, boolean withSource) {
		Element root = doc.createElement("clones");
	    doc.appendChild(root);

		for(CloneSet cs: inputCloneSets){
			Node child = doc.createElement((cs instanceof CloneGroup) ? "clone_group" : "clone_pair");
			((Element)child).setAttribute("groupid", cs.getCloneSetId().toString());
			((Element)child).setAttribute("nfragments", String.valueOf(cs.size()));
			((Element)child).setAttribute("type", cs.getCloneType());
			
			for(ICloneFragment cloneFragment : cs.getCloneFragments()){
				Node child2 = doc.createElement("clone_fragment");
				((Element)child2).setAttribute("file", cloneFragment.getFileName());
				((Element)child2).setAttribute("startline", cloneFragment.getFromLine().toString());
				((Element)child2).setAttribute("endline", cloneFragment.getToLine().toString());
				((Element)child2).setAttribute("pcid", cloneFragment.getProgramComponentId().toString());
				
				if(withSource)
					child2.appendChild(doc.createCDATASection("\n"+cloneFragment.getOriginalCodeBlock()+"\n"));
				
				child.appendChild(child2);
			}
	        root.appendChild(child);
		}
		
	    root.setAttribute("ngroups", Integer.toString(detectionSettings.getDetectionReport().getnCloneSet()));
	    root.setAttribute("nfragments", Integer.toString(detectionSettings.getDetectionReport().getnCloneFragment()));
	    root.setAttribute("cloneTypes",  detectionSettings.getCloneSetType());
	    
	    //System.out.println("Total clone class : "+ nGroup );
	    //System.out.println("Total clone frag : "+ nFragment );
	    
	    String outputFileName =  "simcad_"+detectionSettings.getCloneGranularity()+"_clone-"+detectionSettings.getCloneSetType()+"s_"+detectionSettings.getTypeString()+"_"+detectionSettings.getSourceTransformation()+(withSource?"_withsource":"")+".xml";
	    String outputFullFileName = outputFolderName + (outputFolderName.endsWith(System.getProperty("file.separator")) ?"":System.getProperty("file.separator"))+ outputFileName;
	   
//	    File outputFile = new File(outputFileName);
		try {
			XMLUtil.writeXmlFile(doc, outputFullFileName);
			//if(detectionSettings.isVerbose())
				//System.out.println(outputFullFileName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SimcadException("Can not create output file", e);
		}

	}

}
