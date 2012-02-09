package ca.usask.cs.srlab.simcad.token.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class IgnoreTokenByPatternFilter extends AbstractTokenFilter {

	protected static List<Pattern> patternListToIgnore = new ArrayList<Pattern>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private IgnoreTokenByPatternFilter() {
	}
	
	private IgnoreTokenByPatternFilter(ITokenFilter targetFilter){
		super(targetFilter);
	}
	
	@Override
	protected String doFilter(String token) {
		for (Pattern pattern : patternListToIgnore) {
			if(pattern.matcher(token).matches())
				return null;
		}
		return token;
	}
	
	public static final class FilterBuilder{
		
		public IgnoreTokenByPatternFilter build(){
			return new IgnoreTokenByPatternFilter();
		}
		
		public IgnoreTokenByPatternFilter build(ITokenFilter targetFilter){
			return new IgnoreTokenByPatternFilter(targetFilter);
		}
		
		public FilterBuilder addPattern(String pattern){
			patternListToIgnore.add(Pattern.compile(pattern));
			return this;
		}
	}
	
}
