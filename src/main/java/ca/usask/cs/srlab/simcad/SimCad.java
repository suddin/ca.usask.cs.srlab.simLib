package ca.usask.cs.srlab.simcad;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.io.Files;

import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.detection.CloneDetector;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.IndexBuilder;
import ca.usask.cs.srlab.simcad.index.IndexFactory;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.IProcessor;
import ca.usask.cs.srlab.simcad.processor.ProcessorDisptacher;
import ca.usask.cs.srlab.simcad.processor.post.DetectionSummaryPrinter;
import ca.usask.cs.srlab.simcad.processor.post.SubsumedCloneFilter;
import ca.usask.cs.srlab.simcad.processor.post.XmlOutputProcessor;
import ca.usask.cs.srlab.simcad.util.FileUtil;
import ca.usask.cs.srlab.simcad.util.SourceUtil;

public class SimCad {
	
	public static void main(String args[]) throws IOException{
		
		if(args.length < 7){
			printUsage();
			return;
		}
		
		boolean verbose = false;
		String item_to_search = null;
		String source_dir = null;
		String output_dir = null;
		String language = null;
		String granularity = null;
		String cloneType [] = null;
		String cloneGrouping =null;
		String transformation = null;
		
		int argIndex = 0;
		
		if((args[argIndex]).equals("-v")){
			verbose = true;
			argIndex++;
		}
		
		if(args[argIndex] != null && !args[argIndex].equals("NONE") && (FileUtil.isFileExist(args[argIndex]) || FileUtil.isDirExist(args[argIndex]))){
			item_to_search = args[argIndex++];
		}else{
			if(!args[argIndex].equals("NONE")){
				System.out.println("First argument \""+args[argIndex]+"\" must be 'NONE' or a valid file/directory");
				printUsage();
				return;
			}
			argIndex++;
		}
		
		if(FileUtil.isDirExist(args[argIndex])){
			source_dir = args[argIndex++];
		}else{
			System.out.println("Second argument \""+args[argIndex]+"\" must be a valid source directory");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(SimCadConstants.LANGUAGE_C) || args[argIndex].equals(SimCadConstants.LANGUAGE_CS) ||
				args[argIndex].equals(SimCadConstants.LANGUAGE_JAVA) || args[argIndex].equals(SimCadConstants.LANGUAGE_PYTHON) ) ){
			language = args[argIndex++];
		}else{
			System.out.println("Third argument \""+args[argIndex]+"\" must be one of : c | java | cs | py");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(CloneFragment.CLONE_GRANULARITY_FUNCTION) || args[argIndex].equals(CloneFragment.CLONE_GRANULARITY_BLOCK) ) ){
			granularity = args[argIndex++];
		}else{
			System.out.println("Fourth argument \""+args[argIndex]+"\" must be either \"function\" or \"block\"");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals("1") || args[argIndex].equals("2") || args[argIndex].equals("3")
				|| args[argIndex].equals("12") || args[argIndex].equals("23") || args[argIndex].equals("nearmiss") || args[argIndex].equals("13")
				|| args[argIndex].equals("123")|| args[argIndex].equals("all"))){
			cloneType = CloneSet.CloneTypeMapper.getTypeFromString(args[argIndex++]);
		}else{
			System.out.println("Fifth argument \""+args[argIndex]+"\" must be one of : 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all)");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(SimCadConstants.CLONE_SET_TYPE_GROUP) || args[argIndex].equals(SimCadConstants.CLONE_SET_TYPE_PAIR))){
				cloneGrouping = args[argIndex++];
		}else{
			System.out.println("Sixth argument \""+args[argIndex]+"\" must be one of : group | pair");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS) || args[argIndex].equals(SimCadConstants.SOURCE_TRANSFORMATION_APPROACH_GREEDY))){
//			if(args[argIndex].equals(Constants.SOURCE_TRANSFORMATION_APPROACH_GENEROUS))
//				transformation = Constants.SOURCE_TRANSFORMATION_ACTION_CONSISTENT;
//			else
//				transformation = Constants.SOURCE_TRANSFORMATION_ACTION_BLIND;
//			argIndex++;
			transformation = args[argIndex++];
		}else{
			System.out.println("Seventh argument \""+args[argIndex]+"\" must be either \"generous\" or \"greedy\"");
			printUsage();
			return;
		}
		
		if(argIndex < args.length){
			output_dir = args[argIndex];
			if(!FileUtil.isDirExist(output_dir)){
				try{ //trying to create the output directory
					FileUtil.createDirs(output_dir);
				}catch (Exception e) {
					System.out.println("Error creating output directory \""+args[argIndex]+"\", argument must be a valid directory path");
					printUsage();
					return;		
				}
			}
		}
		
		if(output_dir == null){
			output_dir = source_dir + SimCadConstants.DEFAULT_OUTPUT_FOLDER_SUFFIX;
			try{ //trying to create the output directory
				FileUtil.createDirs(output_dir);
			}catch (Exception e) {
				System.out.println("Error creating output directory \""+args[argIndex]+"\", argument must be a valid directory path");
				printUsage();
				return;		
			}
		}
		
		//clone detection settings 
		DetectionSettings detectionSettings = new DetectionSettings(language, granularity, cloneGrouping, transformation, verbose, cloneType);
		
		detectionSettings.getDetectionReport().setSourceFolder(source_dir);
		detectionSettings.getDetectionReport().setOutputFolder(output_dir);
		
		
		//source data extraction
		FileSystemFragmentDataProviderConfiguration dataProviderConfig = new FileSystemFragmentDataProviderConfiguration(source_dir, output_dir, language, transformation, granularity);
		IFragmentDataProvider cloneFragmentDataProvider = new FileSystemFragmentDataProvider(dataProviderConfig);
		
		//index generation
		ICloneIndex cloneIndex = IndexFactory.LoadIndexHolder();
		IndexBuilder indexBuilder = new IndexBuilder(cloneFragmentDataProvider);
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings);
		
		//prepare input for clone detection: here the whole project is the input
		Collection<CloneFragment> candidateFragments = null;
		if(item_to_search != null){
			File searchItemFile = new File(item_to_search);
			candidateFragments = SourceUtil.getCandidateFragmentsFromFile(searchItemFile, detectionSettings);
		}else
			candidateFragments = cloneIndex.getAllEntries();
		
		//detection
		CloneDetector cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings);
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
		//post-processing
		ProcessorDisptacher processorDisptacher = ProcessorDisptacher.getInstance();

		if(detectionSettings.getCloneGranularity().equals(CloneFragment.CLONE_GRANULARITY_BLOCK)){
			SubsumedCloneFilter sbsmfilter = new SubsumedCloneFilter(detectionSettings, "Process subsume filtering...");
			processorDisptacher.addProcessor(sbsmfilter);
		}

		//post-processing : write clones to xml file
		IProcessor xmlOutputProcessor = new XmlOutputProcessor(detectionSettings, output_dir);
		processorDisptacher.addProcessor(xmlOutputProcessor).applyOn(result, detectionSettings);
		
		//log printer
		String logFileName = output_dir + System.getProperty("file.separator") + "simcad_"+detectionSettings.getCloneGranularity()+"-clones-"+detectionSettings.getTypeString()+"_"+detectionSettings.getSourceTransformation()+".log";
		PrintWriter logPrinter;
		try {
			logPrinter = new PrintWriter(new FileWriter(logFileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SimcadException("Can not create output file", e);
		}
		IProcessor logWriter = new DetectionSummaryPrinter(detectionSettings, logPrinter, "Writing log file...");
		
		//console printer 
		PrintWriter consolePrinter = new PrintWriter(System.out, true);
		IProcessor consoleWriter = new DetectionSummaryPrinter(detectionSettings, consolePrinter);
	
		//display/write detection summary
		processorDisptacher.cleanUp();
		processorDisptacher.addProcessor(logWriter).addProcessor(consoleWriter).applyOn(result, detectionSettings);
	}
	
	private static void printUsage() {
		System.out.println("Usage: Simcad [-v] item_to_search source_path language granularity clone_type clone_grouping source_transform [output_path]\n");
		System.out.println("-v                = verbose mode, shows the detection in progress");
		System.out.println("language          = c | java | cs | py");
		System.out.println("granularity       = block | function : default = function");
		System.out.println("clone_type        = 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all) : default = (123 | all)");
		System.out.println("clone_grouping    = group | pair : default = group");
		System.out.println("source_transform  = generous | greedy : default = generous");
		System.out.println("item_to_search    = 'NONE' or absolute path to file containing search item");
		System.out.println("source_path       = absolute path to source folder");
		System.out.println("output_path       = absolute path to output folder : default = {source_path}_simad_clone");
	}
}
