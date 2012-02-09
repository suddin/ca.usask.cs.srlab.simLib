package ca.usask.cs.srlab.simcad.token.filter;

public final class NullFilter extends AbstractTokenFilter {

	public static final ITokenFilter INSTANCE = new NullFilter();

	private NullFilter() {
	}
	
	@Override
	protected String doFilter(String token) {
		//do nothing
		return token;
	}
	
}
