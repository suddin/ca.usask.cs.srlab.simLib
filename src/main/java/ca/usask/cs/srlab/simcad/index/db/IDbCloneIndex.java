package ca.usask.cs.srlab.simcad.index.db;

import org.hibernate.Session;

public interface IDbCloneIndex {
	public Session openSession();
	public void closeSession();
}
