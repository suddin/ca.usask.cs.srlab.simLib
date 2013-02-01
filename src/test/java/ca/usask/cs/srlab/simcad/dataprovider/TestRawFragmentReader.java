package ca.usask.cs.srlab.simcad.dataprovider;

import java.io.File;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.dataprovider.xml.FixTxlSourceFragmentOutputXML;
import ca.usask.cs.srlab.simcad.dataprovider.xml.RawFragment;
import ca.usask.cs.srlab.simcad.dataprovider.xml.RawFragmentReader;

public class TestRawFragmentReader {

	static String xmlFileName = null;
	
	@BeforeClass
	public static void setup() {
		xmlFileName = new FixTxlSourceFragmentOutputXML()
				.transform(Environment
						.getResourcePath("ca/usask/cs/srlab/simcad/detection/test_functions.xml"));
	}
	
	@Test
	public void testReadFragment() {
		 Set<RawFragment> rawFragmentList = RawFragmentReader.readRawFragments(xmlFileName);
		 
		 for(RawFragment rf : rawFragmentList){
			 System.out.println(rf);
		 }
	}
	
	@AfterClass
	public static void setdown() {
		if(xmlFileName !=null && new File(xmlFileName).exists())
			new File(xmlFileName).delete();
	}

}
