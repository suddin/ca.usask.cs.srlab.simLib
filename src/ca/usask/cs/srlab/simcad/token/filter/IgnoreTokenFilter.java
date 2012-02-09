package ca.usask.cs.srlab.simcad.token.filter;

import java.util.HashSet;
import java.util.Set;

public final class IgnoreTokenFilter extends AbstractTokenFilter {

	protected static Set<String> tokenSetToIgnore = new HashSet<String>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private IgnoreTokenFilter() {
	}
	
	private IgnoreTokenFilter(ITokenFilter targetFilter){
		super(targetFilter);
	}
	
	@Override
	protected String doFilter(String token) {
		if (tokenSetToIgnore.contains(token))
			return null;
		return token;
	}

	
	public static final class FilterBuilder{
		
		public IgnoreTokenFilter build(){
			return new IgnoreTokenFilter();
		}
		
		public IgnoreTokenFilter build(ITokenFilter targetFilter){
			return new IgnoreTokenFilter(targetFilter);
		}
		
		public FilterBuilder addToken(String token){
			tokenSetToIgnore.add(token);
			return this;
		}
	}
	
}
