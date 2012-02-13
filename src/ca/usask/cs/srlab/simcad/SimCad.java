package ca.usask.cs.srlab.simcad;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.postprocess.SubsumeFilterProcessor;
import ca.usask.cs.srlab.simcad.postprocess.XmlOutputProcessor;
import ca.usask.cs.srlab.simcad.preprocessor.FixXMLDataInput;
import ca.usask.cs.srlab.simcad.processor.ProcessorDisptacher;

public class SimCad {

	public static final String CLONE_TYPE_1 = "Type 1";

	
	//simhashGenerator.applyTokenBuildStrategy(TokenBuildStrategyFactory.getStrategyInstanceFor(CLONE_TYPE_1));
	
	int simThreshold = 0;
	
//	boolean strictOnMembership = false;
//	double clusterMembershipRatio = 0.5;	//for a candidate item, it is the ratio of how many items are matched are matched vs total items in a cluster 
	double locTolerance = 1;
//	
//	int minClusterSize = 2;
//	int minSizeOfGranularity = 5;
	int simThreshold2;
	
	int nGroup = 0;
	int nFragment =0; //fragments
	
	boolean applySubsumeFiltering;
	
	private DetectionSettings detectionSettings;

	public SimCad(int simThreshold, boolean applySubsumeFiltering) {
		super();
		this.simThreshold = simThreshold;
		this.applySubsumeFiltering = applySubsumeFiltering;
	}

	//@Before
	public void findClone(String rawFunctionsFileName, ICloneIndex cloneIndex,
			String cloneOutputFolder) throws IOException {

		PrintWriter logPrinter = new PrintWriter(new FileWriter(".log"));

		List<CloneSet> detectedCloneSets = new ArrayList<CloneSet>();

		//find clones here
		
		ProcessorDisptacher pd = ProcessorDisptacher.getInstance().cleanUp();

		// filter subsumed clone

		if (applySubsumeFiltering) {
			pd.addProcessor(new SubsumeFilterProcessor());
		}

		// add post processing functions
		XmlOutputProcessor xop = new XmlOutputProcessor(detectionSettings,
				cloneOutputFolder);
		pd.addProcessor(xop).applyOn(detectedCloneSets);

	}
	
	
	//SimcadCloneDetector source-dir granularity simthreshold transformation
	
	public static void main(String args[]) throws IOException{
		
		if(args.length < 3){
			printUsage();
			return;
		}
		
		String source_dir = null;
		String granularity = null;
		int simThreshold = 0;
		String transformation = "";
		
		if(isDirExist(args[0])){
			source_dir = args[0];
		}else{
			System.out.println("First argument \""+args[0]+"\" must be a valid source directory");
			printUsage();
			return;
		}
		
		
		if(args[1] != null && (args[1].equals("functions") || args[1].equals("blocks") ) ){
			granularity = args[1];
		}else{
			System.out.println("Second argument \""+args[1]+"\" must be either \"functions\" or \"blocks\"");
			printUsage();
			return;
		}
		
		
		if(args[2] != null && isInteger(args[2]) && Integer.parseInt(args[2]) >= 0 && Integer.parseInt(args[2]) <= 13 ){
			simThreshold = Integer.parseInt(args[2]);
		}else{
			System.out.println("Third argument \""+args[2]+"\" must be an integer between 0 to 13");
			printUsage();
			return;
		}
		
		if(args.length > 3 )
			if(args[3].equals("blind") || args[3].equals("consistent")){
				transformation = "-"+args[3];
			}else if (!args[3].equals("none")){
				System.out.println("Fourth argument \""+args[3]+"\" must be either \"blind\" or \"consistent\"");
				printUsage();
				return;
			}
		
		String projectName = source_dir.contains(System.getProperty("file.separator")) ? source_dir.substring(source_dir.lastIndexOf(System.getProperty("file.separator"))+1) : source_dir;
		
		String cloneFragmentFileName = source_dir.substring(0, source_dir.lastIndexOf(System.getProperty("file.separator"))+1)+projectName+"/"+granularity+".xml";
		
		String cloneOutputFolder = source_dir.substring(0, source_dir.lastIndexOf(System.getProperty("file.separator"))+1)+projectName+"/"+granularity+transformation+"-clones";
		
		String cloneFragmentFixFileName = cloneFragmentFileName.subSequence(0, cloneFragmentFileName.lastIndexOf('.'))+"_fix.xml";
		
		if(!isFileExist(cloneFragmentFixFileName))
			FixXMLDataInput.processFile(cloneFragmentFileName);
		
		ICloneIndex cloneIndex = null;
		
		SimCad scad = new SimCad(simThreshold, granularity.equals("blocks") ? true : false);
		
		scad.findClone(cloneFragmentFixFileName, cloneIndex, cloneOutputFolder);
		
		/*
		String cloneFragmentFixFileName = "/Users/sharif/Documents/workspace/simcad/src/ca/usask/cs/srlab/util/EIRC_functionsOLD_fix.xml";
		String cloneOutputFolder = "/Users/sharif/Documents/workspace/simcad/src/ca/usask/cs/srlab/util";
		
		int simThreshold = 13;
		String granularity = "functions";
		SimCadCloneDetector scad = new SimCadCloneDetector(simThreshold, granularity.equals("blocks") ? true : false);
		scad.findClone(cloneFragmentFixFileName, cloneOutputFolder);
		*/
		
	}
		
	private static boolean isDirExist(String dirname) {
		return (new File(dirname)).isDirectory();
	}

	private static boolean isFileExist(String filename) {
		return (new File(filename)).isFile();
	}
	
	public static boolean isFilenameValid(String file) {
		  File f = new File(file);
		  try {
		    f.getCanonicalPath();
		    return true;
		  } catch (IOException e) {
		    return false;
		  }
		}
	
	public static boolean isInteger( String input )  
	{  
	   try  
	   {  
	      Integer.parseInt( input );  
	      return true;  
	   }  
	   catch( Exception e)  
	   {  
	      return false;  
	   }  
	}
	
	private static void printUsage() {
		System.out.println("\nUsage: SimcadCloneDetector source-dir granularity simthreshold [transformation]\n\n");
	}

	int hamming_dist( long a1, long a2){		
		return Long.bitCount(a1^a2);
	}
	
}
