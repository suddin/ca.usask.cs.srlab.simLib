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
import javax.persistence.OneToMany;

import ca.usask.cs.srlab.simcad.model.CloneFragment;


@Entity
public class BitIndexObject {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column
	private Integer numberOf1Bit;
	
	@OneToMany (fetch=FetchType.EAGER/*, mappedBy="mappedToIndex"*/, cascade=CascadeType.PERSIST)
	private List<CloneFragment> cloneFragmentList;

	public BitIndexObject(){}
	
	public BitIndexObject(Integer numberOf1Bit){
		this.numberOf1Bit = numberOf1Bit;
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
