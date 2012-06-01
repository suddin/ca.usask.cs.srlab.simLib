package ca.usask.cs.srlab.simcad.index.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public abstract class AbstractInMemoryCloneIndex implements ICloneIndex {

//	protected Map<String, Long> resourceModificationTimestampMap;
	protected boolean isDirty;

	protected AbstractInMemoryCloneIndex(){
		
	}
	
//	protected AbstractInMemoryCloneIndex(boolean enableResourceModificationTimestampStorage){
//		if(enableResourceModificationTimestampStorage)
//			resourceModificationTimestampMap = new HashMap<String, Long>();
//	}
	
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	@Override
	public ICloneIndex resetDetectionFlags() {
		for (CloneFragment cf : getAllEntries()) {
			cf.resetDetectionFlags();
		}
		isDirty=false;
		return this;
	}
	
	@Override
	public synchronized ICloneIndex resetDetectionFlagsAndRemoveAnythingInjected() {
		List<CloneFragment> removeCandidate = new ArrayList<CloneFragment>();
		for (CloneFragment cf : getAllEntries()) {
			cf.resetDetectionFlags();
			if(cf.isInjected){
				removeCandidate.add(cf);
			}
		}
		for (CloneFragment icf : removeCandidate) {
			remove(icf);
		}
		isDirty=false;
		return this;
	}

	@Override
	public synchronized ICloneIndex removeAnythingInjectedIn(Collection<String> resourceIds) {
		List<CloneFragment> removeCandidate = new ArrayList<CloneFragment>();
		
		for (String resourceId : resourceIds) {
			Collection<CloneFragment> allByResourceId = getAllByResourceId(resourceId);
			if(allByResourceId != null){
				for (CloneFragment cf : allByResourceId) {
					if(cf.isInjected){
	//					System.out.println("resourceId injected ? : "+cf.isInjected);
						removeCandidate.add(cf);
					}
				}
			}
		}
		
		for (CloneFragment icf : removeCandidate) {
			remove(icf);
		}
		
		return this;
	}
	
	/*@Override
	public boolean isResourceModificationTimestampStorageEnable(){
		return resourceModificationTimestampMap != null;
	}
	
	@Override
	public long getResourceLastModificationTimeStamp(String resourceId){
		if(!isResourceModificationTimestampStorageEnable())
			throw new SimcadException("Resource modification timestamp storage is not enabled");
		return resourceModificationTimestampMap.get(resourceId);
	}*/
}
