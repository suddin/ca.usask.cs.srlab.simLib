package ca.usask.cs.srlab.simcad.index;

import java.util.Collection;
import java.util.Set;

import ca.usask.cs.srlab.simcad.index.memory.IndexKey;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public interface ICloneIndex {

	Collection<CloneFragment> getAllByResourceId(String resourceId);

	Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit);

	Collection<CloneFragment> getEntriesByIndex(IndexKey key);

	Collection<CloneFragment> getAllEntries();

	void insert(CloneFragment block);

	Set<IndexKey> getAllKeys();

	ICloneIndex resetDetectionFlags();

	void cleanup();
	
	boolean isDirty();
	
	void setDirty(boolean isDirty);

	Collection<String> getAllUniqueResourceId();

	boolean containsResourceId(String resourceId);

	boolean remove(CloneFragment cloneFragment);

	Collection<CloneFragment> removeAllByResourceId(String fileName);

	ICloneIndex removeAnythingInjectedIn(Collection<String> resourceIds);

	ICloneIndex resetDetectionFlagsAndRemoveAnythingInjected();
	
	//long getResourceLastModificationTimeStamp(String resourceId);

	//boolean isResourceModificationTimestampStorageEnable();
}
