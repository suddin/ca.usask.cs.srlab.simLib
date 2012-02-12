
package ca.usask.cs.srlab.simcad.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class MemoryCloneIndexByJavaCollection implements ICloneIndex {

	private final Map<Integer, HashMap<Byte, ArrayList<CloneFragment>>> lineBitIndex;
	private final Map<String, ArrayList<CloneFragment>> filenameIndex;
	private final Set<IndexKey> keySet; 
	
	public MemoryCloneIndexByJavaCollection() {
		lineBitIndex = new HashMap<Integer, HashMap<Byte, ArrayList<CloneFragment>>>();
		filenameIndex = new HashMap<String, ArrayList<CloneFragment>>();
		keySet = new HashSet<IndexKey>();
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
