package ca.usask.cs.srlab.simcad.model;

/**
 * @author sharif
 * 
 */
public interface ICloneFragment {

	Long getId();

	Integer getProgramComponentId();

	Integer getFromLine();

	Integer getToLine();

	String getOriginalCodeBlock();

	String getFileName();

	Long getSimhash1();

	Long getSimhash2();

	Integer getOneBitCount();

	Integer getLineOfCode();
}
