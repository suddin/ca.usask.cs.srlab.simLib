package ca.usask.cs.srlab.simcad.index;

import org.junit.Before;

import ca.usask.cs.srlab.simcad.index.memory.MemoryCloneIndexByCommonsCollection;


public class TestMemoryIndexByCommonsCollection extends AbstractMemoryIndexTest {
	
	@Before
	public void setup(){
		supersetup(new MemoryCloneIndexByCommonsCollection());
	}
	
}
