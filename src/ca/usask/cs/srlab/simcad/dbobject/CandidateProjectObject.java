package ca.usask.cs.srlab.simcad.dbobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
public class CandidateProjectObject {
	
	@Id @GeneratedValue (strategy=GenerationType.AUTO)
	private Integer id;

	@Column
	private String projectName;
	
	@Column
	private String location;
	
	@OneToMany (fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<DetectionResultObject> detectionResultList;
	
	@OneToMany (fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<LineIndexObject> lineIndexList;
	
	@OneToMany (fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<FileIndexObject> fileIndexList;
	
	public CandidateProjectObject() {}
	
	public CandidateProjectObject(String projectName, String location) {
		this.projectName = projectName;
		this.location = location;
	}

	public void addLineIndex(LineIndexObject lineIndexObject){
		if(lineIndexList == null)
			lineIndexList = new ArrayList<LineIndexObject>();
		lineIndexList.add(lineIndexObject);
	}
	
	public void addFileIndex(FileIndexObject fileIndexObject){
		if(fileIndexList == null)
			fileIndexList = new ArrayList<FileIndexObject>();
		fileIndexList.add(fileIndexObject);
	}
	
	public void addDetectionResult(DetectionResultObject detectionResultObject) {
		if(detectionResultList == null)
			detectionResultList = new ArrayList<DetectionResultObject>();
		detectionResultList.add(detectionResultObject);		
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getId() {
		return id;
	}
	
	public List<LineIndexObject> getLineIndexList() {
		return lineIndexList;
	}
	
	public List<FileIndexObject> getFileIndexList() {
		return fileIndexList;
	}

	public List<DetectionResultObject> getDetectionResultList() {
		return detectionResultList;
	}

	public void setDetectionResultList(
			List<DetectionResultObject> detectionResultList) {
		this.detectionResultList = detectionResultList;
	}

	public Map<Integer, LineIndexObject> getLineIndexMap(){
		Map<Integer, LineIndexObject> lineIndexMap = new HashMap<Integer, LineIndexObject>(lineIndexList.size());
		for (LineIndexObject lineIndexObject : lineIndexList) {
			lineIndexMap.put(lineIndexObject.getLineNumber(), lineIndexObject);
		}
		return lineIndexMap;
	}
	
	public Map<Integer, FileIndexObject> getFileIndexMap(){
		Map<Integer, FileIndexObject> fileIndexMap = new HashMap<Integer, FileIndexObject>(lineIndexList.size());
		for (FileIndexObject fileIndexObject : fileIndexList) {
			fileIndexMap.put(fileIndexObject.getId(), fileIndexObject);
		}
		return fileIndexMap;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLineIndexList(List<LineIndexObject> lineIndexList) {
		this.lineIndexList = lineIndexList;
	}

	public void setFileIndexList(List<FileIndexObject> fileIndexList) {
		this.fileIndexList = fileIndexList;
	}

	public LineIndexObject getLineIndexObject(Integer lineCount) {
		for(LineIndexObject lineIndexObject : lineIndexList){
			if(lineIndexObject.getLineNumber() == lineCount)
				return lineIndexObject;
		}
		return null;
	}
	
	public FileIndexObject getFileIndexObject(String fileName) {
		for(FileIndexObject fileIndexObject : fileIndexList){
			if(fileIndexObject.getFileName().equals(fileName))
				return fileIndexObject;
		}
		return null;
	}

}
