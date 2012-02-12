package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dataprovider.AbstractDataProvider;
import ca.usask.cs.srlab.simcad.dataprovider.IFragmentDataProviderConfiguration;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class GenericCloneFragmentDataProvider extends AbstractDataProvider{
	
	@SuppressWarnings("unused")
	private GenericCloneFragmentDataProvider(){
	}
	
	public GenericCloneFragmentDataProvider(IFragmentDataProviderConfiguration dataProviderConfig){
		this(dataProviderConfig, null);
	}
	
	public GenericCloneFragmentDataProvider(IFragmentDataProviderConfiguration dataProviderConfig, DetectionSettings ds){
		super(dataProviderConfig, ds);
	}
	
	public List<CloneFragment> extractFragments() {

		Object dataSource = applyDataTransformation();

		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();

		try {
			Integer items = 0;

			for (/* foreach item in dataSource */; dataSource != null;) {
				
				//start : extract values for these variables
				String fileName = "fileName";
				String startline = "startline";
				String endline = "endline";
				String originalContent = "content";
				String transformedContent = "content";
				//end
				
				int loc = computeLoc(transformedContent);

				if (loc < PropsUtil.getMinSizeOfGranularity())
					continue;

				CloneFragment cloneFragment = createNewCloneFragment(fileName,
						startline, endline, originalContent, transformedContent, items, 0, 0);

				long simhash[] = simhashGenerator.generateSimhash(cloneFragment);

				cloneFragment.setSimhash1(simhash[0]);
				cloneFragment.setSimhash2(simhash[1]);

				cloneFragmentList.add(cloneFragment);

				items++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SimcadException("unable to extract data", e);
		}

		return cloneFragmentList;
	}

	protected Object applyDataTransformation() {
		List<IXMLFragmentDataProviderTransformer> dataTransformerList = new ArrayList<IXMLFragmentDataProviderTransformer>(); //load transformers from dataProviderConfig
		Object dataSource = null;// //load original data source from dataProviderConfig
		for (IXMLFragmentDataProviderTransformer ixmlFragmentDataProviderTransformer : dataTransformerList) {
			dataSource = ixmlFragmentDataProviderTransformer.transform(dataSource);
		}
		return dataSource;
	}

	private CloneFragment createNewCloneFragment(String file, String startline,
			String endline, String originalContent, String transformedlContent, int index, long simhash1, long simhash2) {
		//TODO: take decision on function or block
		return new FunctionCloneFragment(file, Integer.valueOf(startline), Integer.valueOf(endline), index, originalContent, transformedlContent, simhash1, simhash2);
	}
	
	private int computeLoc(String content) {
		String []line = content.split("\n");
		int loc=0;
		for(String ln : line){
			if(ln.length() > 0)
				loc++;
		}
		return loc;
	}
	
}
