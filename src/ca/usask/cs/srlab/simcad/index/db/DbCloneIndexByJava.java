package ca.usask.cs.srlab.simcad.index.db;

import java.util.Collection;
import java.util.Set;

import org.hibernate.cfg.Configuration;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dbobject.BitIndexObject;
import ca.usask.cs.srlab.simcad.dbobject.DbConfigurationBuilder;
import ca.usask.cs.srlab.simcad.dbobject.FileIndexObject;
import ca.usask.cs.srlab.simcad.dbobject.LineIndexObject;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.memory.IndexKey;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public class DbCloneIndexByJava extends AbstractDbCloneIndex {

	public DbCloneIndexByJava() {
		Configuration configuration = DbConfigurationBuilder.buildConfigurationForDbIndex();
		configure(configuration);
	}

	@Override
	public Collection<CloneFragment> getAllByResourceId(String resourceId) {
		FileIndexObject fileIndexObject = (FileIndexObject) getObject(session, FileIndexObject.class, FileIndexObject.fileNameToFileIndexId(resourceId));
		if(fileIndexObject != null)
			return fileIndexObject.getCloneFragmentList();
		return null;
	}

	@Override
	public Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CloneFragment> getEntriesByIndex(IndexKey key) {
		return getEntriesByIndex(key.getLineKey().intValue(), key.getBitKey().intValue());
	}

	@Override
	public Collection<CloneFragment> getAllEntries() {
		return getAllObject(session, CloneFragment.class);
	}
	
	@Override
	public void insert(CloneFragment cloneFragment) {

		FileIndexObject fileIndexObject = getFileIndexObjectByFileName(session, cloneFragment.getFileName());
		if(fileIndexObject == null){
			fileIndexObject = new FileIndexObject(cloneFragment.getFileName());
		}
		fileIndexObject.addCloneFragment(cloneFragment);
		saveObject(session, fileIndexObject);
		
		LineIndexObject lineIndexObject = getLineIndexObjectByLineOfCode(session, cloneFragment.getLineOfCode());
		if(lineIndexObject != null){
			BitIndexObject bitIndexObject = lineIndexObject.getBitIndexObject(cloneFragment.getOneBitCount());
			if(bitIndexObject != null){
				bitIndexObject.addCloneFragment(cloneFragment);
				saveObject(session, bitIndexObject);
			}else{
				bitIndexObject = new BitIndexObject(cloneFragment.getOneBitCount());
				bitIndexObject.addCloneFragment(cloneFragment);
				lineIndexObject.addBitIndex(bitIndexObject);
				saveObject(session, lineIndexObject);
			}
		}else{
			lineIndexObject = new LineIndexObject(cloneFragment.getLineOfCode());
			BitIndexObject bitIndexObject = new BitIndexObject(cloneFragment.getOneBitCount());
			bitIndexObject.addCloneFragment(cloneFragment);
			lineIndexObject.addBitIndex(bitIndexObject);
			saveObject(session, lineIndexObject);
		}
//		
//		IndexKey key = new IndexKey(cloneFragment.getLineOfCode(), cloneFragment.getOneBitCount());
//		keySet.add(key);
	}

	@Override
	public Set<IndexKey> getAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICloneIndex resetDetectionFlags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<String> getAllUniqueResourceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsResourceId(String resourceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized boolean remove(CloneFragment cloneFragment) {
		boolean r2 = removeFromLineBitIndex(cloneFragment);
		boolean r1 = removeFromFileIndex(cloneFragment);
		
		if(r1 != r2)
			throw new SimcadException("Error in removing resource");
		
		return r1;
	}

	private boolean removeFromFileIndex(CloneFragment cloneFragment) {
		FileIndexObject fileIndexObject = getFileIndexObjectByFileName(session, cloneFragment.getFileName());
		if(fileIndexObject != null){
			boolean success = fileIndexObject.removeCloneFragment(cloneFragment);
			if(success)
				saveObject(session, fileIndexObject);
			return success;
		}
		return false;
	}
	
	//@Override
	private Collection<CloneFragment> getBitIndexObject(Integer line, Integer bit) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean removeFromLineBitIndex(CloneFragment cloneFragment) {
		LineIndexObject lineIndexObject = getLineIndexObjectByLineOfCode(session, cloneFragment.getLineOfCode());
		if(lineIndexObject != null){ 
			BitIndexObject bitIndexObject = lineIndexObject.getBitIndexObject(cloneFragment.getOneBitCount());
			if(bitIndexObject != null){
				boolean success = bitIndexObject.removeCloneFragment(cloneFragment);
				if(success)
					saveObject(session, bitIndexObject);
				return success;
			}
		}
		return false;
	}

	@Override
	public Collection<CloneFragment> removeAllByResourceId(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICloneIndex removeAnythingInjectedIn(Collection<String> resourceIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICloneIndex resetDetectionFlagsAndRemoveAnythingInjected() {
		// TODO Auto-generated method stub
		return null;
	}
}