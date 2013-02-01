package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProvider;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class XMLMultiSourceFragmentDataProvider extends
		AbstractFragmentDataProvider {

	protected Iterator<RawFragment> orirginalRawFragments;
	protected Iterator<RawFragment> transformedRawFragments;

	@SuppressWarnings("unused")
	private XMLMultiSourceFragmentDataProvider() {
	}

	public XMLMultiSourceFragmentDataProvider(
			XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig) {
		super(dataProviderConfig);
	}

	public void initDataSource() {
		String dataSource[] = applyDataTransformation();
		orirginalRawFragments = RawFragmentReader.readRawFragments(
				dataSource[0]).iterator();
		transformedRawFragments = RawFragmentReader.readRawFragments(
				dataSource[1]).iterator();
	}

	public List<CloneFragment> extractFragments() {

		initDataSource();

		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();
		Integer minSizeOfGranularity = PropsUtil.getMinSizeOfGranularity();
		boolean fileUrlRelative = PropsUtil.getIsFragmentFileRelativeURL();

		Integer items = 0;
		boolean lastMatchingFailed = false;
		RawFragment originalSource, transformedSource = null;

		for (; orirginalRawFragments.hasNext();) {

			originalSource = orirginalRawFragments.next();

			if (!lastMatchingFailed) {
				transformedSource = transformedRawFragments.next();
			}

			if (CloneFragment.computeActualLineOfCode(originalSource
					.getContent()) < minSizeOfGranularity)
				continue;

			if (!originalSource.file.equals(transformedSource.file)) {
				lastMatchingFailed = true;
				continue;
				// throw new
				// SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at file) are not synced to each other");
			} else if (!originalSource.startLine
					.equals(transformedSource.startLine)) {
				lastMatchingFailed = true;
				continue;
				// throw new
				// SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at startline) are not synced to each other");
			}
			if (!originalSource.endLine.equals(transformedSource.endLine)) {
				lastMatchingFailed = true;
				continue;
				// throw new
				// SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at endline) are not synced to each other");
			}

			String file = originalSource.file;

			if (fileUrlRelative) {
				// file =
				// file.replace(dataProviderConfig.getSourceDaraRootUrl(), "");
				file = file.substring(dataProviderConfig.getSourceDataRootUrl()
						.length());
			}
			// System.out.println(file);

			CloneFragment cloneFragment = createNewCloneFragment(file,
					originalSource.startLine, originalSource.endLine,
					originalSource.content, transformedSource.content, items++);
			cloneFragmentList.add(cloneFragment);
			lastMatchingFailed = false;
		}

		return cloneFragmentList;
	}

	// @Override
	protected String[] applyDataTransformation() {
		List<IXMLFragmentDataProviderTransformer> dataTransformerList = ((XMLMultiSourceFragmentDataProviderConfiguration) dataProviderConfig)
				.getDataTransformer();
		String dataSource1 = ((XMLMultiSourceFragmentDataProviderConfiguration) dataProviderConfig)
				.getOriginalSourceXmlFileName();
		String dataSource2 = ((XMLMultiSourceFragmentDataProviderConfiguration) dataProviderConfig)
				.getTransformedSourceXmlFileName();

		for (IXMLFragmentDataProviderTransformer xmlFragmentDataProviderTransformer : dataTransformerList) {
			dataSource1 = xmlFragmentDataProviderTransformer
					.transform(dataSource1);
			dataSource2 = xmlFragmentDataProviderTransformer
					.transform(dataSource2);
		}
		return new String[] { dataSource1, dataSource2 };
	}

}
