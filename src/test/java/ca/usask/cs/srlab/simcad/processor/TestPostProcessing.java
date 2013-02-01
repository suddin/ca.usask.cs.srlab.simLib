package ca.usask.cs.srlab.simcad.processor;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.dbobject.CandidateProjectObject;
import ca.usask.cs.srlab.simcad.dbobject.DbConfigurationBuilder;
import ca.usask.cs.srlab.simcad.detection.CloneDetector;
import ca.usask.cs.srlab.simcad.detection.DetectionTest;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.post.DatabaseOutputProcessor;
import ca.usask.cs.srlab.simcad.processor.post.XmlOutputProcessor;

/*
antlr-2.7.6.jar
asm-attrs.jar
asm.jar
c3p0-0.9.1.jar
cglib-2.1.3.jar
dom4j-1.6.1.jar
*/

public class TestPostProcessing extends DetectionTest{
	
	@Test
	public void testType1CloneOutputToXML(){

		File f1 = new File(Environment.getResourcePath("./")+System.getProperty("file.separator")+"simcad_function_clone-groups_type-1_generous.xml");
		File f2 = new File(Environment.getResourcePath("./")+System.getProperty("file.separator")+"simcad_function_clone-groups_type-1_generous_withsource.xml");

		if (f1.exists())
			f1.delete();
		if (f2.exists())
			f2.delete();
		
		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type1_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type1_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
		ProcessorDisptacher pd = ProcessorDisptacher.getInstance();
		IProcessor processor = new XmlOutputProcessor(detectionSettings_type1_group, Environment.getResourcePath("./"));
		
		Collection<CloneSet> afterProcessResult = pd.addProcessor(processor).applyOn(result, detectionSettings_type1_group);
		Assert.assertArrayEquals(result.toArray(), afterProcessResult.toArray());
		
		Assert.assertTrue(f1.exists());
		Assert.assertTrue(f2.exists());
		
	}
	
	@Test
	public void testType1CloneOutputToDB(){

		indexBuilder.buildCloneIndex(cloneIndex, detectionSettings_type1_group);
		cloneDetector = CloneDetector.getInstance(cloneIndex, detectionSettings_type1_group);
		Collection<CloneFragment> candidateFragments = cloneIndex.getAllEntries();
		List<CloneSet> result = cloneDetector.detect(candidateFragments);
		
		ProcessorDisptacher pd = ProcessorDisptacher.getInstance();
		
		Configuration dbConfiguration = DbConfigurationBuilder.buildConfigurationForDbIndex();
		IProcessor processor = new DatabaseOutputProcessor("dnsjava-0-3", detectionSettings_type1_group, dbConfiguration);
		
		Collection<CloneSet> afterProcessResult = pd.addProcessor(processor).applyOn(result, detectionSettings_type1_group);
		Assert.assertArrayEquals(result.toArray(), afterProcessResult.toArray());
		
		//test from database
		SessionFactory sessionFactory = dbConfiguration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		CandidateProjectObject savedCpo = (CandidateProjectObject) session.get(CandidateProjectObject.class, 1);
		
		Assert.assertEquals(1, savedCpo.getDetectionResultList().size());
		Assert.assertEquals(detectionSettings_type1_group, savedCpo.getDetectionResultList().get(0).getDetectionSettings());
		Assert.assertEquals(1, savedCpo.getDetectionResultList().get(0).getOutputClones().size());
	}
}
