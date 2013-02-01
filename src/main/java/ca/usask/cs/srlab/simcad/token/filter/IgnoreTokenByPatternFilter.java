package ca.usask.cs.srlab.simcad.token.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class IgnoreTokenByPatternFilter extends AbstractTokenFilter {

	private List<Pattern> patternListToIgnore = new ArrayList<Pattern>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private IgnoreTokenByPatternFilter( List<Pattern> patternListToIgnore) {
		this.patternListToIgnore = patternListToIgnore;
	}
	
	private IgnoreTokenByPatternFilter(ITokenFilter targetFilter,  List<Pattern> patternListToIgnore){
		super(targetFilter);
		this.patternListToIgnore = patternListToIgnore;
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
		List<Pattern> patternList = new ArrayList<Pattern>();
		
		public IgnoreTokenByPatternFilter build(){
			return new IgnoreTokenByPatternFilter(patternList);
		}
		
		public IgnoreTokenByPatternFilter build(ITokenFilter targetFilter){
			return new IgnoreTokenByPatternFilter(targetFilter, patternList);
		}
		
		public FilterBuilder addPattern(String pattern){
			patternList.add(Pattern.compile(pattern));
			return this;
		}
	}
	
}
