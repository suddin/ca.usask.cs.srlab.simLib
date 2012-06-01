package ca.usask.cs.srlab.simcad.dbobject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import ca.usask.cs.srlab.simcad.hash.ApacheHash;
import ca.usask.cs.srlab.simcad.model.CloneFragment;

@Entity
public class FileIndexObject {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Lob
	@Column (length=32767)
	private String fileName;

	@OneToMany (cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	//@JoinColumn(name="FILEINDEXOBJECT_ID")	
	private List<CloneFragment> cloneFragmentList;

	public FileIndexObject(){}
	
	public FileIndexObject(String fileName) {
		this.fileName = fileName;
		//this.id  = fileNameToFileIndexId(fileName);
	}

	public static int fileNameToFileIndexId(String fileName) {
		return ApacheHash.lookup3ycs(fileName, 0, fileName.length(), 0);
	}

	public void addCloneFragment(CloneFragment cloneFragment){
		if(cloneFragmentList == null)
			cloneFragmentList = new ArrayList<CloneFragment>();
		cloneFragmentList.add(cloneFragment);
	}

	public boolean removeCloneFragment(CloneFragment cloneFragment) {
		if(cloneFragmentList != null)
			return cloneFragmentList.remove(cloneFragment);
		return false;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<CloneFragment> getCloneFragmentList() {
		return cloneFragmentList;
	}

	public void setCloneFragmentList(List<CloneFragment> cloneFragmentList) {
		this.cloneFragmentList = cloneFragmentList;
	}
}
