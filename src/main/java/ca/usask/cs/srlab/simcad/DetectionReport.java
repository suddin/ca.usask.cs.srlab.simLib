package ca.usask.cs.srlab.simcad;


public class DetectionReport {

	private String sourceFolder;
	private String outputFolder;
	
	private long preprocessingTime;
	private long detectionTime;
	private long postprocessingTime;
	
	private int nxCodeFragment;	//extracted
	private int nfCodeFragment; //filtered
	
	private int nCloneSet;
	private int nCloneFragment;
	
    public String getPreprocessingTime() {
		return formatTime(preprocessingTime);
	}
	public void setPreprocessingTime(long preprocessingTime) {
		this.preprocessingTime = preprocessingTime;
	}
	public String getDetectionTime() {
		return formatTime(detectionTime);
	}
	public void setDetectionTime(long detectionTime) {
		this.detectionTime = detectionTime;
	}
	public String getPostprocessingTime() {
		return formatTime(postprocessingTime);
	}
	public void setPostprocessingTime(long postprocessingTime) {
		this.postprocessingTime = postprocessingTime;
	}
	/**
	 * extracted code fragment
	 * @return
	 */
	public int getnxCodeFragment() {
		return nxCodeFragment;
	}
	/**
	 * extracted code fragment
	 * @return
	 */
	public void setnxCodeFragment(int nxCodeFragment) {
		this.nxCodeFragment = nxCodeFragment;
	}
	/**
	 * filtered code fragment
	 * @return
	 */
	public int getnfCodeFragment() {
		return nfCodeFragment;
	}
	/**
	 * filtered code fragment
	 * @return
	 */
	public void setnfCodeFragment(int nfCodeFragment) {
		this.nfCodeFragment = nfCodeFragment;
	}
	public int getnCloneSet() {
		return nCloneSet;
	}
	public void setnCloneSet(int nCloneSet) {
		this.nCloneSet = nCloneSet;
	}
	public int getnCloneFragment() {
		return nCloneFragment;
	}
	public void setnCloneFragment(int nCloneFragment) {
		this.nCloneFragment = nCloneFragment;
	}
	public String getSourceFolder() {
		return sourceFolder;
	}
	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}
	public String getOutputFolder() {
		return outputFolder;
	}
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	private String formatTime(long time) {
		return (time / 1000) / 60 + " min, " + (time / 1000) % 60 + " sec, "+ (time % 1000) + " ms";
	}
//	long start = System.currentTimeMillis();
//	
//	long end = System.currentTimeMillis();
//	long diff  = end - start;

}
