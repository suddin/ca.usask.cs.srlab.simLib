package ca.usask.cs.srlab.simcad.token.filter;

public abstract class AbstractTokenFilter implements ITokenFilter{

	private ITokenFilter targetFilter;

	protected AbstractTokenFilter() {
	}

	protected AbstractTokenFilter(ITokenFilter targetFilter) {
		this.targetFilter = targetFilter;
	}

	protected abstract String doFilter(String token);
	
	public String doFilterAndInvokeNext(String token) {
		String result = doFilter(token);
		if(targetFilter != null && result != null)
			return targetFilter.doFilterAndInvokeNext(result);
		return result;	
	}

}