package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

public interface ITokenBuilder {

	public Collection<String> generateToken(String rawData);

	public TokenBuildStrategy getBuildStrategy();
	
	public void setBuildStrategy(TokenBuildStrategy strategy);
}