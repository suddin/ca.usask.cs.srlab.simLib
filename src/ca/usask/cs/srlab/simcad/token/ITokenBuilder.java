package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.token.filter.ITokenFilter;

public interface ITokenBuilder {

	public Collection<String> generateToken(String codeFragment);

	public void setFilterChain(ITokenFilter tokenFilter);

}