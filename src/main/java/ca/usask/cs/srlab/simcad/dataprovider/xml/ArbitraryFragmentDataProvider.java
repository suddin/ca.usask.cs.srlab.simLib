package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.util.LinkedList;
import java.util.List;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;
import ca.usask.cs.srlab.simcad.util.SourceUtil;

public class ArbitraryFragmentDataProvider extends XMLMultiSourceFragmentDataProvider {

	Integer selectedTextFromLine; 
	Integer selectedTextToLine;
	
	public ArbitraryFragmentDataProvider(XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig, Integer selectedTextFromLine, 
	Integer selectedTextToLine){
		super(dataProviderConfig);
		this.selectedTextFromLine = selectedTextFromLine; 
		this.selectedTextToLine = selectedTextToLine;
	}
	
//	public XMLMultiSourceCloneFragmentDataProvider(XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig, DetectionSettings ds){
//		super(dataProviderConfig, ds);
//	}
	
	public List<CloneFragment> extractFragments(){
		
		initDataSource();
		
		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();
		Integer minSizeOfGranularity = PropsUtil.getMinSizeOfGranularity();
		boolean fileUrlRelative = PropsUtil.getIsFragmentFileRelativeURL();
		
		Integer items = 0;
		
		for(; orirginalRawFragments.hasNext();){
			RawFragment originalSource = orirginalRawFragments.next();
			RawFragment transformedSource = transformedRawFragments.next();

			String originalContent = originalSource.getContent();
			if(CloneFragment.computeActualLineOfCode(originalContent) < minSizeOfGranularity) continue;

			String file = originalSource.file;
			String startline = originalSource.startLine;
			String endline = originalSource.endLine;
			
			String fileT = transformedSource.file;
			String startlineT = transformedSource.startLine;
			String endlineT = transformedSource.endLine;
			
			
			//System.out.println(file +" ("+startline+","+endline+")");
			//System.out.println(fileT +" ("+startlineT+","+endlineT+")");
			
			if(!file.equals(fileT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at file) are not synced to each other");
			}else if(!startline.equals(startlineT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at startline) are not synced to each other");
			}if(!endline.equals(endlineT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at endline) are not synced to each other");
			}
			
			if(fileUrlRelative){
				//file = file.replace(dataProviderConfig.getSourceDaraRootUrl(), "");
				file = file.substring(dataProviderConfig.getSourceDataRootUrl().length());
			}
//			System.out.println(file);
			
			String transformedContent = transformedSource.getContent();
			
			
			int startLine = Integer.valueOf(startlineT);
			int endLine = Integer.valueOf(endlineT);
			int finalStartLine = startLine;
			int finalEndLine = endLine;
			
			if(endLine < selectedTextFromLine) //above selection
				continue;
			else if (endLine <= selectedTextToLine){  //endline under selection
				if(startLine < selectedTextFromLine){ //startline goes above selection
					//do top clipping
					int lineToClip = (selectedTextFromLine - startLine);
					originalContent = SourceUtil.doTopClip(originalContent, lineToClip);
					transformedContent = SourceUtil.doTopClip(transformedContent, lineToClip);
					
					finalStartLine = finalStartLine + lineToClip;
				}
				else{ 
					//all in selection
				}
			} else { //endline below selection
				if(startLine < selectedTextFromLine){ //startline goes above selection
					//do top and bottom clipping
					int lineToClip = (selectedTextFromLine - startLine);
					//top
					originalContent = SourceUtil.doTopClip(originalContent, lineToClip);
					transformedContent = SourceUtil.doTopClip(transformedContent, lineToClip);
					
					finalStartLine = finalStartLine + lineToClip;
					
					//bottom
					lineToClip = (endLine - selectedTextToLine);
					
					originalContent = SourceUtil.doBottomClip(originalContent, lineToClip);
					transformedContent = SourceUtil.doBottomClip(transformedContent, lineToClip);
					
					finalEndLine = finalEndLine - lineToClip;
				}
				else if (startLine <= selectedTextToLine){ 
					//to bottom clipping
					int lineToClip = (endLine - selectedTextToLine);
					
					originalContent = SourceUtil.doBottomClip(originalContent, lineToClip);
					transformedContent = SourceUtil.doBottomClip(transformedContent, lineToClip);
				
					finalEndLine = finalEndLine - lineToClip;
				}else{ //startline goes below selection
					continue;
				}
			}
			
			CloneFragment cloneFragment = createNewCloneFragment(file, String.valueOf(finalStartLine),  String.valueOf(finalEndLine), originalContent, transformedContent, items++/*, transformedContent, 0, 0*/);
			cloneFragmentList.add(cloneFragment);
		}
		
		return cloneFragmentList;
	}
	
}
