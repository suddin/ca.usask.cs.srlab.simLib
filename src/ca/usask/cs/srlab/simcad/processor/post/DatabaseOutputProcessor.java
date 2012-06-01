package ca.usask.cs.srlab.simcad.processor.post;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dbobject.CandidateProjectObject;
import ca.usask.cs.srlab.simcad.dbobject.DetectionResultObject;
import ca.usask.cs.srlab.simcad.model.CloneSet;
import ca.usask.cs.srlab.simcad.processor.AbstractProcessor;

public class DatabaseOutputProcessor extends AbstractProcessor {

	private SessionFactory sessionFactory;
	private DetectionSettings detectionSettings;
	private String projectName;
	
	public DatabaseOutputProcessor(String projectName, DetectionSettings detectionSettings, Configuration dbConfiguration) {
		super();
		//Configuration configuration = DbConfigurationBuilder.buildConfigurationForDbIndex();
		this.projectName = projectName;
		this.detectionSettings = detectionSettings;
		configure(dbConfiguration);
	}

	private void configure(Configuration configuration){
		this.sessionFactory = configuration.buildSessionFactory();
		SchemaExport schema = new SchemaExport(configuration);
 		//schema.setOutputFile(configFileLocation+"/schema.sql");
 		schema.create(false, true);
	}
	
	@Override
	public boolean process(Collection<CloneSet> inputCloneSets,
			Collection<CloneSet> outputCloneSets) {
		super.process(inputCloneSets, outputCloneSets);
		
		Session session = this.sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		
		try{
			CandidateProjectObject cpo = getCandidateProjectByName(session, projectName);
			
			if(cpo == null){
				cpo = new CandidateProjectObject(projectName, "");
			}
			
			List<DetectionResultObject> detectionResultList = cpo.getDetectionResultList();
			
			DetectionResultObject candidateDro = null;
			
			if(detectionResultList != null){
				for(DetectionResultObject dro : detectionResultList){
					if(dro.getDetectionSettings().equals(detectionSettings)){
						candidateDro = dro;
						break;
					}
				}
			}
			
			if(candidateDro != null){
				candidateDro.clearOutputClones();
				candidateDro.addAllOutputClones(inputCloneSets);
			}else{
				candidateDro = new DetectionResultObject(detectionSettings, inputCloneSets);
				cpo.addDetectionResult(candidateDro);
			}
			
			session.save(cpo);
			tx.commit();
			
		}catch (Exception e) {
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
			throw new SimcadException("Error in saving clone detection result o database", e);
		}finally{
			if(session != null && session.isOpen())
				session.close();
		}
		
		outputCloneSets.addAll(inputCloneSets);
		return false;
	}


	private CandidateProjectObject getCandidateProjectByName(Session session,
			String projectName) {
		Query query = session
				.createQuery("from CandidateProjectObject as cpo where cpo.projectName = '"
						+ projectName + "'");
		List result = query.list();

		return result != null && result.size() > 0 ? (CandidateProjectObject) result.get(0) : null;
	}

}
