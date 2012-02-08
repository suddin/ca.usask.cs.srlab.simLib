
package ca.usask.cs.srlab.simcad.index;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiValueMap;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class MemoryCloneIndexByCommonsCollection implements ICloneIndex {

	private final MultiValueMap filenameIndex;

	private final MultiValueMap lineBitIndex;
	
	public MemoryCloneIndexByCommonsCollection() {
		filenameIndex = new MultiValueMap();
		lineBitIndex = new MultiValueMap();
	}

	public Collection<String> getAllUniqueResourceId() {
		return filenameIndex.keySet();
	}

	public boolean containsResourceId(String resourceId) {
		return filenameIndex.containsKey(resourceId);
	}

	public Collection<CloneFragment> getByResourceId(String fileName) {
		return filenameIndex.getCollection(fileName);
	}

	public void insert(CloneFragment cloneFragment) {
		filenameIndex.put(cloneFragment.getFileName(), cloneFragment);
		MultiKey key = new MultiKey(cloneFragment.getLineOfCode(),Long.bitCount(cloneFragment.getSimhash1()));
		lineBitIndex.put(key, cloneFragment);
	}

	@Override
	public Collection<CloneFragment> getEntriesByIndex(IndexKey key) {
		return lineBitIndex.getCollection(new MultiKey(key.getKeys()));
	}
	
	@Override
	public Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit) {
		return lineBitIndex.getCollection(new MultiKey(line, bit));
	}

	@Override
	public Collection<CloneFragment> getAllEntries() {
		return filenameIndex.values();
	}

	@Override
	public Set<IndexKey> getAllKeys() {
		return lineBitIndex.keySet();
	}

}
