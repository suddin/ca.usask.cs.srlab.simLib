package ca.usask.cs.srlab.simcad.token.filter;

public interface ITokenFilter {

	public String doFilterAndInvokeNext(String token);
}
