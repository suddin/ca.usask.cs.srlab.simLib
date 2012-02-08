package ca.usask.cs.srlab.simcad.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ca.usask.cs.srlab.simcad.event.CloneFoundEvent;
import ca.usask.cs.srlab.simcad.event.DetectionEndEvent;
import ca.usask.cs.srlab.simcad.event.DetectionProgressEvent;
import ca.usask.cs.srlab.simcad.event.DetectionStartEvent;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;
import ca.usask.cs.srlab.simcad.index.IndexKey;
import ca.usask.cs.srlab.simcad.listener.ICloneDetectionListener;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneGroup;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneFragment;
import ca.usask.cs.srlab.simcad.model.ICloneSet;
import ca.usask.cs.srlab.simcad.model.ICloneType;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public final class CloneDetector {
	
	private ICloneIndex cloneIndex;
	private DetectionSettings detectionSettings;
	private ICloneDetectionListener detectionListener;
	
	public List<CloneSet> detect(Collection<CloneFragment> candidateFragments) {
		return this.detectClones(candidateFragments);
	}

	public static CloneDetector setup(ICloneIndex cloneIndex, DetectionSettings detectionSettings){
		return new CloneDetector(cloneIndex, detectionSettings);	
	}
	
	public static CloneDetector setup(ICloneIndex cloneIndex, DetectionSettings detectionSettings, ICloneDetectionListener detectionListener){
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
		int minClusterSize = PropsUtil.getMinClusterSize();
		
		/*
		//DBSCAN(D, eps, MinPts)
		for(Entry<Integer, HashMap<Byte, ArrayList<SourceItem>>> itemListFirstLevelMapEntry : itemListFirstLevelMap.entrySet()){
			Map<Byte, ArrayList<SourceItem>> itemListSecondLevelMap = itemListFirstLevelMapEntry.getValue();
			for(Entry<Byte, ArrayList<SourceItem>> itemList : itemListSecondLevelMap.entrySet()){
				for(SourceItem item : itemList.getValue()){
					if(item.isProceessed) continue;
					List<SourceItem> newCluster = getNeighbors (item);
					if (newCluster.size() >= minClusterSize){
						detectedCloneSets.add(new CloneGroup(newCluster));
//						nGroup++;
//						nFragment += newCluster.size(); //fragments
						//itemList.removeAll(newCluster);
					}
				}
			}
	  	}
		*/
		
		int currentProgressIndex = 0;
		int cloneSetIndex = 0;
		
		fireDetectionStartEvent();
		
		for(CloneFragment cloneFragment : candidateFragments){
			
			fireDetectionProgressEvent(currentProgressIndex++);
			
			if(cloneFragment.isProceessed) continue;
			
			List<CloneFragment> newCluster = findNeighbors(cloneFragment);
			
			if (newCluster.size() >= minClusterSize){
				CloneGroup newCloneGroup = createNewCloneGroup(newCluster, cloneSetIndex++);
				detectedCloneSets.add(newCloneGroup);
				fireCloneFoundEvent(newCloneGroup);
			}
			
		}
		fireDetectionEndEvent();
		return detectedCloneSets;
	}
	
	
	private CloneGroup createNewCloneGroup(List<CloneFragment> newCluster, Integer cloneSetIndex){
		//TODO: decide if its a clone pair or clone group
		ICloneType cloneType = TypeMapper.determineCloneType(newCluster);
		return new CloneGroup(newCluster, cloneType, cloneSetIndex);
	}
	
	
	private void fireCloneFoundEvent(CloneGroup newCloneGroup) {
		if (detectionListener != null) {
			detectionListener.foundClone(new CloneFoundEvent(this, newCloneGroup));
		}		
	}

	
	private void fireDetectionProgressEvent(int currentIndex) {
		if (detectionListener != null) {
			detectionListener.progressDetection(new DetectionProgressEvent(this, currentIndex));
		}		
	}

	private void fireDetectionEndEvent() {
		if (detectionListener != null) {
			detectionListener.endDetection(new DetectionEndEvent(this));
		}	
	}

	private void fireDetectionStartEvent() {
		if (detectionListener != null) {
			detectionListener.startDetection(new DetectionStartEvent(this));
		}
	}

	private <T extends CloneFragment> List<T> findNeighbors (T item){
		List<T> cluster = new ArrayList<T>();
		Set<Long> capturedHash = new HashSet<Long>();
		
		
		int deviation = 0; 
		
		int simThreshold1 = detectionSettings.getSimThreshold();
		int simThreshold2;
		int dynamicSimThreshold1;// = simThreshold + deviation;
		int dynamicSimThreshold2;// = simThreshold2 + deviation;
		
		item.isTempFriend = false;
		cluster.add(item);
		
		item.isProceessed=true;
		
		int length = cluster.size();
		for(int i=0; i<length; i++) {
			CloneFragment searchItem = cluster.get(i);
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
					if((searchItem.getLineOfCode() - (searchItem.getLineOfCode() * PropsUtil.getLocTolerance()) < indexKey.getLineKey().intValue() && searchItem.getLineOfCode() + (searchItem.getLineOfCode()+PropsUtil.getLocTolerance()) > indexKey.getLineKey().intValue())
							&& (searchItem.getOneBitCount() - dynamicSimThreshold1 <= indexKey.getBitKey().intValue() && searchItem.getOneBitCount() + dynamicSimThreshold1 >= indexKey.getBitKey().intValue())){
					
						for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(indexKey)){
							
							if(!matchCandidate.isProceessed && ((hamming_dist(searchItem.getSimhash1(), matchCandidate.getSimhash2()) <= dynamicSimThreshold1
									&& hamming_dist(searchItem.getSimhash2(), matchCandidate.getSimhash2()) <= dynamicSimThreshold2))){
								
								//check if at least clusterMembershipRatio times the existing members in the cluster are cool with this guy
								int minFriendCount = (int) (cluster.size() * PropsUtil.getClusterMembershipRatio());
								minFriendCount = minFriendCount < 1 ? 1: minFriendCount;
								
								boolean coolDude = false;
								for(CloneFragment eMember:cluster){
									
									//set initial friendship to false
									eMember.isTempFriend = false;
									
									if(hamming_dist(matchCandidate.getSimhash1(), eMember.getSimhash1()) <= dynamicSimThreshold1
											&& hamming_dist(matchCandidate.getSimhash2(), eMember.getSimhash2()) <= dynamicSimThreshold2){
										
										matchCandidate.friendCount ++;
										//matchCandidate.friendlist.add(eMember);
										eMember.isTempFriend = true;
										
										if(/*matchCandidate.friendlist.size()*/
												matchCandidate.friendCount == minFriendCount){ //target reached, he got the ticket to join in friends club
											coolDude = true;
											if(!PropsUtil.isStrictOnMembership()) break;
										}
									}
								} //done with friendship checking
														
								if(coolDude){ //now add him to friends club
									
									matchCandidate.isProceessed=true;
									cluster.add((T) matchCandidate);
									length++;
									
									//move temp friends count to real count
									for(Iterator<CloneFragment> it  = (Iterator<CloneFragment>) cluster.iterator(); it.hasNext();){
										CloneFragment eMember = it.next();
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
				
//				
//				
//				for(Integer locKey : lockeySet){ // 1st level index iterator
//					if(searchItem.getLineOfCode() - (searchItem.getLineOfCode() * PropsUtil.getLocTolerance()) < locKey && searchItem.getLineOfCode() + (searchItem.getLineOfCode()+PropsUtil.getLocTolerance()) > locKey){ //first level filter
//						Map<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = itemListFirstLevelMap.get(locKey);
//						Set<Byte> bitKeySet = itemListSecondLevelMap.keySet();
//						for(Byte bitKey : bitKeySet){ // 2nd level index iterator
//							if(searchItem.getOneBitCount() - dynamicSimThreshold1 <= bitKey && searchItem.getOneBitCount() + dynamicSimThreshold1 >= bitKey){ //2nd level filter
//								for(CloneFragment matchCandidate : itemListSecondLevelMap.get(bitKey)){
//							
//									if(!matchCandidate.isProceessed && ((hamming_dist(searchItem.getSimhash1(), matchCandidate.getSimhash2()) <= dynamicSimThreshold1
//											&& hamming_dist(searchItem.getSimhash2(), matchCandidate.getSimhash2()) <= dynamicSimThreshold2))){
//										
//										//check if at least clusterMembershipRatio times the existing members in the cluster are cool with this guy
//										int minFriendCount = (int) (cluster.size() * PropsUtil.getClusterMembershipRatio());
//										minFriendCount = minFriendCount < 1 ? 1: minFriendCount;
//										
//										boolean coolDude = false;
//										for(CloneFragment eMember:cluster){
//											
//											//set initial friendship to false
//											eMember.isTempFriend = false;
//											
//											if(hamming_dist(matchCandidate.getSimhash1(), eMember.getSimhash1()) <= dynamicSimThreshold1
//													&& hamming_dist(matchCandidate.getSimhash2(), eMember.getSimhash2()) <= dynamicSimThreshold2){
//												
//												matchCandidate.friendCount ++;
//												//matchCandidate.friendlist.add(eMember);
//												eMember.isTempFriend = true;
//												
//												if(/*matchCandidate.friendlist.size()*/
//														matchCandidate.friendCount == minFriendCount){ //target reached, he got the ticket to join in friends club
//													coolDude = true;
//													if(!PropsUtil.isStrictOnMembership()) break;
//												}
//											}
//										} //done with friendship checking
//																
//										if(coolDude){ //now add him to friends club
//											
//											matchCandidate.isProceessed=true;
//											cluster.add((T) matchCandidate);
//											length++;
//											
//											//move temp friends count to real count
//											for(Iterator<CloneFragment> it  = (Iterator<CloneFragment>) cluster.iterator(); it.hasNext();){
//												CloneFragment eMember = it.next();
//												if(eMember.isTempFriend) {
//													//eMember.friendlist.add(matchCandidate);
//													eMember.friendCount ++;
//												}
//												eMember.isTempFriend=false;
//											}
//											
//										}//new member add done							
//										
//									}
//								}
//							}// 2nd level filter
//						}//for: 2nd level index iterator	
//					}//first level filter
//				}
//				
				
				//cleanup noise from the friends club based on new friendship
				if(cluster.size() > 1 && PropsUtil.isStrictOnMembership()){

					//List<SourceItem> removedMember = new ArrayList<SourceItem>();
					
					//do{
					
						int minFriendCount = (int) (cluster.size() * PropsUtil.getClusterMembershipRatio());
						minFriendCount = minFriendCount < 1 ? 1: minFriendCount;

						//removedMember.clear();
						
						for(Iterator<CloneFragment> it  = (Iterator<CloneFragment>) cluster.iterator(); it.hasNext();){
							CloneFragment eMember = it.next();
							
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
				
				
			}else{ //simthreshold1 == 0
//				Map<Byte, ArrayList<CloneFragment>> itemListSecondLevelMap = itemListFirstLevelMap.get(searchItem.getLineOfCode());
//				for(CloneFragment matchCandidate : itemListSecondLevelMap.get(searchItem.getOneBitCount())){
//					if(!matchCandidate.isProceessed && searchItem.getSimhash1().equals(matchCandidate.getSimhash1())
//							&& searchItem.getSimhash2().equals(matchCandidate.getSimhash2())){
//						cluster.add((T) matchCandidate);
//						length++;
//						matchCandidate.isProceessed=true;
//					}
//				}
				
				for(CloneFragment matchCandidate : cloneIndex.getEntriesByIndex(searchItem.getLineOfCode(), searchItem.getOneBitCount())){
					if(!matchCandidate.isProceessed && searchItem.getSimhash1().equals(matchCandidate.getSimhash1())
							&& searchItem.getSimhash2().equals(matchCandidate.getSimhash2())){
						cluster.add((T) matchCandidate);
						length++;
						matchCandidate.isProceessed=true;
					}
				}
				
			}
			//finished neighbor search of current item and now record it
			capturedHash.add(searchItem.getSimhash1());
		}
		
		return cluster;
	}

	private int hamming_dist(Long simhash1, Long simhash2) {
		return Long.bitCount(simhash1 ^ simhash2);
	}

}
