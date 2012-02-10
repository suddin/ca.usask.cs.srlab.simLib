package ca.usask.cs.srlab.simcad.token.filter;

import java.util.HashSet;
import java.util.Set;

public final class IgnoreTokenFilter extends AbstractTokenFilter {

	private Set<String> tokenSetToIgnore = new HashSet<String>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private IgnoreTokenFilter(Set<String> tokenSetToIgnore) {
		this.tokenSetToIgnore = tokenSetToIgnore;
	}
	
	private IgnoreTokenFilter(ITokenFilter targetFilter, Set<String> tokenSetToIgnore){
		super(targetFilter);
		this.tokenSetToIgnore = tokenSetToIgnore;
	}
	
	@Override
	protected String doFilter(String token) {
		if (tokenSetToIgnore.contains(token))
			return null;
		return token;
	}

	
	public static final class FilterBuilder{
		private Set<String> tokenSet = new HashSet<String>();
		public IgnoreTokenFilter build(){
			return new IgnoreTokenFilter(tokenSet);
		}
		
		public IgnoreTokenFilter build(ITokenFilter targetFilter){
			return new IgnoreTokenFilter(targetFilter, tokenSet);
		}
		
		public FilterBuilder addToken(String token){
			tokenSet.add(token);
			return this;
		}
	}
	
}
