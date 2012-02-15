package ca.usask.cs.srlab.simcad.util;

import java.util.Comparator;

/**
 * More efficient (in terms of performance) implementation of a String comparator.
 * Speed is gained by using hash code as a primary comparison attribute, which is cached for String.
 * Be aware that this ordering is not lexicographic, however stable.
 */
public final class FastStringComparator implements Comparator<String> {

  public static final FastStringComparator INSTANCE = new FastStringComparator();

  /**
   * Compares two strings (not lexicographically).
   */
  public int compare(String s1, String s2) {
    if (s1 == s2) { // NOSONAR false-positive: Compare Objects With Equals
      return 0;
    }
    int h1 = s1.hashCode();
    int h2 = s2.hashCode();
    if (h1 < h2) {
      return -1;
    } else if (h1 > h2) {
      return 1;
    } else {
      return s1.compareTo(s2);
    }
  }

  /**
   * Enforce use of a singleton instance.
   */
  private FastStringComparator() {
  }

}
