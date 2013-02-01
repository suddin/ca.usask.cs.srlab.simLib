package ca.usask.cs.srlab.simcad.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemArbitraryFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.filesystem.FileSystemFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class SourceUtil {

	public static List<CloneFragment> getCandidateFragmentsFromFile( File item_to_search, DetectionSettings detectionSettings) {
		
		String code_fragment_to_search = FileUtil.getFileContents(item_to_search);
		
		return getCandidateFragmentFromCodeBlock(code_fragment_to_search, detectionSettings);
	}
	
	@Deprecated
	public static List<CloneFragment> getCandidateFragmentsFromFile( File item_to_search, int fromLine, int toLine,
			DetectionSettings detectionSettings) {
		
		String code_fragment_to_search = FileUtil.getFileContents(item_to_search, fromLine, toLine);
		
		return getCandidateFragmentFromCodeBlock(code_fragment_to_search, detectionSettings);
	}
	
	@Deprecated
	public static List<CloneFragment> getCandidateFragmentFromCodeBlock( String codeBlock, 
			DetectionSettings detectionSettings) {
		
		File tmp_file = null;
		try{
			tmp_file = FileUtil.createTempJavaFileWithContent(codeBlock);
			return buildCandidateFragmentsFromFile(tmp_file, detectionSettings);
		}
		finally{
			FileUtil.deleteDirectory(tmp_file.getParent());
		}
	}
	
	public static List<CloneFragment> buildCandidateFragmentsFromFileWithRangeSelection(
			File selectedTextFile, Integer selectedTextFromLine,
			Integer selectedTextToLine, DetectionSettings detectionSettings) {

		return buildCandidateFragmentsFromFile(selectedTextFile,
				selectedTextFromLine, selectedTextToLine, detectionSettings);

	}

	private static List<CloneFragment> buildCandidateFragmentsFromFile(File selectedFile, DetectionSettings detectionSettings) {
		return buildCandidateFragmentsFromFile(selectedFile, null, null, detectionSettings);
	}
	
	private static List<CloneFragment> buildCandidateFragmentsFromFile(File itemToSearch, Integer selectedTextFromLine, Integer selectedTextToLine, DetectionSettings detectionSettings) {
	
		List<CloneFragment> candidateFragments = new ArrayList<CloneFragment>();
		
		File tmpSourceFile = FileUtil.createTempJavaFileWithContent(FileUtil.getFileContents(itemToSearch));
		
		//String tempdir = System.getProperty("java.io.tmpdir");
		
		File sourceDir = tmpSourceFile.isDirectory() ? tmpSourceFile : new File(tmpSourceFile.getParent());
		File outputDir = null;
		try{
			outputDir = FileUtil.createTempDirectory();
			FileSystemFragmentDataProviderConfiguration dataProviderConfig = new FileSystemFragmentDataProviderConfiguration(
					sourceDir.getAbsolutePath(), outputDir.getAbsolutePath(),
					detectionSettings.getLanguage(),
					detectionSettings.getSourceTransformation(),
					detectionSettings.getCloneGranularity(), true);
			IFragmentDataProvider cloneFragmentDataProvider = (selectedTextFromLine != null && selectedTextToLine != null) ? 
					new FileSystemArbitraryFragmentDataProvider(dataProviderConfig, selectedTextFromLine, selectedTextToLine) : 
					new FileSystemFragmentDataProvider(dataProviderConfig);
			candidateFragments = cloneFragmentDataProvider.extractFragments();
		}
		finally{
			FileUtil.deleteDirectory(sourceDir);
			FileUtil.deleteDirectory(outputDir);
		}
		
		for(CloneFragment cloneFragment : candidateFragments){
			cloneFragment.setFileName(itemToSearch.getAbsolutePath());
		}
		
		return candidateFragments;
	}
	
	public static String doTopClip(String content, int lineToClip){
		content = content.trim();
		for (int i = 0; i < lineToClip; i ++){
			content = content.substring(content.indexOf("\n")+1);
		}
		return content;
	}
	
	public static String doBottomClip(String content, int lineToClip){
		content = content.trim();
		for (int i = 0; i < lineToClip; i ++){
			content = content.substring(0, content.lastIndexOf("\n"));
		}
		return content;
	}
	
}
