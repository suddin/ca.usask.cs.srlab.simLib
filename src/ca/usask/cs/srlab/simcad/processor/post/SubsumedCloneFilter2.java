
package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

import com.google.common.collect.Lists;

/**
 * Performs incremental and brute force algorithm in order to filter clones, which are fully covered by other clones.
 * All clones for filtering must be of the same origin - there is no sanity check on this.
 * In a worst case it performs O(N*N) comparisons.
 * <p>
 * Godin: This implementation was chosen because it simple.
 * And I wasn't able to find big difference in performance with an interval tree, which we had for the moment of testing.
 * Whereas in fact I expected that interval tree would be better for this task.
 * Moreover with interval tree we also can use incremental approach,
 * but we did not had an implementation with remove operation for the moment of testing.
 * </p>
 */
final class SubsumedCloneFilter2 extends AbstractProcessor{

  /**
   * Note that LinkedList should provide better performance here, because of use of operation remove.
   * 
   * @see #add(CloneSet)
   */
  private final List<CloneSet> filtered = Lists.newLinkedList();

  /**
   * @return current results of filtering
   */
  public List<CloneSet> getResult() {
    return filtered;
  }

  /**
   * Running time - O(N*2*C), where N - number of clones, which was found earlier and C - time of {@link #containsIn(CloneSet, CloneSet)}.
   */
  private void add(CloneSet current) {
    Iterator<CloneSet> i = filtered.iterator();
    while (i.hasNext()) {
      CloneSet earlier = i.next();
      // Note that following two conditions cannot be true together - proof by contradiction:
      // let C be the current clone and A and B were found earlier
      // then since relation is transitive - (A in C) and (C in B) => (A in B)
      // so A should be filtered earlier
      if (containsIn(current, earlier)) {
        // current clone fully covered by clone, which was found earlier
        return;
      }
      if (containsIn(earlier, current)) {
        // current clone fully covers clone, which was found earlier
        i.remove();
      }
    }
    filtered.add(current);
  }

  /**
   * Checks that second clone contains first one.
   * <p>
   * Clone A is contained in another clone B, if every part pA from A has part pB in B,
   * which satisfy the conditions:
   * <pre>
   * (pA.resourceId == pB.resourceId) and (pB.unitStart <= pA.unitStart) and (pA.unitEnd <= pB.unitEnd)
   * </pre>
   * And all resourcesId from B exactly the same as all resourceId from A, which means that also every part pB from B has part pA in A,
   * which satisfy the condition:
   * <pre>
   * pB.resourceId == pA.resourceId
   * </pre>
   * So this relation is:
   * <ul>
   * <li>reflexive - A in A</li>
   * <li>transitive - (A in B) and (B in C) => (A in C)</li>
   * <li>antisymmetric - (A in B) and (B in A) <=> (A = B)</li>
   * </ul>
   * </p>
   * <p>
   * <strong>Important: this method relies on fact that all parts were already sorted by resourceId and unitStart by using
   * {@link BlocksGroup.BlockComparator}, which uses {@link org.sonar.duplications.utils.FastStringComparator} for comparison by resourceId.</strong>
   * </p>
   * <p>
   * Running time - O(|A|+|B|).
   * </p>
   */
  static boolean containsIn(CloneSet first, CloneSet second) {
	return false;
//    if (first.size() > second.size()) {
//      return false;
//    }
//    List<CloneFragment> firstParts = first.getCloneFragments();
//    List<CloneFragment> secondParts = second.getCloneFragments();
//    return SortedListsUtils.contains(secondParts, firstParts, new ContainsInComparator(second.getCloneUnitLength(), first.getCloneUnitLength()))
//        && SortedListsUtils.contains(firstParts, secondParts, ContainsInComparator.RESOURCE_ID_COMPARATOR);
  }

@Override
public boolean process(Collection<CloneSet> inputCloneSets, Collection<CloneSet> outputCloneSets) {
	for(CloneSet cloneSet : inputCloneSets){
		add(cloneSet);
	}
	outputCloneSets.addAll(filtered);
	return true;
}

}
