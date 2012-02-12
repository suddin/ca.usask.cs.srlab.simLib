package ca.usask.cs.srlab.simcad.index;

import java.util.Collection;
import java.util.Set;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public interface ICloneIndex {

  Collection<CloneFragment> getByResourceId(String resourceId);

  Collection<CloneFragment> getEntriesByIndex(Integer line, Integer bit);
  
  Collection<CloneFragment> getEntriesByIndex(IndexKey key);
  
  Collection<CloneFragment> getAllEntries(); 

  void insert(CloneFragment block);
  
  public Set<IndexKey> getAllKeys();
  
  public void cleanup();

}
