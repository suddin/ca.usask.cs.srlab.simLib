package ca.usask.cs.srlab.simcad.index;

import org.junit.BeforeClass;


public class TestMemoryIndexByGoogleCollection extends AbstractMemoryIndexTest {
	
	@BeforeClass
	public static void setup(){
		supersetup(new MemoryCloneIndexByGoogleCollection());
	}
	
}
