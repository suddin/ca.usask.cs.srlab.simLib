package ca.usask.cs.srlab.simcad.index;

import org.junit.Before;

import ca.usask.cs.srlab.simcad.index.memory.MemoryCloneIndexByGoogleCollection;


public class TestMemoryIndexByGoogleCollection extends AbstractMemoryIndexTest {
	
	@Before
	public void setup(){
		supersetup(new MemoryCloneIndexByGoogleCollection());
	}
	
}
