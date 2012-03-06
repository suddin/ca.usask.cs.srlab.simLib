package ca.usask.cs.srlab.simcad.processor.post;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

public class HtmlOutputProcessor  extends AbstractProcessor {

	private String htmlFileName;

	public HtmlOutputProcessor(String htmlFileName) {
		this(htmlFileName, null);
	}
	
	public HtmlOutputProcessor(String htmlFileName, String message) {
		super(message);
		this.htmlFileName = htmlFileName;
	}
	
	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		super.process(inputCloneSets, outputCloneSets);
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(htmlFileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SimcadException("Unable to open file:" + htmlFileName, e);
		}
		
		printWriter.println("<html>");
		
		printWriter.println("<head>");
		printWriter.println("<title>SimCad Clones<title>");
		printWriter.println("</head>");
		
		printWriter.println("<body>");
		
		printWriter.println("<h1>TODO</h1>");
		
	    printWriter.println("</body>");
	    printWriter.println("</html>");
	    
	    printWriter.close();
		
		outputCloneSets.addAll(inputCloneSets);
		return true;
	}

}
