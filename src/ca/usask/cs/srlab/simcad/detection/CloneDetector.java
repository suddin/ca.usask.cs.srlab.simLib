package ca.usask.cs.srlab.simcad.detection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.event.CloneFoundEvent;
import ca.usask.cs.srlab.simcad.event.DetectionEndEvent;
import ca.usask.cs.srlab.simcad.event.DetectionProgressEvent;
import ca.usask.cs.srlab.simcad.event.DetectionStartEvent;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.IndexKey;
import ca.usask.cs.srlab.simcad.listener.ICloneDetectionListener;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneGroup;
import ca.usask.cs.srlab.simcad.model.ClonePair;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;
import ca.usask.cs.srlab.simcad.util.FastStringComparator;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public final class CloneDetector {
	
	private static final Integer MIN_CLUSTER_SIZE = PropsUtil.getMinClusterSize();
	private static final boolean STRICT_ON_MEMBERSHIP = PropsUtil.isStrictOnMembership();
	private static final Double CLUSTER_MEMBERSHIP_RATIO = PropsUtil.getClusterMembershipRatio();
	private static final Double LOC_TOLERANCE = PropsUtil.getLocTolerance();
	
	private ICloneIndex cloneIndex;
	private DetectionSettings detectionSettings;
	private ICloneDetectionListener detectionListener;
	
	public List<CloneSet> detect(Collection<CloneFragment> candidateFragments) {
		if(candidateFragments == null || candidateFragments.size() == 0)
			throw new SimcadException("The detection request is empty");
		return this.detectClones(candidateFragments);
	}

	public static CloneDetector getInstance(ICloneIndex cloneIndex, DetectionSettings detectionSettings){
		return new CloneDetector(cloneIndex, detectionSettings);	
	}
	
	public static CloneDetector getInstance(ICloneIndex cloneIndex, DetectionSettings detectionSettings, ICloneDetectionListener detectionListener){
		return new CloneDetector(cloneIndex, detectionSettings, detectionListener);	
	}

	private CloneDetector(){};
	
	private CloneDetector(ICloneIndex cloneIndex, DetectionSettings settings) {
		this(cloneIndex, settings, null);
	}
	
	private CloneDetector(ICloneIndex cloneIndex, DetectionSettings detectionSettings, ICloneDetectionListener detectionListener) {
		this.cloneIndex = cloneIndex;
		this.detectionSettings = detectionSettings;
		this.detectionListener = detectionListener;
	}
	
	public CloneDetector attachDetectionListener(ICloneDetectionListener detectionListener){
		this.detectionListener = detectionListener;
		return this;
	}
	
	private List<CloneSet> detectClones(Collection<CloneFragment> candidateFragments) {
		List<CloneSet> detectedCloneSets = new LinkedList<CloneSet>();
		
		int nCodeFragment = candidateFragments.size();
		int nCloneFragment = 0;
		int currentProgressIndex = 1;
		int cloneSetIndex = 1;
		
		fireDetectionStartEvent(nCodeFragment);
		
		long startTime = System.currentTimeMillis();
		
		for(CloneFragment cloneFragment : candidateFragments){
			
			fireDetectionProgressEvent(currentProgressIndex++, nCodeFragment);
			
			if(cloneFragment.isProceessed) continue;
			
			if(Constants.CLONE_SET_TYPE_GROUP.equals(detectionSettings.getCloneSetType())){
				List<ICloneFragment> newCluster = findNeighborsForGroup(cloneFragment);
				
				if (newCluster.size() >= MIN_CLUSTER_SIZE){
					CloneGroup newCloneGroup = new CloneGroup(newCluster, null, cloneSetIndex++);
					if(filterByTypeInDetectionSettings(detectedCloneSets, newCloneGroup)){
						fireCloneFoundEvent(newCloneGroup);
						nCloneFragment += newCloneGroup.size();
					}
				}
			}else{
				List<CloneFragment> clonePairElements = findNeighborsForPair(cloneFragment);
				for(CloneFragment matchedFragment : clonePairElements){
					ClonePair newClonePair = new ClonePair(cloneFragment, matchedFragment, null, cloneSetIndex++);
					if(filterByTypeInDetectionSettings(detectedCloneSets, newClonePair)){
						fireCloneFoundEvent(newClonePair);
						nCloneFragment += newClonePair.size();
					}
				}
			}
		}//for
		
		long endTime = System.currentTimeMillis();
		long diff  = endTime - startTime;
		detectionSettings.getDetectionReport().setDetectionTime(diff);
		detectionSettings.getDetectionReport().setnCloneFragment(nCloneFragment);
		detectionSettings.getDetectionReport().setnCloneSet(cloneSetIndex-1);
		
		fireDetectionEndEvent();
		return detectedCloneSets;
	}
	
	/**
	 * 
	 * @param detectedCloneSets
	 * @param newCloneSet
	 * @return : false/true if newCloneSet filtered out/in
	 */
	private boolean filterByTypeInDetectionSettings(List<CloneSet> detectedCloneSets, CloneSet newCloneSet){
		if(detectionSettings.containsType(newCloneSet.getCloneType())){
			detectedCloneSets.add(newCloneSet);
			return true;
		}
		return false;
	}
	
	private void fireCloneFoundEvent(CloneSet newCloneSet) {
		if(detectionSettings.isVerbose())
			System.out.println("Found new clone "+detectionSettings.getCloneSetType()+". Current Total : " + newCloneSet.getCloneSetId());
		if (detectionListener != null) {
			detectionListener.foundClone(new CloneFoundEvent(this, newCloneSet));
		}		
	}
	
	private void fireDetectionProgressEvent(int currentIndex, int of) {
		if(detectionSettings.isVerbose())
			System.out.println("Processing item "+currentIndex+" of "+of);
		if (detectionListener != null) {
			detectionListener.progressDetection(new DetectionProgressEvent(this, currentIndex));
		}		
	}

	private void fireDetectionEndEvent() {
		//if(detectionSettings.isVerbose())
			System.out.println("Detection process completed...\n");
		if (detectionListener != null) {
			detectionListener.endDetection(new DetectionEndEvent(this));
		}	
	}

	private void fireDetectionStartEvent(int nCodeFragment) {
		//if(detectionSettings.isVerbose())
			System.out.println("Starting detection process...\n");
		if (detectionListener != null) {
			detectionListener.startDetection(new DetectionStartEvent(this, nCodeFragment));
		}
	}

	private <T extends CloneFragment> List<ICloneFragment> findNeighborsForGroup(T item){
		List<ICloneFragment> neighbors = new ArrayList<ICloneFragment>();
		Set<Long> capturedHash = new HashSet<Long>();
		
		int deviation = 0; 
		
		int simThreshold1 = 0;
		
		if(detectionSettings.containsType(CloneSet.CLONE_TYPE_3)){
			simThreshold1 = Environment.getType3cloneSimthreshold();
		}
		
		int simThreshold2;
		int dynamicSimThreshold1;// = simThreshold + deviation;
		int dynamicSimThreshold2;// = simThreshold2 + deviation;
		
		item.isTempFriend = false;
		neighbors.add(item);
		
		item.isProceessed=true;
		
		int length = neighbors.size();
		for(int i = 0; i < length; i++) {
			ICloneFragment searchItem = neighbors.get(i);
			//an additional check to save more computation
			if(capturedHash.contains(searchItem.getSimhash1())) {
				continue;// its result already picked up by someone else earlier, so just ignore
			}
			
			//dynamic threshold update
			if(simThreshold1 != 0){
				
				simThreshold2 = simThreshold1;
				
				switch(simThreshold1){
				
				case 6:
					simThreshold2 = 5;
					break;
				
				case 7:
					if(searchItem.getLineOfCode() < 6){
						deviation = -1;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -1;
					}
					simThreshold2 = 6;
					break;
				
				case 8:
					if(searchItem.getLineOfCode() < 6){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -1;
					}
					simThreshold2 = 7;
					break;
				
				case 9:
					if(searchItem.getLineOfCode() < 6){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -1;
					}
					simThreshold2 = 8;
					break;
				
				case 10:
					if(searchItem.getLineOfCode() < 6){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -1;
					}
					simThreshold2 = 8;
					break;
				
				case 11:
					if(searchItem.getLineOfCode() < 6){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -1;
					}
					simThreshold2 = 9;
					break;
				
				case 12:
					if(searchItem.getLineOfCode() < 6){
						deviation = -5;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 30){
						deviation = -1;
					}	
					simThreshold2 = 12;
					break;
				case 13:
					if(searchItem.getLineOfCode() < 6){
						deviation = -5;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 30){
						deviation = -1;
					}	
					simThreshold2 = 13;
					break;
				}
				
				/*else if(item.lineOfCode > 40){
					deviation = 2;
				}else if(item.lineOfCode > 30){
					deviation = 1;
				}*/
				
				dynamicSimThreshold1 = simThreshold1 + deviation;
				dynamicSimThreshold2 = simThreshold2 + deviation;
				
				Set<IndexKey> keySet = cloneIndex.getAllKeys();
				for (IndexKey indexKey : keySet) {
					if((searchItem.getLineOfCode() - (searchItem.getLineOfCode() * LOC_TOLERANCE) < indexKey.getLineKey().intValue() && searchItem.getLineOfCode() + (searchItem.getLineOfCode() * LOC_TOLERANCE) > indexKey.getLineKey().intValue())
							&& (searchItem.getOneBitCount() - dynamicSimThreshold1 <= indexKey.getBitKey().intValue() && searchItem.getOneBitCount() + dynamicSimThreshold1 >= indexKey.getBitKey().intValue())){
					
						for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(indexKey)){
							
							if(!matchCandidate.isProceessed && ((hamming_dist(searchItem.getSimhash1(), matchCandidate.getSimhash1()) <= dynamicSimThreshold1
									&& hamming_dist(searchItem.getSimhash2(), matchCandidate.getSimhash2()) <= dynamicSimThreshold2))){
								
								//check if at least clusterMembershipRatio times the existing members in the cluster are cool with this guy
								int minFriendCount = (int) (neighbors.size() * CLUSTER_MEMBERSHIP_RATIO);
								minFriendCount = minFriendCount < 1 ? 1: minFriendCount;
								
								boolean coolDude = false;
								//for(ICloneFragment eMember : neighbors){
								for(Iterator<ICloneFragment> it  = (Iterator<ICloneFragment>) neighbors.iterator(); it.hasNext();){	
									CloneFragment eMember = (CloneFragment) it.next();
									//set initial friendship to false
									eMember.isTempFriend = false;
									
									if(hamming_dist(matchCandidate.getSimhash1(), eMember.getSimhash1()) <= dynamicSimThreshold1
											&& hamming_dist(matchCandidate.getSimhash2(), eMember.getSimhash2()) <= dynamicSimThreshold2){
										
										matchCandidate.friendCount ++;
										eMember.isTempFriend = true;
										
										if(matchCandidate.friendCount == minFriendCount){ //target reached, he got the ticket to join in friends club
											coolDude = true;
											if(!STRICT_ON_MEMBERSHIP) break;
										}
									}
								} //done with friendship checking
														
								if(coolDude){ //now add him to friends club
									
									matchCandidate.isProceessed=true;
									neighbors.add((T) matchCandidate);
									length++;
									
									//move temp friends count to real count
									for(Iterator<ICloneFragment> it  = (Iterator<ICloneFragment>) neighbors.iterator(); it.hasNext();){
										CloneFragment eMember = (CloneFragment) it.next();
										if(eMember.isTempFriend) {
											//eMember.friendlist.add(matchCandidate);
											eMember.friendCount ++;
										}
										eMember.isTempFriend=false;
									}
									
								}//new member add done							
								
							}
						}
					}
				}
						
				//cleanup noise from the friends club based on new friendship
				if(neighbors.size() > 1 && STRICT_ON_MEMBERSHIP){

					//List<SourceItem> removedMember = new ArrayList<SourceItem>();
					
					//do{
					
						int minFriendCount = (int) (neighbors.size() * CLUSTER_MEMBERSHIP_RATIO);
						minFriendCount = minFriendCount < 1 ? 1: minFriendCount;

						//removedMember.clear();
						
						for(Iterator<ICloneFragment> it  = (Iterator<ICloneFragment>) neighbors.iterator(); it.hasNext();){
							CloneFragment eMember = (CloneFragment) it.next();
							
							if(eMember.friendCount/*eMember.friendlist.size()*/ < minFriendCount){ 
								//get him outta here!
								eMember.isProceessed = false;
								
								eMember.friendCount = 0;
								//eMember.friendlist.clear();
								eMember.isTempFriend = false;
								//dismissedItemList.add(eMember);
								it.remove();
								length--;
								
								//removedMember.add(eMember);
								//System.out.println("Member removed from club!");
							}
						}
					
						/*
						 * 
						if(removedMember.size() > 0) { 
							System.out.println("gotcha black sheep!");
						}
							
						//remove this member from other member's friendslist
						for(Iterator<SourceItem> it2  = cluster.iterator(); it2.hasNext();){
							SourceItem eMember = it2.next();
							eMember.friendlist.removeAll(removedMember);
						}
					
					}while(removedMember.size() > 0);*/
				}
				
				
			}else{ 
				for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(searchItem.getLineOfCode(), searchItem.getOneBitCount())){
					if(!matchCandidate.isProceessed && searchItem.getSimhash1().equals(matchCandidate.getSimhash1())
							&& searchItem.getSimhash2().equals(matchCandidate.getSimhash2())){
						
						if(detectionSettings.containsType(CloneSet.CLONE_TYPE_2)){
							neighbors.add((T) matchCandidate);
							length++;
						}else{ //only type-1 is searched for
							if(FastStringComparator.INSTANCE.compare(searchItem.getOriginalCodeBlock(), matchCandidate.getOriginalCodeBlock()) == 0){
								neighbors.add((T) matchCandidate);
								length++;
							}
						}
						
						matchCandidate.isProceessed=true;
					}
				}
				
			}
			//finished neighbor search of current item and now record it
			capturedHash.add(searchItem.getSimhash1());
		}
		
		return neighbors;
	}

	private <T extends CloneFragment> List<T> findNeighborsForPair(T searchItem){
		List<T> neighbors = new ArrayList<T>();
		
		int deviation = 0; 
		
		int simThreshold1 = 0;
		
		if(detectionSettings.containsType(CloneSet.CLONE_TYPE_3)){
			simThreshold1 = Environment.getType3cloneSimthreshold();
		}
		
		int simThreshold2;
		int dynamicSimThreshold1;// = simThreshold + deviation;
		int dynamicSimThreshold2;// = simThreshold2 + deviation;
		
		searchItem.isTempFriend = false;
		searchItem.isProceessed=true;

		//dynamic threshold update
			if(simThreshold1 != 0){
				
				simThreshold2 = simThreshold1;
				
				switch(simThreshold1){
				
				case 6:
					simThreshold2 = 5;
					break;
				
				case 7:
					if(searchItem.getLineOfCode() < 6){
						deviation = -1;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -1;
					}
					simThreshold2 = 6;
					break;
				
				case 8:
					if(searchItem.getLineOfCode() < 6){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -1;
					}
					simThreshold2 = 7;
					break;
				
				case 9:
					if(searchItem.getLineOfCode() < 6){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -1;
					}
					simThreshold2 = 8;
					break;
				
				case 10:
					if(searchItem.getLineOfCode() < 6){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -1;
					}
					simThreshold2 = 8;
					break;
				
				case 11:
					if(searchItem.getLineOfCode() < 6){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -1;
					}
					simThreshold2 = 9;
					break;
				
				case 12:
					if(searchItem.getLineOfCode() < 6){
						deviation = -5;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 30){
						deviation = -1;
					}	
					simThreshold2 = 12;
					break;
				case 13:
					if(searchItem.getLineOfCode() < 6){
						deviation = -5;
					}else if(searchItem.getLineOfCode() < 8){
						deviation = -4;
					}else if(searchItem.getLineOfCode() < 10){
						deviation = -3;
					}else if(searchItem.getLineOfCode() < 20){
						deviation = -2;
					}else if(searchItem.getLineOfCode() < 30){
						deviation = -1;
					}	
					simThreshold2 = 13;
					break;
				}
				
				/*else if(item.lineOfCode > 40){
					deviation = 2;
				}else if(item.lineOfCode > 30){
					deviation = 1;
				}*/
				
				dynamicSimThreshold1 = simThreshold1 + deviation;
				dynamicSimThreshold2 = simThreshold2 + deviation;
				
				Set<IndexKey> keySet = cloneIndex.getAllKeys();
				for (IndexKey indexKey : keySet) {
					if((searchItem.getLineOfCode() - (searchItem.getLineOfCode() * LOC_TOLERANCE) < indexKey.getLineKey().intValue() && searchItem.getLineOfCode() + (searchItem.getLineOfCode() * LOC_TOLERANCE) > indexKey.getLineKey().intValue())
							&& (searchItem.getOneBitCount() - dynamicSimThreshold1 <= indexKey.getBitKey().intValue() && searchItem.getOneBitCount() + dynamicSimThreshold1 >= indexKey.getBitKey().intValue())){
					
						for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(indexKey)){
							
							if(!matchCandidate.isProceessed && ((hamming_dist(searchItem.getSimhash1(), matchCandidate.getSimhash1()) <= dynamicSimThreshold1
									&& hamming_dist(searchItem.getSimhash2(), matchCandidate.getSimhash2()) <= dynamicSimThreshold2))){
								
//								if(STRICT_ON_MEMBERSHIP){
//									//check if at least clusterMembershipRatio times the existing members in the cluster are cool with this guy
//									int minFriendCount = (int) (neighbors.size() * CLUSTER_MEMBERSHIP_RATIO);
//									minFriendCount = minFriendCount < 1 ? 1: minFriendCount;
//									
//									for(CloneFragment eMember:neighbors){
//										
//										if(hamming_dist(matchCandidate.getSimhash1(), eMember.getSimhash1()) <= dynamicSimThreshold1
//												&& hamming_dist(matchCandidate.getSimhash2(), eMember.getSimhash2()) <= dynamicSimThreshold2){
//											
//											matchCandidate.friendCount ++;
//											
//											if(matchCandidate.friendCount == minFriendCount){ //target reached, he got the ticket to join in friends club
//												
//												neighbors.add((T) matchCandidate);
//											}
//										}
//									} //done with friendship checking
//								}else
									neighbors.add((T) matchCandidate);
									//matchCandidate.isProceessed = true;
														
							}
						}
					
					}
				}
				
			}else{ 
				for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(searchItem.getLineOfCode(), searchItem.getOneBitCount())){
					if(!matchCandidate.isProceessed && searchItem.getSimhash1().equals(matchCandidate.getSimhash1())
							&& searchItem.getSimhash2().equals(matchCandidate.getSimhash2())){
						
						if(detectionSettings.containsType(CloneSet.CLONE_TYPE_2))
							neighbors.add((T) matchCandidate);
						else{ //only type-1 is searched for
							if(FastStringComparator.INSTANCE.compare(searchItem.getOriginalCodeBlock(), matchCandidate.getOriginalCodeBlock()) == 0)
								neighbors.add((T) matchCandidate);
						}
						
						//matchCandidate.isProceessed = true;
					}
				}
			}
		
		return neighbors;
	}
	
	private int hamming_dist(Long simhash1, Long simhash2) {
		return Long.bitCount(simhash1 ^ simhash2);
	}
}
