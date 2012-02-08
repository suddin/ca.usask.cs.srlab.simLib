package ca.usask.cs.srlab.simcad.index;

import java.util.Collection;
import java.util.Set;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import com.google.common.collect.HashMultimap;

public class MemoryCloneIndexByGoogleCollection implements ICloneIndex {

	private final HashMultimap<String, CloneFragment> filenameIndex;

	private final HashMultimap<IndexKey, CloneFragment> lineBitIndex;

	public MemoryCloneIndexByGoogleCollection() {
		filenameIndex = HashMultimap.create();
		lineBitIndex = HashMultimap.create();
	}

	public Set<IndexKey> getAllKeys(){
		return lineBitIndex.keySet();
	}
	
	public Collection<String> getAllUniqueResourceId() {
		return filenameIndex.keySet();
	}

	public boolean containsResourceId(String resourceId) {
		return filenameIndex.containsKey(resourceId);
	}

	public Collection<CloneFragment> getByResourceId(String fileName) {
		return filenameIndex.get(fileName);
	}

	public void insert(CloneFragment cloneFragment) {
		filenameIndex.put(cloneFragment.getFileName(), cloneFragment);
		IndexKey key = new IndexKey(cloneFragment.getLineOfCode(),
				Long.bitCount(cloneFragment.getSimhash1()));
		lineBitIndex.put(key, cloneFragment);
	}

	@Override
	public Collection<CloneFragment> getEntriesByIndex(IndexKey key) {
		return lineBitIndex.get(key);
	}
	
	@Override
	public Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit) {
		return lineBitIndex.get(new IndexKey(line, bit));
	}

	@Override
	public Collection<CloneFragment> getAllEntries() {
		return filenameIndex.values();
	}

}
