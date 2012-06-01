package ca.usask.cs.srlab.simcad.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;

public class AbstractMemoryIndexTest {

	@Rule 
	public TestName testName = new TestName();
	 
	ICloneIndex cloneIndex;
	static CloneFragment cf1;
	static CloneFragment cf11;
	static CloneFragment cf2;
	static CloneFragment cf3;
	
	
	public AbstractMemoryIndexTest() {
		super();
	}

	@BeforeClass
	public static void init(){
		cf1 = new FunctionCloneFragment("file1", 1, 4, 1, "original1() {code1}");
		cf1.setSimhash1(1234l);
		cf1.setSimhash2(1234l);

		cf11 = new FunctionCloneFragment("file11", 1, 4, 1, "original2() {code11}");
		cf11.setSimhash1(1234l);
		cf11.setSimhash2(1234l);
		
		cf2 = new FunctionCloneFragment("file1", 11, 44, 2, "original3() {code2}");
		cf2.setSimhash1(5678l);
		cf2.setSimhash2(5678l);
		
		cf3 = new FunctionCloneFragment("file3", 11, 44, 3, "original() {code3}");
		cf3.setSimhash1(3267l);
		cf3.setSimhash2(3267l);
	}
	
	public void supersetup(ICloneIndex cloneIndexx){
		cloneIndex = cloneIndexx;
		System.out.println("executing "+cloneIndex.getClass().getSimpleName() +" : "+ testName.getMethodName());
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
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> col = cloneIndex.getAllEntries();
		Assert.assertEquals(col.size(), 3);
	}

	@Test
	public void testGetAllUniqueResourceId(){
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		Collection<String> allUniqueResourceId = cloneIndex.getAllUniqueResourceId();
		
		Assert.assertEquals(2, allUniqueResourceId.size());
		
		Assert.assertTrue(allUniqueResourceId.contains(cf1.getFileName()));
		Assert.assertTrue(allUniqueResourceId.contains(cf3.getFileName()));
	}
	
	@Test
	public void testGetCollectionByResourceId() {
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> 
		
		col = cloneIndex.getAllByResourceId(cf1.getFileName());
		Assert.assertEquals(col.size(), 2);
		
		col = cloneIndex.getAllByResourceId(cf3.getFileName());
		Assert.assertEquals(col.size(), 1);
	}
	
	
	@Test
	public void testRemove() {
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> col = cloneIndex.getEntriesByIndex(cf1.getLineOfCode(), cf1.getOneBitCount());
		Assert.assertTrue(col.contains(cf1));
		
		cloneIndex.remove(cf1);
		
		col = cloneIndex.getEntriesByIndex(cf1.getLineOfCode(), cf1.getOneBitCount());
		Assert.assertTrue(!col.contains(cf1));
		
		col = cloneIndex.getAllByResourceId(cf1.getFileName());
		Assert.assertTrue(!col.contains(cf1));
	}
	
	@Test
	public void testRemoveAllByResourceId() {
		cloneIndex.insert(cf1);
		cloneIndex.insert(cf11);
		cloneIndex.insert(cf2);
		cloneIndex.insert(cf3);
	
		cloneIndex.removeAllByResourceId(cf1.getFileName());
	
		//both cf1 and cf2 should 1be removed
		Collection<CloneFragment> col = cloneIndex.getAllByResourceId(cf1.getFileName());
		Assert.assertNull(col);
		
		col = cloneIndex.getEntriesByIndex(cf1.getLineOfCode(), cf1.getOneBitCount());
		Assert.assertTrue(!col.contains(cf1));
		
		col = cloneIndex.getEntriesByIndex(cf2.getLineOfCode(), cf2.getOneBitCount());
		Assert.assertTrue(!col.contains(cf2));
		
		Collection<String> allUniqueResourceId = cloneIndex.getAllUniqueResourceId();
		Assert.assertTrue(!allUniqueResourceId.contains(cf1.getFileName()));
	}
	
	
	@Test
	public void testResetFlagAndRemoveInjected(){
		
		cloneIndex.insert(cf1);
		
		cf11.isInjected=true;
		cloneIndex.insert(cf11);
		
		cf2.isProceessed=true;
		cloneIndex.insert(cf2);
		
		cf3.isInjected=true;
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(4, allEntries.size());
		
		cloneIndex.resetDetectionFlagsAndRemoveAnythingInjected();
		
		allEntries = cloneIndex.getAllEntries();
		
		Assert.assertEquals(2, allEntries.size());
		
		Assert.assertFalse(allEntries.contains(cf11));
		Assert.assertFalse(allEntries.contains(cf3));
		
		Assert.assertTrue(allEntries.contains(cf1));
		Assert.assertTrue(allEntries.contains(cf2));
		
		Assert.assertFalse(cf2.isProceessed);
	}

	@Test
	public void testRemoveNothingInjectedInUnnownResource(){
		
		cloneIndex.insert(cf1);
		
		cf11.isInjected=true;
		cloneIndex.insert(cf11);
		
		cf2.isProceessed=true;
		cloneIndex.insert(cf2);
		
		cf3.isInjected=true;
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(4, allEntries.size());
		
		Collection<String> resourceIds = Arrays.asList("dummy_resource");
		cloneIndex.removeAnythingInjectedIn(resourceIds); //nothing injected in cf1.getFileName()!
		
		allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(4, allEntries.size());
	}
	
	
	@Test
	public void testRemoveOnlyInjectedIn(){
		
		cloneIndex.insert(cf1);
		
		cf11.isInjected=true;
		cloneIndex.insert(cf11);
		
		cf2.isProceessed=true;
		cloneIndex.insert(cf2);
		
		cf3.isInjected=true;
		cloneIndex.insert(cf3);
		
		Collection<CloneFragment> allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(4, allEntries.size());
		
		Collection<String> resourceIds = Arrays.asList(cf1.getFileName());
		cloneIndex.removeAnythingInjectedIn(resourceIds); //nothing injected in cf1.getFileName()!
		
		allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(4, allEntries.size());
		
		resourceIds = Arrays.asList(cf11.getFileName());
		cloneIndex.removeAnythingInjectedIn(resourceIds); //this one is injected hence should be removed
		
		allEntries = cloneIndex.getAllEntries();
		Assert.assertEquals(3, allEntries.size());
		
		Assert.assertFalse(allEntries.contains(cf11));

		Assert.assertTrue(allEntries.contains(cf1));
		Assert.assertTrue(allEntries.contains(cf2));
		Assert.assertTrue(allEntries.contains(cf3));
		
		Assert.assertTrue(cf2.isProceessed);
	}
	
	
	@Test
	public void testConcurrentIndexChange(){
		
		Thread worker1 = new Thread(new Runnable() {
			@Override
			public void run() {
				cloneIndex.insert(cf1);
				
				cf11.isInjected=true;
				cloneIndex.insert(cf11);
				
			}
		});
		worker1.setName("insert");
		
		Thread worker2 = new Thread(new Runnable() {
			@Override
			public void run() {
				cf2.isProceessed=true;
				cloneIndex.insert(cf2);
				
				cf3.isInjected=true;
				cloneIndex.insert(cf3);
			}
		});
		worker2.setName("insert again");
		
		
		Thread worker3 = new Thread(new Runnable() {
			@Override
			public void run() {
				Collection<String> resourceIds = Arrays.asList(cf11.getFileName());
				cloneIndex.removeAnythingInjectedIn(resourceIds);
			}
		});
		worker3.setName("remove inject in defined resource");
		
		Thread worker4 = new Thread(new Runnable() {
			@Override
			public void run() {
				cloneIndex.resetDetectionFlagsAndRemoveAnythingInjected();
			}
		});
		worker4.setName("reset flag and remove injected");
		
		
		worker1.start();
		worker2.start();
		worker3.start();
		worker4.start();
		
		while(true){
			if(worker1.isAlive() || worker2.isAlive() || worker3.isAlive() || worker4.isAlive()){
				continue;
			}
			else{
				break;
			}
		}
		
		Collection<CloneFragment>allEntries = cloneIndex.getAllEntries();
		
		Assert.assertEquals(2, allEntries.size());
		
		Assert.assertFalse(allEntries.contains(cf11));
		Assert.assertFalse(allEntries.contains(cf3));
		
		Assert.assertTrue(allEntries.contains(cf1));
		Assert.assertTrue(allEntries.contains(cf2));
		
		Assert.assertFalse(cf2.isProceessed);
	}
}