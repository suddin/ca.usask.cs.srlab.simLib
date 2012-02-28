package ca.usask.cs.srlab.simcad.processor.post;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

public class DetectionSummaryPrinter extends AbstractProcessor {

	private String message;
	private PrintWriter printWriter;
	private DetectionSettings detectionSettings;
	
	
	@SuppressWarnings("unused")
	private DetectionSummaryPrinter(){}
	
	public DetectionSummaryPrinter(DetectionSettings detectionSettings, PrintWriter printWriter) {
		super();
		this.detectionSettings = detectionSettings;
		this.printWriter = printWriter;
	}
	
	public DetectionSummaryPrinter(DetectionSettings detectionSettings, PrintWriter printWriter, String message) {
		super();
		this.detectionSettings = detectionSettings;
		this.printWriter = printWriter;
		this.message = message;
	}
	
	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,	Collection<CloneSet> outputCloneSets) {
		if(message!=null)
			System.out.println(message+"\n");
			
		printWriter.println("----------Detection Summary----------");
		printWriter.println("Date/Time	: "+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		printWriter.println("Source Location	: "+ detectionSettings.getDetectionReport().getSourceFolder());
		printWriter.println("Output Location	: "+ detectionSettings.getDetectionReport().getOutputFolder());

		printWriter.println("Source fragment type	: "+ detectionSettings.getCloneGranularity());
		
		printWriter.println("Clone type		: "+ detectionSettings.getTypeStringDetail());
		printWriter.println("Clone grouping type	: "+ detectionSettings.getCloneSetType());
		//printWriter.println("Total extracted fragment : "+ detectionSettings.getDetectionReport().getnxCodeFragment());	
		printWriter.println("Total source fragment 	: "+ detectionSettings.getDetectionReport().getnfCodeFragment());	
		printWriter.println("Total clone fragment	: "+ detectionSettings.getDetectionReport().getnCloneFragment());	
	    printWriter.println("Total clone group/pair	: "+ detectionSettings.getDetectionReport().getnCloneSet());

	    printWriter.println("Pre-Processing time	: " +detectionSettings.getDetectionReport().getPreprocessingTime());
	    printWriter.println("Detection time		: " +detectionSettings.getDetectionReport().getDetectionTime());
	    printWriter.println("Post-Processing time	: " +detectionSettings.getDetectionReport().getPostprocessingTime());
	    printWriter.println("");
	    printWriter.close();
		
		outputCloneSets.addAll(inputCloneSets);
		
		return true;
	}

}
