package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Comparator;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.FastStringComparator;

/**
 * Allows to determine if CloneFragment includes another CloneFragment.
 * Inclusion is the partial order, so in fact this class violates contracts of {@link Comparator},
 * however it allows to use {@link org.sonar.duplications.utils.SortedListsUtils} for efficient filtering.
 */
public final class ContainsInComparator implements Comparator<CloneFragment> {

  /**
   * Defines order by resourceId.
   */
  public static final Comparator<CloneFragment> RESOURCE_ID_COMPARATOR = new Comparator<CloneFragment>() {
    public int compare(CloneFragment o1, CloneFragment o2) {
      return FastStringComparator.INSTANCE.compare(o1.getFileName(), o2.getFileName());
    }
  };

  /**
   * Defines order by resourceId and by unitStart.
   */
  public static final Comparator<CloneFragment> CloneFragment_COMPARATOR = new Comparator<CloneFragment>() {
    public int compare(CloneFragment o1, CloneFragment o2) {
      int c = RESOURCE_ID_COMPARATOR.compare(o1, o2);
      if (c == 0) {
        return o1.getFromLine() - o2.getFromLine();
      }
      return c;
    }
  };

  private final int l1, l2;

  /**
   * Constructs new comparator for two parts with lengths {@code l1} and {@code l2} respectively.
   */
  public ContainsInComparator(int l1, int l2) {
    this.l1 = l1;
    this.l2 = l2;
  }

  /**
   * Compares two parts on inclusion.
   * part1 includes part2 if {@code (part1.resourceId == part2.resourceId) && (part1.unitStart <= part2.unitStart) && (part2.unitEnd <= part1.unitEnd)}.
   * 
   * @return 0 if part1 includes part2,
   *         1 if resourceId of part1 is greater than resourceId of part2 or if unitStart of part1 is greater than unitStart of part2,
   *         -1 in all other cases
   */
  public int compare(CloneFragment part1, CloneFragment part2) {
    int c = RESOURCE_ID_COMPARATOR.compare(part1, part2);
    if (c == 0) {
      if (part1.getFromLine() <= part2.getFromLine()) {
        if (part2.getFromLine() + l2 <= part1.getFromLine() + l1) {
          return 0; // part1 contains part2
        } else {
          return -1; // SortedListsUtils#contains should continue search
        }
      } else {
        return 1; // unitStart of part1 is less than unitStart of part2 - SortedListsUtils#contains should stop search
      }
    } else {
      return c;
    }
  }

}
