package ca.usask.cs.srlab.simcad.index.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.usask.cs.srlab.simcad.index.AbstractCloneIndex;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.model.BlockCloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

public abstract class AbstractInMemoryCloneIndex extends AbstractCloneIndex {

	protected AbstractInMemoryCloneIndex(String sourceProject, String sourceLanguage, String fragmentType, String transformationType) {
		super(sourceProject, sourceLanguage, fragmentType, transformationType);
	}

//	protected Map<String, Long> resourceModificationTimestampMap;


	
//	protected AbstractInMemoryCloneIndex(boolean enableResourceModificationTimestampStorage){
//		if(enableResourceModificationTimestampStorage)
//			resourceModificationTimestampMap = new HashMap<String, Long>();
//	}
	
	public AbstractInMemoryCloneIndex() {
		super();
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
		
		resetDetectionFlags();
		
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

	
	@Override
	public Collection<CloneFragment> getFragmentsByResourceId(String fileName, int fromLine, int toLine){
		List<CloneFragment> result = new ArrayList<CloneFragment>();
		
		for(CloneFragment cloneFragment : getAllByResourceId(fileName)){
			if(fromLine > cloneFragment.getToLine() || toLine < cloneFragment.getFromLine())
				continue;
			else
				addToCollection(result, cloneFragment, fromLine, toLine);
		}
		
		return result;
	}
	
	protected void addToCollection(List<CloneFragment> result, CloneFragment newFragment, int fromLine, int toLine) {
		if(newFragment instanceof BlockCloneFragment){ //apply subsume filtering
			Iterator<CloneFragment> resultIterator = result.iterator();
			while(resultIterator.hasNext()){
				CloneFragment oldFragment = resultIterator.next();
				if(newFragment.isSubsumedBy(oldFragment)){ //new one is subsumed by an old one
					if(fromLine >= newFragment.getFromLine() && toLine <= newFragment.getToLine())
						//selection subsumed by new fragment
						resultIterator.remove();
					break;
				}
				if(oldFragment.isSubsumedBy(newFragment)){ //new one subsumed an old one
					if(fromLine >= oldFragment.getFromLine() && toLine <= oldFragment.getFromLine())
						//selection subsumed by old fragment
						return;
					break;
				}
			}
			result.add(newFragment);
		} else 
			result.add(newFragment);
		
	}
}
