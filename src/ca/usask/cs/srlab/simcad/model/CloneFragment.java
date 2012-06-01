package ca.usask.cs.srlab.simcad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * @author sharif
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class CloneFragment implements ICloneFragment, Cloneable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Integer programComponentId;
	
	@Lob
	@Column(length=32767)
	private String fileName;
	
	@Column
	private Integer fromLine;

	@Column
	private Integer toLine;

	@Column
	private Long simhash1;
	
	@Column
	private Long simhash2;
	
	@Lob
	private String originalCodeBlock;

//	@Lob
//	@Column(length=32767)
//	private String transformedCodeBlock;
	
//	@ManyToOne
//	@JoinColumn (name="BITINDEXOBJECT_ID")
//	private BitIndexObject mappedToIndex;
//	
//	public CloneFragment(){
//		
//	}
	
	/**
	 * copy constructor
	 */
	public CloneFragment(CloneFragment cloneFragment) {
		this.programComponentId = cloneFragment.programComponentId;
		this.fileName = cloneFragment.fileName;
		this.fromLine = cloneFragment.fromLine;
		this.toLine = cloneFragment.toLine;
		this.id = cloneFragment.id;
		this.simhash1 = cloneFragment.simhash1;
		this.simhash2 = cloneFragment.simhash2;
		this.originalCodeBlock = cloneFragment.originalCodeBlock;
	}
	
	public CloneFragment(String fileName, Integer fromLine, Integer toLine,
			Integer programComponentId, String codeBlock/*, Long id,*/ 
			/*String transformedCodeBlock, long simhash1, long simhash2*/) {
		//this.id = id;
		this.programComponentId = programComponentId;
		this.fileName = fileName;
		this.fromLine = fromLine;
		this.toLine = toLine;
		this.originalCodeBlock = codeBlock;
//		this.transformedCodeBlock = transformedCodeBlock;
//		this.simhash1 = simhash1;
//		this.simhash2 = simhash2;
	}

	public CloneFragment() {
	}

	public Long getId() {
		return id;
	}

	public Integer getProgramComponentId() {
		return programComponentId;
	}

	@Override
	public Integer getFromLine() {
		return fromLine;
	}

	@Override
	public Integer getToLine() {
		return toLine;
	}

	@Override
	public String getOriginalCodeBlock() {
		return originalCodeBlock;
	}

	public void setOriginalCodeBlock(String originalCodeBlock) {
		this.originalCodeBlock = originalCodeBlock;
	}

//	public BitIndexObject getMappedToIndex() {
//		return mappedToIndex;
//	}
//
//	public void setMappedToIndex(BitIndexObject mappedToIndex) {
//		this.mappedToIndex = mappedToIndex;
//	}

//	public String getTransformedCodeBlock() {
//		return transformedCodeBlock;
//	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSimhash1() {
		return simhash1;
	}

	public void setSimhash1(Long simhash1) {
		this.simhash1 = simhash1;
	}

	public Long getSimhash2() {
		return simhash2;
	}

	public void setSimhash2(Long simhash2) {
		this.simhash2 = simhash2;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFromLine(Integer fromLine) {
		this.fromLine = fromLine;
	}

	public void setToLine(Integer toLine) {
		this.toLine = toLine;
	}

//	public void setTransformedCodeBlock(String transformedCodeBlock) {
//		this.transformedCodeBlock = transformedCodeBlock;
//	}

	@Transient
	private transient int bitCount;
	
	@Transient
	public Integer getOneBitCount(){
		if(bitCount == 0)
			bitCount = Long.bitCount(this.simhash1);
		return bitCount;
	}
	
	@Transient
	private transient int lineOfCode;  
	
	@Transient
	public Integer getLineOfCode(){
		if(this.lineOfCode == 0 && originalCodeBlock != null){
			this.lineOfCode = computeActualLineOfCode(originalCodeBlock);
		}
		return lineOfCode;
	}

	public static int computeActualLineOfCode(String codeBlock) {
		String []line = codeBlock.split("\n");
		int loc = 0; 
		for(String ln : line){
			if(ln.length() > 0)
				loc++;
		}
		return loc;
	}

	public void resetDetectionFlags(){
		isTempFriend = false;
		isProceessed = false;
		friendCount = 0;
	}
	
	//detection flags
	public transient boolean isTempFriend;
	public transient boolean isProceessed;
	public transient int friendCount;
	//detection flags
	
	public transient boolean isInjected;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CloneFragment) {
			CloneFragment another = (CloneFragment) obj;
			return another.fileName.equals(fileName)
					&& another.fromLine == fromLine && another.toLine == toLine
					&& another.simhash1 == simhash1;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int h = fileName.hashCode();
		h = 31 * h + fromLine;
		h = 31 * h + toLine;
		h = 31 * h + simhash1.intValue();
		return h;
	}
	
	@Override
	public String toString() {
		return fileName+" ("+fromLine+","+toLine+")\n"+originalCodeBlock/*+"\n"+transformedCodeBlock*/;
	}
	
	public String toShortString() {
		return fileName+" ("+fromLine+","+toLine+")";
	}
	
	@Override
	public CloneFragment clone() throws CloneNotSupportedException {
		return (CloneFragment) super.clone();
	}
	
}
