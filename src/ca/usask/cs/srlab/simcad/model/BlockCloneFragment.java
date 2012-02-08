package ca.usask.cs.srlab.simcad.model;

import javax.persistence.Entity;

@Entity
public class BlockCloneFragment extends CloneFragment {

	public BlockCloneFragment(String fileName, Integer fromLine,
			Integer toLine, Integer programComponentId, String codeBlock, /*Long id,
			*/ String transformedCodeBlock, long hash1, long hash2) {
		super(fileName, fromLine, toLine, programComponentId, codeBlock, /*id, */
				transformedCodeBlock, hash1, hash2);
	}

}
