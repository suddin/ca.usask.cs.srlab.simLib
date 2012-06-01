package ca.usask.cs.srlab.simcad.index.memory;

import java.util.Collection;
import java.util.Set;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

import com.google.common.collect.HashMultimap;

public class MemoryCloneIndexByGoogleCollection extends AbstractInMemoryCloneIndex {

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

	public Collection<CloneFragment> getAllByResourceId(String fileName) {
		Set<CloneFragment> result = filenameIndex.get(fileName);
		return (result == null || result.isEmpty()) ? null : result;
	}

	public synchronized void insert(CloneFragment cloneFragment) {
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


	@Override
	public void cleanup() {
		if(lineBitIndex!=null)
			lineBitIndex.clear();
		if(filenameIndex!=null)
			filenameIndex.clear();
	}

	@Override
	public synchronized boolean remove(CloneFragment cloneFragment) {
		return removeFromFileIndex(cloneFragment)
				&& removeFromLineBitIndex(cloneFragment);
	}

	private boolean removeFromFileIndex(CloneFragment cloneFragment) {
		Set<CloneFragment> fragmentListFromFileIndex = filenameIndex
				.get(cloneFragment.getFileName());
		if (fragmentListFromFileIndex == null)
			return false;
		if (fragmentListFromFileIndex.contains(cloneFragment)){
			if(fragmentListFromFileIndex.size() == 1)
				return (filenameIndex.remove(cloneFragment.getFileName(), cloneFragment));
			else
				return fragmentListFromFileIndex.remove(cloneFragment);
		}
		return false;
	}

	private boolean removeFromLineBitIndex(CloneFragment cloneFragment) {
		Set<CloneFragment> itemList = lineBitIndex
				.get(new IndexKey(cloneFragment.getLineOfCode(), cloneFragment.getOneBitCount()));
		if (itemList != null) {
			return itemList.remove(cloneFragment);
		}
		return false;
	}

	@Override
	public synchronized Collection<CloneFragment> removeAllByResourceId(String fileName) {
		Set<CloneFragment> removedFragmentList = filenameIndex.removeAll(fileName);
		for (CloneFragment cloneFragment : removedFragmentList) {
			removeFromLineBitIndex(cloneFragment);
		}
		return removedFragmentList;
	}
}
