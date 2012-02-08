package ca.usask.cs.srlab.simcad.index.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import ca.usask.cs.srlab.simcad.model.CloneFragment;


@Entity
public class FileIndexObject {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column (length=64)
	private String fileNameHash;

	@OneToMany (cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="FILEINDEXOBJECT_ID")	
	private List<CloneFragment> cloneFragmentList;

	public void addCloneFragment(CloneFragment cloneFragment){
		if(cloneFragmentList == null)
			cloneFragmentList = new ArrayList<CloneFragment>();
		cloneFragmentList.add(cloneFragment);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileNameHash() {
		return fileNameHash;
	}

	public void setFileNameHash(String fileNameHash) {
		this.fileNameHash = fileNameHash;
	}

	public List<CloneFragment> getCloneFragmentList() {
		return cloneFragmentList;
	}

	public void setCloneFragmentList(List<CloneFragment> cloneFragmentList) {
		this.cloneFragmentList = cloneFragmentList;
	}
	
}
