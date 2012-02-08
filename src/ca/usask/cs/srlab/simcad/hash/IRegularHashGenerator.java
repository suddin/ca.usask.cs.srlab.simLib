package ca.usask.cs.srlab.simcad.hash;

public interface IRegularHashGenerator {

	long generate64BitHashFor(String key);

	long generate64BitHashFor(String key, long seed);

}