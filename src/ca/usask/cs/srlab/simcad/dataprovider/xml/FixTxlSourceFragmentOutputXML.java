package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import ca.usask.cs.srlab.simcad.SimcadException;

public class FixTxlSourceFragmentOutputXML implements
		IXMLFragmentDataProviderTransformer {

	@Override
	public String transform(Object sourceFileName) {
		String newFileName = ((String) sourceFileName).subSequence(0, ((String) sourceFileName).lastIndexOf('.'))+"_fix.xml";
		
		File newf = new File(newFileName);
		if (newf.exists())
			newf.delete();
		
		try{
			BufferedReader inputReader = new BufferedReader(new FileReader((String) sourceFileName));

			PrintWriter out = new PrintWriter(new FileWriter(newFileName));
			
			String lin;
	
			out.println("<project>");
			out.println("<name></name>");
			out.println("<description></description>");
			out.println("<prog_language></prog_language>");
			out.println("<source_elements>");
			
			// If line begins with "Received:", ditch it, and its continuations
			while ((lin = inputReader.readLine()) != null) {
				if (lin.startsWith("<source file")) {
					out.println(lin + "<![CDATA[");
					// out.println("<![CDATA[");
				} else if (lin.startsWith("</source>")) {
					// out.println("]]>");
					out.println("]]>" + lin);
				} else {
					if (lin.contains("<![CDATA[")) {
						String st = lin
								.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA ");
						out.println(st.replaceAll("]]>", "ENDOFCDATA "));
					} else if (lin.contains("]]>")) {
						String st = lin.replaceAll("]]>", "ENDOFCDATA ");
						out.println(st.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA "));
					} else
						out.println(lin);
				}
			}
			out.println("</source_elements>");
			out.println("</project>");
			inputReader.close();
			out.close();
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new SimcadException("unable to fix xml :" + sourceFileName, e);
		}
		return newFileName;
	}

}
