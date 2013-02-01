package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class FixTxlSourceFragmentOutputXML implements IXMLFragmentDataProviderTransformer {

	boolean unicodeFilterOn;
	
	public FixTxlSourceFragmentOutputXML(){
		unicodeFilterOn = PropsUtil.isUnicodeFilterOn();
	}
	
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
			
			while ((lin = inputReader.readLine()) != null) {
				if (lin.startsWith("<source file")) {
					if(lin.endsWith(">"))
						out.println(lin + "<![CDATA[");
					else{ //sometimes long filename breaks it in multiple lines
						StringBuffer sb = new StringBuffer(lin);
						while ((lin = inputReader.readLine()) != null) {
							if(lin.endsWith(">")){
								out.println(sb.toString() + lin + "<![CDATA[");
								break;
							}
							else
								sb.append(lin);
						}
					}
					// out.println("<![CDATA[");
				} else if (lin.startsWith("</source>")) {
					// out.println("]]>");
					out.println("]]>" + lin);
				} else {
					
					if(unicodeFilterOn){
						StringBuilder sb = new StringBuilder(lin.trim());
						int index = 0;
						for(Character c : sb.toString().toCharArray()){
							if(Character.getType(c) == Character.CONTROL)
								sb.deleteCharAt(index);
							else
								index++;
						}
						lin = sb.toString();
					}
					
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
