package ca.usask.cs.srlab.simcad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.xml.XMLMultiSourceFragmentDataProviderConfiguration;
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

public class SimCadXML {
	
	public static void main(String args[]) throws IOException{
		
		if(args.length < 5){
			printUsage();
			return;
		}
		
		boolean verbose = false;
		String o_source_xml = null;
		String t_source_xml = null;
		String output_dir = null;
		String granularity = null;
		String cloneType [] = null;
		String cloneGrouping =null;
		
		int argIndex = 0;
		
		if((args[argIndex]).equals("-v")){
			verbose = true;
			argIndex++;
		}
		
		if(FileUtil.isFileExist(args[argIndex])){
			o_source_xml = args[argIndex++];
		}else{
			System.out.println("First argument \""+args[argIndex]+"\" must be a valid source file");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && !args[argIndex].equals("NONE") && (FileUtil.isFileExist(args[argIndex]))){
			t_source_xml = args[argIndex++];
		}else{
			if(!args[argIndex].equals("NONE")){
				System.out.println("Second argument \""+args[argIndex]+"\" must be 'NONE' or a valid file");
				printUsage();
				return;
			}else
				t_source_xml = o_source_xml;
			argIndex++;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(CloneFragment.CLONE_GRANULARITY_FUNCTION) || args[argIndex].equals(CloneFragment.CLONE_GRANULARITY_BLOCK) ) ){
			granularity = args[argIndex++];
		}else{
			System.out.println("Third argument \""+args[argIndex]+"\" must be either \"function\" or \"block\"");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals("1") || args[argIndex].equals("2") || args[argIndex].equals("3")
				|| args[argIndex].equals("12") || args[argIndex].equals("23") || args[argIndex].equals("nearmiss") || args[argIndex].equals("13")
				|| args[argIndex].equals("123")|| args[argIndex].equals("all"))){
			cloneType = CloneSet.CloneTypeMapper.getTypeFromString(args[argIndex++]);
		}else{
			System.out.println("Fourth argument \""+args[argIndex]+"\" must be one of : 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all)");
			printUsage();
			return;
		}
		
		if(args[argIndex] != null && (args[argIndex].equals(SimCadConstants.CLONE_SET_TYPE_GROUP) || args[argIndex].equals(SimCadConstants.CLONE_SET_TYPE_PAIR))){
			cloneGrouping = args[argIndex++];
		}else{
			System.out.println("Fifth argument \""+args[argIndex]+"\" must be one of : group | pair");
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
			output_dir = new File(o_source_xml).getParent() + SimCadConstants.DEFAULT_OUTPUT_FOLDER_SUFFIX;
			try{ //trying to create the output directory
				FileUtil.createDirs(output_dir);
			}catch (Exception e) {
				System.out.println("Error creating output directory \""+args[argIndex]+"\", argument must be a valid directory path");
				printUsage();
				return;		
			}
		}
		
		//clone detection settings 
		DetectionSettings detectionSettings = new DetectionSettings("", granularity, cloneGrouping, "", verbose, cloneType);
		
		detectionSettings.getDetectionReport().setSourceFolder(new File(o_source_xml).getParent());
		detectionSettings.getDetectionReport().setOutputFolder(output_dir);
		
		//
		XMLMultiSourceFragmentDataProviderConfiguration xmlDataProviderConfig = new XMLMultiSourceFragmentDataProviderConfiguration(
				o_source_xml, t_source_xml,
				null, granularity);
		
		xmlDataProviderConfig.addDataTransformer(new FixTxlSourceFragmentOutputXML());
		IFragmentDataProvider xmlMultiSourceFragmentDataProvider = new XMLMultiSourceFragmentDataProvider(xmlDataProviderConfig);
		
		//source data extraction
		//FileSystemFragmentDataProviderConfiguration dataProviderConfig = new FileSystemFragmentDataProviderConfiguration(source_dir, output_dir, language, transformation, granularity);
		//IFragmentDataProvider cloneFragmentDataProvider = new FileSystemFragmentDataProvider(dataProviderConfig);
		
		//index generation
		ICloneIndex cloneIndex = IndexFactory.LoadIndexHolder();
		IndexBuilder indexBuilder = new IndexBuilder(xmlMultiSourceFragmentDataProvider);
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings);
		
		//prepare input for clone detection: here the whole project is the input
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		
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
	

	//java -Xmx1024m -cp ${lib}/tools/simLib.jar ca.usask.cs.srlab.simcad.SimCadXML ${VERBOSE} "${O_SOURCE_XML}" "${T_SOURCE_XML}" ${GRANULARITY} ${CLONE_TYPE} ${CLONE_GROUPING} "${OUTPUT_DIR}" 2>&1
	
	private static void printUsage() {
		System.out.println("Usage: SimCadXML [-v] o_source_xml t_source_xml granularity clone_type clone_grouping [output_path]\n");
		System.out.println("-v                = verbose mode, shows the detection in progress");
		System.out.println("o_source_xml      = absolute path to xml file containing original source fragments");
		System.out.println("t_source_xml      = absolute path to xml file containing transformed source fragments");
		System.out.println("granularity       = block | function : default = function");
		System.out.println("clone_type        = 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all) : default = (123 | all)");
		System.out.println("clone_grouping    = group | pair : default = group");
		System.out.println("output_path       = absolute path to output folder : default = {source_path}_simad_clone");
	}
}
