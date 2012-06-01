package ca.usask.cs.srlab.simcad.index.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import ca.usask.cs.srlab.simcad.dbobject.FileIndexObject;
import ca.usask.cs.srlab.simcad.dbobject.LineIndexObject;
import ca.usask.cs.srlab.simcad.index.ICloneIndex;

public abstract class AbstractDbCloneIndex implements ICloneIndex, IDbCloneIndex {
	private SessionFactory sessionFactory;
	private Configuration configuration;
	protected Session session;
	protected boolean isDirty;
	
	protected void configure(Configuration configuration){
		this.configuration = configuration;
		this.sessionFactory = this.configuration.configure()/*.setProperty(Environment.SHOW_SQL,"false")*/
			.buildSessionFactory();
		setupDB();
	}
	
    private void setupDB(){
 		SchemaExport schema = new SchemaExport(configuration);
 		schema.create(false, true);
    }
    
//    protected SessionFactory getSessionFactory() {
//		return sessionFactory;
//	}
    
    public Session openSession(){
    	if(session == null || !session.isOpen())
    		session = sessionFactory.openSession();
    	return session;
    }
    
	public void closeSession(){
		session.close();
	}
    
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
    
    //db crud operation
    
	private static void readObject(Session session, Class clazz) {
		if(clazz.equals(FileIndexObject.class)){
			List<FileIndexObject> result = session.createCriteria(clazz).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			//List<Company> result = session.createQuery("from Company").list();
			for (FileIndexObject c : result) {
				System.out.println(c);
			}
		}else if (clazz.equals(LineIndexObject.class)){
			List<LineIndexObject> result = session.createCriteria(LineIndexObject.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			for (LineIndexObject e : result) {
				System.out.println(e);
			}
		}
	}

	protected <T> Collection<T> getAllObject(Session session, Class<T> clazz) {
		return session.createCriteria(clazz).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
		
	protected <T> Object getObject(Session session, Class<T> clazz, Serializable id) {
		return (T) session.get(clazz, id);
	}
	
	protected void saveObject(Session session, Object object) {
		Transaction transaction = session.getTransaction();
		transaction.begin();
		session.save(object);
		transaction.commit();
	}

	protected void removeObject(Session session, Object object) {
		Transaction transaction = session.getTransaction();
		transaction.begin();
		session.delete(object);
		transaction.commit();
	}
	
	protected LineIndexObject getLineIndexObjectByLineOfCode(Session session,
			Integer lineOfCode) {
		// TODO Auto-generated method stub
		return null;
	}

	protected FileIndexObject getFileIndexObjectByFileName(Session session,
			String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
