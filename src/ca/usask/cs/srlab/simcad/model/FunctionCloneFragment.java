package ca.usask.cs.srlab.simcad.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class FunctionCloneFragment extends CloneFragment {

	@Column
	private String functionSignature;

	@SuppressWarnings("unused")
	private FunctionCloneFragment(){
		super();
	}
	
	public FunctionCloneFragment(String fileName, Integer fromLine,
			Integer toLine, Integer programComponentId, String codeBlock/*, Long id,
			String transformedCodeBlock, long simhash1, long simhash2*/) {
		super(fileName, fromLine, toLine, programComponentId, codeBlock/*, id, 
				transformedCodeBlock, simhash1, simhash2*/);
		this.functionSignature = extractfunctionSignature(codeBlock);
	}

	private String extractfunctionSignature(String codeBlock) {
		if(codeBlock == null) return "";
		
		int function_end = getFileName().endsWith(".py") ? codeBlock.indexOf(":") - 1: codeBlock.indexOf("{") - 1;
		int bracket_start = codeBlock.lastIndexOf("(");
		int function_start = 0;
		/*
		boolean overTheName = false;
		for (int i = bracket_start - 1; i > 0; i--) {
			if (!overTheName && !Character.isWhitespace(codeBlock.charAt(i))) {
				overTheName = true;
				continue;
			}

			if (overTheName == true
					&& Character.isWhitespace(codeBlock.charAt(i))) {
				function_start = i;
				break;
			}
		}
		*/
		return codeBlock.substring(function_start, function_end).replace("\n", "")
		.replace("@Override", "")
		.replace("@SuppressWarnings", "");
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
