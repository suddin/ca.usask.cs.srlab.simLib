package ca.usask.cs.srlab.simcad.index.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class LineIndexObject {
	
//	@Id @GeneratedValue (strategy=GenerationType.AUTO)
//	private Integer id;
	
	@Id @Generated(value="assigned")
	@Column (name="id")
	private Integer lineNumber;
	
	@OneToMany (fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<BitIndexObject> bitIndexList;
	
	public void addBitIndex(BitIndexObject bitIndexObject){
		if(bitIndexList == null)
			bitIndexList = new ArrayList<BitIndexObject>();
		bitIndexList.add(bitIndexObject);
	}
	
//	public Integer getId() {
//		return id;
//	}
	
	public Integer getLineNumber() {
		return lineNumber;
	}
	
	public List<BitIndexObject> getBitIndexList() {
		return bitIndexList;
	}
	
	public Map<Integer, BitIndexObject> getBitIndexMap(){
		Map<Integer, BitIndexObject> bitIndexMap = new HashMap<Integer, BitIndexObject>(bitIndexList.size());
		for (BitIndexObject bitIndexObject : bitIndexList) {
			bitIndexMap.put(bitIndexObject.getNumberOf1Bit(), bitIndexObject);
		}
		return bitIndexMap;
	}
	
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setBitIndexList(List<BitIndexObject> bitIndexList) {
		this.bitIndexList = bitIndexList;
	}
	
}
