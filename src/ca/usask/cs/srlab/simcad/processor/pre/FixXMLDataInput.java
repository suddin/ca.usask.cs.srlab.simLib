package ca.usask.cs.srlab.simcad.processor.pre;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FixXMLDataInput {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String granularity[] = {/*"functions", */"blocks"};
		String transformation[] = {"","-consistent"/*,"-blind"*/};
		
		String projects[]={"apache-mina-2.0.3","apache-ant-1.8.2","apache-tomcat-7.0.11","eclipse-3.6.2_jdt.core","jboss-5.1.0.GA-src","linux-2.6.38","firefox-2.0.0.4","httpd-2.2.17","mysql-5.5.10","weltab"};
		String langs[]={"java","java","java","java","java","c","c","c","c"};
		
		for(int i = 1; i < 2; i++){
		for(String gran : granularity){
			for(String trans : transformation){
		String fileName = //"/home/sharif/clonetest/final_test_projects/java/apache-ant-1.8.2_functions-consistent-clones/apache-ant-1.8.2_functions-consistent-clones-0.3-withsource.xml";
			"/home/sharif/clonetest/final_test_projects/"+langs[i]+"/"+projects[i]+"_"+gran+trans+"-clones/"+projects[i]+"_"+gran+trans+".xml";
		//File old = new File(fileName );
	    processFile(fileName);
		
			}
		}
		}
		
		System.out.println("Done!");
	}

	public static void processFile(String fileName)
			throws FileNotFoundException, IOException {
		String newFileName = fileName.subSequence(0, fileName.lastIndexOf('.'))+"_fix.xml";
	    File newf = new File(newFileName);
	    if(newf.exists()) newf.delete();
	    
	    BufferedReader is =
	        new BufferedReader(new FileReader(fileName));
	    PrintWriter out = new PrintWriter(new FileWriter(newFileName));
	    
		String lin;

		out.println("<project>");
		out.println("<name></name>");
		out.println("<description></description>");
		out.println("<prog_language></prog_language>");
		out.println("<source_elements>");
		
		// If line begins with "Received:", ditch it, and its continuations
		while ((lin = is.readLine()) != null) {
			if (lin.startsWith("<source file")) {
				out.println(lin+"<![CDATA[");
				//out.println("<![CDATA[");
			}else if(lin.startsWith("</source>")){
				//out.println("]]>");
				out.println("]]>"+lin);
			}else{
				if(lin.contains("<![CDATA[")){
					String st = lin.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA ");
					out.println(st.replaceAll("]]>", "ENDOFCDATA "));
				}else if(lin.contains("]]>")){
					String st = lin.replaceAll("]]>", "ENDOFCDATA ");
					out.println(st.replaceAll("<!\\[CDATA\\[", "BEGINOFCDATA "));
				}else
					out.println(lin);
			}
		}
		out.println("</source_elements>");
		out.println("</project>");
		is.close();
		out.close();
	}

}

