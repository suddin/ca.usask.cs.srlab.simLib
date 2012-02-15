package ca.usask.cs.srlab.simcad.index;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;

public class AbstractMemoryIndexTest {

	static ICloneIndex cloneIndex;
	static CloneFragment cf1;
	static CloneFragment cf2;
	static CloneFragment cf3;
	
	
	public AbstractMemoryIndexTest() {
		super();
	}
	
	public static void supersetup(ICloneIndex cloneIndexx){
		cloneIndex = cloneIndexx;
		cf1 = new FunctionCloneFragment("file1", 1, 4, 1, "original code1");
		cf1.setSimhash1(1234l);
		cf1.setSimhash2(1234l);

		cf2 = new FunctionCloneFragment("file1", 11, 44, 2, "original code2");
		cf2.setSimhash1(5678l);
		cf2.setSimhash2(5678l);
		
		cf3 = new FunctionCloneFragment("file3", 11, 44, 3, "original code3");
		cf3.setSimhash1(3267l);
		cf3.setSimhash2(3267l);
		
		System.out.println("testing with : "+cloneIndex.getClass().getSimpleName());
	}
	
	@Test
	public void InsertTest() {
		cloneIndex.insert(cf1);
		
		Collection<CloneFragment> col = cloneIndex.getEntriesByIndex(cf1.getLineOfCode(), cf1.getOneBitCount());
		
		
		Assert.assertEquals(col.size(), 1);
		Assert.assertEquals((new ArrayList<CloneFragment>(col)).get(0) , cf1);
	}

	@Test
	public void testGetAll() {
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> col = cloneIndex.getAllEntries();
		Assert.assertEquals(col.size(), 3);
	}

	@Test
	public void testGetCollectionByResourceId() {
		Collection<CloneFragment> 
		
		col = cloneIndex.getByResourceId(cf1.getFileName());
		Assert.assertEquals(col.size(), 2);
		
		col = cloneIndex.getByResourceId(cf3.getFileName());
		Assert.assertEquals(col.size(), 1);
	}

}