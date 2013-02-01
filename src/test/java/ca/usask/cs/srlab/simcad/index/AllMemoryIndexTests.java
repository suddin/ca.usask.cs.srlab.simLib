package ca.usask.cs.srlab.simcad.index;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestMemoryIndexByJavaCollection.class, TestMemoryIndexByCommonsCollection.class, TestMemoryIndexByGoogleCollection.class })
public class AllMemoryIndexTests {

}
