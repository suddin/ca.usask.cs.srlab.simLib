
package ca.usask.cs.srlab.simcad.index.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class MemoryCloneIndexByJavaCollection extends AbstractInMemoryCloneIndex {

	private final Map<Integer, HashMap<Byte, ArrayList<CloneFragment>>> lineBitIndex;
	private final Map<String, ArrayList<CloneFragment>> filenameIndex;
	private final Set<IndexKey> keySet; 
	
	public MemoryCloneIndexByJavaCollection() {
		this("sourceLocation", "sourceLanguage", "fragmentType", "transformationType");
	}

	public MemoryCloneIndexByJavaCollection(String sourceLocation, String sourceLanguage, String fragmentType, String transformationType) {
		super(sourceLocation, sourceLanguage, fragmentType, transformationType);
		lineBitIndex = new HashMap<Integer, HashMap<Byte, ArrayList<CloneFragment>>>();
		filenameIndex = new HashMap<String, ArrayList<CloneFragment>>();
		keySet = new TreeSet<IndexKey>(new IndexKeyComparator());
	}

	@Override
	public Collection<String> getAllUniqueResourceId() {
		return filenameIndex.keySet() !=null ? new ArrayList<String>(filenameIndex.keySet()) : new ArrayList<String>();
	}

	@Override
	public boolean containsResourceId(String resourceId) {
		return filenameIndex.containsKey(resourceId);
	}

	@Override
	public Collection<CloneFragment> getAllByResourceId(String fileName) {
		ArrayList<CloneFragment> result = filenameIndex.get(fileName);
		return result!=null ? new ArrayList<CloneFragment>(result) : new ArrayList<CloneFragment>();
	}

	@Override
	public synchronized Collection<CloneFragment> removeAllByResourceId(String fileName) {
		ArrayList<CloneFragment> removedFragmentList = filenameIndex.remove(fileName);
		for (CloneFragment cloneFragment : removedFragmentList) {
			removeFromLineBitIndex(cloneFragment);
		}
		return removedFragmentList;
	}
	
	@Override
	public synchronized boolean remove(CloneFragment cloneFragment) {
		boolean r1 = removeFromFileIndex(cloneFragment);
		boolean r2 = removeFromLineBitIndex(cloneFragment);
		
		if(r1 != r2)
			throw new SimcadException("Error in removing resource");
		
		return r1;
	}

	private boolean removeFromFileIndex(CloneFragment cloneFragment) {
		ArrayList<CloneFragment> fragmentListFromFileIndex = filenameIndex
				.get(cloneFragment.getFileName());
		if (fragmentListFromFileIndex == null)
			return false;
		if (fragmentListFromFileIndex.contains(cloneFragment)){
//			if(fragmentListFromFileIndex.size() == 1){
//				return (filenameIndex.remove(cloneFragment.getFileName()) != null);
//			} else
				return fragmentListFromFileIndex.remove(cloneFragment);
		}
		return false;
	}

	private boolean removeFromLineBitIndex(CloneFragment cloneFragment) {
		Map<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = lineBitIndex
				.get(cloneFragment.getLineOfCode());
		if (itemListSecondLevelMap != null) {
			ArrayList<CloneFragment> itemList = itemListSecondLevelMap
					.get(cloneFragment.getOneBitCount().byteValue());
			if (itemList.remove(cloneFragment))
				return true;
		}
		return false;
	}
	
	@Override
	public synchronized void insert(CloneFragment cloneFragment) {
		if(filenameIndex.containsKey(cloneFragment.getFileName())){
			filenameIndex.get(cloneFragment.getFileName()).add(cloneFragment);
		}else{
			ArrayList<CloneFragment> newList = new ArrayList<CloneFragment>();
			newList.add(cloneFragment);
			filenameIndex.put(cloneFragment.getFileName(), newList);
		}
		
		IndexKey key = new IndexKey(cloneFragment.getLineOfCode(), cloneFragment.getOneBitCount());
		keySet.add(key);
		
		//implementation of two level map
		if(lineBitIndex.containsKey(cloneFragment.getLineOfCode())){
			Map<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = lineBitIndex.get(cloneFragment.getLineOfCode());
			if(itemListSecondLevelMap.containsKey(cloneFragment.getOneBitCount().byteValue())){
				itemListSecondLevelMap.get(cloneFragment.getOneBitCount().byteValue()).add(cloneFragment);
			}else{
				ArrayList<CloneFragment> itemList = new ArrayList<CloneFragment>();
				itemList.add(cloneFragment);
				itemListSecondLevelMap.put(cloneFragment.getOneBitCount().byteValue(), itemList);
			}
		}else{
			HashMap<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = new HashMap<Byte, ArrayList<CloneFragment>>();
			lineBitIndex.put(cloneFragment.getLineOfCode(), itemListSecondLevelMap);			
			ArrayList<CloneFragment> itemList = new ArrayList<CloneFragment>();
			itemList.add(cloneFragment);
			itemListSecondLevelMap.put(cloneFragment.getOneBitCount().byteValue(), itemList);
		}
	}

	@Override
	public Collection<CloneFragment> getEntriesByIndex(IndexKey key) {
		return getEntriesByIndex(key.getLineKey().intValue(), key.getBitKey().intValue());
	}
	
	@Override
	public Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit) {
		Map<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = lineBitIndex.get(line);
		if(itemListSecondLevelMap != null)
			return itemListSecondLevelMap.get(bit.byteValue());
		else
			return null;
	}

	@Override
	public Collection<CloneFragment> getAllEntries() {
		List<CloneFragment> values = new ArrayList<CloneFragment>();	
		for (List<CloneFragment> list : filenameIndex.values())
			values.addAll(list);
		return values;
	}

	@Override
	public Set<IndexKey> getAllKeys() {
		return keySet;
	}

	@Override
	public void cleanup() {
		if(lineBitIndex!=null)
			lineBitIndex.clear();
		if(filenameIndex!=null)
			filenameIndex.clear();
		if(keySet!=null)
			keySet.clear();
	}
}
