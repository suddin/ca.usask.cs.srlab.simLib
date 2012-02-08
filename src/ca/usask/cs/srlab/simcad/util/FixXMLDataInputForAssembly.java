package ca.usask.cs.srlab.simcad.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FixXMLDataInputForAssembly {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String fileName = "/Users/sharif/Documents/workspace/simcad/src/ca/usask/cs/srlab/util/EIRC_functionsOLD.xml";
	    processFile(fileName);
		System.out.println("Done!");
	}

	public static void processFile(String fileName)
			throws FileNotFoundException, IOException {
		String fixFileName = fileName.subSequence(0, fileName.lastIndexOf('.'))+"_fix.xml";
		
		File fixFile = new File(fixFileName);
	    if(fixFile.exists()) fixFile.delete();
	    
	    BufferedReader is =
	        new BufferedReader(new FileReader(fileName));
	    
	    PrintWriter outFixFile = new PrintWriter(new FileWriter(fixFileName));
	    //Map<Integer, Long> charTypeMap = new HashMap<Integer, Long>();
	    
		String lin;

		outFixFile.println("<project>");
		outFixFile.println("<name></name>");
		outFixFile.println("<description></description>");
		outFixFile.println("<prog_language></prog_language>");
		outFixFile.println("<source_elements>");
		
		// If line begins with "Received:", ditch it, and its continuations
		while ((lin = is.readLine()) != null) {
			if (lin.startsWith("<source file")) {
				outFixFile.println(lin+"<![CDATA[");
				//out.println("<![CDATA[");
			}else if(lin.startsWith("</source>")){
				//out.println("]]>");
				outFixFile.println("]]>"+lin);
			}else{
				/*if(lin.contains("<![CDATA[")){
					String st = lin.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA ");
					outFixFile.println(st.replaceAll("]]>", "ENDOFCDATA "));
				}else if(lin.contains("]]>")){
					String st = lin.replaceAll("]]>", "ENDOFCDATA ");
					outFixFile.println(st.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA "));
				}else*/
				
				StringBuilder sb = new StringBuilder(lin.trim());
				int index = 0;
				for(Character c : sb.toString().toCharArray()){
					if(Character.getType(c) == Character.CONTROL)
						sb.deleteCharAt(index);
					else
						index++;
				}
					outFixFile.println(/*SimhashGenerator.doDataRerinement*/sb.toString());
			}
		}
		outFixFile.println("</source_elements>");
		outFixFile.println("</project>");
		
//		for(Entry<Integer, Long> e : charTypeMap.entrySet()){
//			System.out.println("char type: " + e.getKey() + " count: " + e.getValue());
//		}
		
		is.close();
		outFixFile.close();
	}

}

