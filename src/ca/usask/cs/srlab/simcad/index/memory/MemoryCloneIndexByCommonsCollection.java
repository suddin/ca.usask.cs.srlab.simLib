
package ca.usask.cs.srlab.simcad.index.memory;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiValueMap;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class MemoryCloneIndexByCommonsCollection extends AbstractInMemoryCloneIndex {

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

	public Collection<CloneFragment> getAllByResourceId(String fileName) {
		return filenameIndex.getCollection(fileName);
	}
	
	@Override
	public synchronized boolean remove(CloneFragment cloneFragment) {
		return removeFromFileIndex(cloneFragment)
				&& removeFromLineBitIndex(cloneFragment);
	}

	private boolean removeFromFileIndex(CloneFragment cloneFragment) {
		Collection fragmentListFromFileIndex = filenameIndex
				.getCollection(cloneFragment.getFileName());
		if (fragmentListFromFileIndex == null)
			return false;
		if (fragmentListFromFileIndex.contains(cloneFragment)){
			if(fragmentListFromFileIndex.size() == 1)
				return (filenameIndex.remove(cloneFragment.getFileName()) != null);
			else
				return fragmentListFromFileIndex.remove(cloneFragment);
		}
		return false;
	}

	private boolean removeFromLineBitIndex(CloneFragment cloneFragment) {
		Collection<CloneFragment> itemList = lineBitIndex
		.getCollection(new MultiKey(cloneFragment.getLineOfCode(), cloneFragment.getOneBitCount()));
		if (itemList != null) {
			return itemList.remove(cloneFragment);
		}
		return false;
	}

	@Override
	public synchronized Collection<CloneFragment> removeAllByResourceId(String fileName) {
		Collection<CloneFragment> removedFragmentList = filenameIndex.getCollection(fileName);
		filenameIndex.remove(fileName);
		for (CloneFragment cloneFragment : removedFragmentList) {
			//removeFromFileIndex(cloneFragment);
			removeFromLineBitIndex(cloneFragment);
		}
		return removedFragmentList;
	}

	public synchronized void insert(CloneFragment cloneFragment) {
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

	@Override
	public void cleanup() {
		if(lineBitIndex!=null)
			lineBitIndex.clear();
		if(filenameIndex!=null)
			filenameIndex.clear();
	}
}
