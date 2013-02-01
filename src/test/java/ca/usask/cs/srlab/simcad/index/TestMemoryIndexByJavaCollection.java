package ca.usask.cs.srlab.simcad.index;

import org.junit.Before;

import ca.usask.cs.srlab.simcad.index.memory.MemoryCloneIndexByJavaCollection;


public class TestMemoryIndexByJavaCollection extends AbstractMemoryIndexTest{
	
	@Before
	public void setupIndex(){
		supersetup(new MemoryCloneIndexByJavaCollection());
	}
}
