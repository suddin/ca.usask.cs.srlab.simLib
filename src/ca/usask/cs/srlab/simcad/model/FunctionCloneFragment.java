package ca.usask.cs.srlab.simcad.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class FunctionCloneFragment extends CloneFragment {

	@Column
	private String functionSignature;

	public FunctionCloneFragment(){
		super();
	}
	
	public FunctionCloneFragment(String fileName, Integer fromLine,
			Integer toLine, Integer programComponentId, String codeBlock, /*Long id,*/
			String transformedCodeBlock, long simhash1, long simhash2) {
		super(fileName, fromLine, toLine, programComponentId, codeBlock, /*id,*/ 
				transformedCodeBlock, simhash1, simhash2);
		this.functionSignature = extractfunctionSignature(codeBlock);
	}

	private String extractfunctionSignature(String codeBlock) {
		return codeBlock != null ? codeBlock.split("\n")[0] : "";
	}

	public String getFunctionName() {
		return functionSignature;
	}

	public String getFunctionSignature() {
		return functionSignature;
	}

	public void setFunctionSignature(String functionSignature) {
		this.functionSignature = functionSignature;
	}
	
}
