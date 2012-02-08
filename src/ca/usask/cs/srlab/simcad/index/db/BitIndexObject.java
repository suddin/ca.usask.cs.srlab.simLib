package ca.usask.cs.srlab.simcad.index.db;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ca.usask.cs.srlab.simcad.model.CloneFragment;


@Entity
public class BitIndexObject {
	
	@Id @Generated(value="assigned")
	private Integer id;
	
	@Column
	private Integer numberOf1Bit;
	
	@OneToMany (fetch=FetchType.EAGER, mappedBy="mappedToIndex")
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

	public Integer getNumberOf1Bit() {
		return numberOf1Bit;
	}

	public void setNumberOf1Bit(Integer numberOf1Bit) {
		this.numberOf1Bit = numberOf1Bit;
	}

	public List<CloneFragment> getCloneFragmentList() {
		return cloneFragmentList;
	}

	public void setCloneFragmentList(List<CloneFragment> cloneFragmentList) {
		this.cloneFragmentList = cloneFragmentList;
	}
	
}
