package ca.usask.cs.srlab.simcad.index;

import ca.usask.cs.srlab.simcad.index.io.IndexIOConfiguration;
import ca.usask.cs.srlab.simcad.index.io.IndexIOHandler;
import ca.usask.cs.srlab.simcad.index.io.IndexIOHandlerFactory;

public abstract class AbstractCloneIndex implements ICloneIndex{

	protected String sourceProject;
	
	protected String sourceLanguage;
	
	protected String fragmentType;

	protected String transformationType;
	
	protected boolean isDirty;
	
	//protected ;
	
	public AbstractCloneIndex(String sourceProject, String sourceLanguage, String fragmentType, String transformationType){
		this.sourceProject = sourceProject;
		this.sourceLanguage = sourceLanguage;
		this.fragmentType = fragmentType;
		this.transformationType = transformationType;
	}
	
	public AbstractCloneIndex() {
	}

	@Override
	public String getSourceProject() {
		return sourceProject;
	}
	
	@Override
	public void setSourceProject(String sourceProject) {
		this.sourceProject = sourceProject;
	}
	
	@Override
	public String getSourceLanguage() {
		return sourceLanguage;
	}
	
	@Override
	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}
	
	@Override
	public String getFragmentType() {
		return fragmentType;
	}

	@Override
	public void setFragmentType(String fragmentType) {
		this.fragmentType = fragmentType;
	}
	
	@Override
	public String getTransformationType() {
		return transformationType;
	}
	
	@Override
	public void setTransformationType(String transformationType) {
		this.transformationType = transformationType;
	}
	
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	
	@Override
	public void exportIndex(IndexIOConfiguration indexIOConfiguration){
		IndexIOHandler indexIOHandler = IndexIOHandlerFactory.getHandlerInstance(indexIOConfiguration);
		indexIOHandler.doExport(this);
	}
	
	@Override
	public ICloneIndex importIndex(IndexIOConfiguration indexIOConfiguration){
		IndexIOHandler indexIOHandler = IndexIOHandlerFactory.getHandlerInstance(indexIOConfiguration);
		indexIOHandler.doImport(this);
		return this;
	}
}
