package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;
import ca.usask.cs.srlab.simcad.processor.IProcessor;

import com.google.common.collect.ImmutableList;

public final class SubsumedCloneFilter implements IProcessor {
	
	@Override
	public boolean process(Collection<CloneSet> inputCloneSets, Collection<CloneSet> outputCloneSets) {

		int subsumedCluster = 0;
		int subsumedFragment = 0;
		
		 ImmutableList<CloneSet> cloneSetList = ImmutableList.copyOf(inputCloneSets);
		
		for(int i = 0; i < cloneSetList.size(); i++){
			CloneSet sourceCloneSet = cloneSetList.get(i);
			
			if(!sourceCloneSet.isSubsumed()){
				for(int j = 0; j< cloneSetList.size(); j++){
					if (i==j) continue;
					
					CloneSet targetCloneSet = cloneSetList.get(j);
					
					if(!targetCloneSet.isSubsumed() && 
							sourceCloneSet.size() == targetCloneSet.size()){
						
						//check if every item of targetgroup is subsumed by sourcegroup 
						boolean fullSubsume = true;
						//List<ICloneFragment> subsumedItem = new ArrayList<ICloneFragment>();
						//int subsumeCount = 0;
						for(int t = 0; t <targetCloneSet.size(); t++){
							ICloneFragment itemT = targetCloneSet.getMember(t);
						
							//check itemT is subsumed by any of itemS
							boolean partialSubsume = false;
							for(int s = 0; s < sourceCloneSet.size(); s++){
								ICloneFragment itemS = sourceCloneSet.getMember(s);
								if(itemS.getFileName().equals(itemT.getFileName())
										&&  Integer.valueOf(itemS.getFromLine()) <= Integer.valueOf(itemT.getFromLine())
										&&  Integer.valueOf(itemS.getToLine()) >= Integer.valueOf(itemT.getToLine())){
									partialSubsume = true;
									//subsumedItem.add(itemT);
									//subsumeCount++;
									break;
								}
							}
								
							if(!partialSubsume){
								fullSubsume = false;
								break;
							}
						}
						
						if(fullSubsume){
							targetCloneSet.setSubsumed(true);
							subsumedCluster++;
							subsumedFragment += targetCloneSet.size();  
						}
				
						
					}
				}//target cloneset
			}
		}
		
		System.out.println("Subsumed clusters: "+subsumedCluster);
		System.out.println("Subsumed fragments: "+subsumedFragment);
		
		for(CloneSet cloneSet : cloneSetList){
			if(!cloneSet.isSubsumed())
			outputCloneSets.add(cloneSet);
		}
		
		return true;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
