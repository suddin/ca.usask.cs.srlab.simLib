package ca.usask.cs.srlab.simcad.index.io;

public class IndexIOHandlerFactory {
	
	private IndexIOHandlerFactory(){};

	public static IndexIOHandler getHandlerInstance(IndexIOConfiguration indexIOConfiguration) {
		if(indexIOConfiguration !=null && indexIOConfiguration instanceof XMLIndexIOConfiguration)
			return new XMLIndexIOHandler((XMLIndexIOConfiguration) indexIOConfiguration);
		return null;
	}

}
