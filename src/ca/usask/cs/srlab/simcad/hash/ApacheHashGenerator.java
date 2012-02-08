package ca.usask.cs.srlab.simcad.hash;

public class ApacheHashGenerator implements IRegularHashGenerator {

	/* (non-Javadoc)
	 * @see ca.usask.cs.srlab.simcad.hash.IRegularHashGenerator#generate64BitHashFor(java.lang.String)
	 */
	@Override
	public long generate64BitHashFor(String key) {
		return generate64BitHashFor(key, 0);
	}

	/* (non-Javadoc)
	 * @see ca.usask.cs.srlab.simcad.hash.IRegularHashGenerator#generate64BitHashFor(java.lang.String, long)
	 */
	@Override
	public long generate64BitHashFor(String key, long seed) {
		return ApacheHash.lookup3ycs64(key, 0, key.length(), seed);
	}
}
