package ca.usask.cs.srlab.simcad.model;

public interface ICloneType {

    public static final Integer CLONE_TYPE_1 = 1;
    public static final Integer CLONE_TYPE_2 = 2;
    public static final Integer CLONE_TYPE_3 = 3;
    public static final Integer CLONE_TYPE_4 = 4;
    
    String getTypeName();
    
    Integer getCloneType();
}
