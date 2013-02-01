package ca.usask.cs.srlab.simcad.index.io;

import ca.usask.cs.srlab.simcad.index.ICloneIndex;

public interface IndexIOHandler {

	void doExport(ICloneIndex cloneIndex);

	void doImport(ICloneIndex cloneIndex);

}