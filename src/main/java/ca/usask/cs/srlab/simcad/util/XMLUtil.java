package ca.usask.cs.srlab.simcad.util;

import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import ca.usask.cs.srlab.simcad.SimcadException;

public class XMLUtil {

	public static void writeXmlFile(Document doc, String filename) {
	    try {
	        // Prepare the DOM document for writing
	    	DOMSource source = new DOMSource(doc);

	        // Prepare the output file
	        File file = new File(filename);
	        StreamResult result = new StreamResult(file);

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        //apply pretty printing
	        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        xformer.transform(source, result);
	    } catch (TransformerConfigurationException e) {
			throw new SimcadException("Can not create output file", e);
	    } catch (TransformerException e) {
			throw new SimcadException("Can not create output file", e);
	    }
	}
	
}
