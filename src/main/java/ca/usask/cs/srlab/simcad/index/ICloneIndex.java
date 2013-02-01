package ca.usask.cs.srlab.simcad.index;

import java.util.Collection;
import java.util.Set;

import ca.usask.cs.srlab.simcad.index.io.IndexIOConfiguration;
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
	
	//Collection<CloneFragment> getFragmentsByResourceId(String fileName);
	
	Collection<CloneFragment> getFragmentsByResourceId(String fileName, int fromLine, int toLine);

	ICloneIndex removeAnythingInjectedIn(Collection<String> resourceIds);

	ICloneIndex resetDetectionFlagsAndRemoveAnythingInjected();
	
	//long getResourceLastModificationTimeStamp(String resourceId);

	//boolean isResourceModificationTimestampStorageEnable();
	
	void exportIndex(IndexIOConfiguration indexIOConfiguration);
	
	ICloneIndex importIndex(IndexIOConfiguration indexIOConfiguration);

	String getSourceProject();
	
	String getSourceLanguage();
	
	String getFragmentType();

	String getTransformationType();

	void setTransformationType(String transformationType);

	void setSourceProject(String sourceProject);

	void setSourceLanguage(String sourceLanguage);

	void setFragmentType(String fragmentType);
}
