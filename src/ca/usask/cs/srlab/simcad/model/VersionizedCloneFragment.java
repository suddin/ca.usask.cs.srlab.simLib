package ca.usask.cs.srlab.simcad.model;

/**
 * @author sharif
 * 
 */
public class VersionizedCloneFragment {

	private CloneFragment cloneFragment;
	private String version;
	private String authors;
	private String blameAuthors;
	
	public VersionizedCloneFragment(CloneFragment cloneFragment,
			String version, String authors, String blameAuthors) {
		super();
		this.cloneFragment = cloneFragment;
		this.version = version;
		this.authors = authors;
		this.blameAuthors = blameAuthors;
	}

//	public VersionizedCloneFragment(String fileName, Integer fromLine,
//			Integer toLine, String codeBlock, /*Long id,
//			Integer programComponentId,*/ String transformedCodeBlock,
//			String version, String authors, String blameAuthors) {
//		this.cloneFragment = new CloneFragment(fileName, fromLine, toLine, codeBlock, /*id, programComponentId,*/
//				transformedCodeBlock);
//		this.version = version;
//		this.authors = authors;
//		this.blameAuthors = blameAuthors;
//	}

	public CloneFragment getCloneFragment() {
		return cloneFragment;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthors() {
		return authors;
	}

	public String getBlameAuthors() {
		return blameAuthors;
	}
}
