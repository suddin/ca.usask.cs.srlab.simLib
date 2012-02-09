package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

import ca.usask.cs.srlab.simcad.model.CloneFragment;

public interface ITokenBuilder {

	public Collection<String> generateToken(CloneFragment cloneFragment);

}