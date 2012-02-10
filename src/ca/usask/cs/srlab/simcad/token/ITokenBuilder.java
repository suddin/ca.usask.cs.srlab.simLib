package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.token.filter.ITokenFilter;

public interface ITokenBuilder {

	public Collection<String> generateToken(CloneFragment cloneFragment);

	public void setFilterChain(ITokenFilter tokenFilter);

}