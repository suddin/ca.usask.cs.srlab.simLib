package ca.usask.cs.srlab.simcad.dbobject;

import java.io.File;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.Environment;
import ca.usask.cs.srlab.simcad.model.BlockCloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.CloneGroup;
import ca.usask.cs.srlab.simcad.model.ClonePair;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class DbConfigurationBuilder {

	public static Configuration buildConfigurationForDbIndex() {

		AnnotationConfiguration config = new AnnotationConfiguration();

		String simRoot = Environment.getSimLibRoot();
		String dbConfigFileUrl = PropsUtil.getDbConfigFileUrl();
		
		String hibConfig = Environment.getExternalResourcePath("hibernate.cfg.xml");
		
		config.configure(new File(hibConfig));

		config.addAnnotatedClass(FunctionCloneFragment.class)
				.addAnnotatedClass(BlockCloneFragment.class)
				.addAnnotatedClass(CloneFragment.class)
				.addAnnotatedClass(ClonePair.class)
				.addAnnotatedClass(CloneGroup.class)
				.addAnnotatedClass(CloneSet.class)
				.addAnnotatedClass(DetectionSettings.class)
				.addAnnotatedClass(DetectionResultObject.class)
				.addAnnotatedClass(CandidateProjectObject.class)
				.addAnnotatedClass(BitIndexObject.class)
				.addAnnotatedClass(LineIndexObject.class)
				.addAnnotatedClass(FileIndexObject.class);

		return config;
	}
}
