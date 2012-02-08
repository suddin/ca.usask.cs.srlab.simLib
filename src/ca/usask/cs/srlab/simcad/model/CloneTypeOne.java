package ca.usask.cs.srlab.simcad.model;

public class CloneTypeOne implements ICloneType {

	@Override
	public String getTypeName() {
		return "Type-1";
	}

	@Override
	public Integer getCloneType() {
		return CLONE_TYPE_1;
	}

}
