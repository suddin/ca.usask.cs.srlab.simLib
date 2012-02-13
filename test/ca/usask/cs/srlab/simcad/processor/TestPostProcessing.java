package ca.usask.cs.srlab.simcad.processor;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.detection.CloneDetector;
import ca.usask.cs.srlab.simcad.detection.DetectionTest;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.postprocess.XmlOutputProcessor;

public class TestPostProcessing extends DetectionTest{
	
	@Test
	public void testType1CloneOutputToXML(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_type1_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
		ProcessorDisptacher pd = ProcessorDisptacher.getInstance();
		IProcessor processor = new XmlOutputProcessor(detectionSettings_type1_group, Environment.getResourcePath("./"));
		
		Collection<CloneSet> afterProcessResult = pd.addProcessor(processor).applyOn(result);
		Assert.assertArrayEquals(result.toArray(), afterProcessResult.toArray());
		
	}
	
	@Test
	public void testNearMissCloneOutputToXML(){
		cloneDetector = CloneDetector.setup(cloneIndex, detectionSettings_nearmiss_pair);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
		ProcessorDisptacher pd = ProcessorDisptacher.getInstance();
		IProcessor processor = new XmlOutputProcessor(detectionSettings_nearmiss_pair, Environment.getResourcePath("./"));

		Collection<CloneSet> afterProcessResult = pd.addProcessor(processor).applyOn(result);
		Assert.assertArrayEquals(result.toArray(), afterProcessResult.toArray());
		
	}
}
