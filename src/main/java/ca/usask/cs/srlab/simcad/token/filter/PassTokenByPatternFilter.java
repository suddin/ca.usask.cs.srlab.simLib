package ca.usask.cs.srlab.simcad.token.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class PassTokenByPatternFilter extends AbstractTokenFilter {

	private List<Pattern> patternListToIgnore = new ArrayList<Pattern>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private PassTokenByPatternFilter( List<Pattern> patternListToIgnore) {
		this.patternListToIgnore = patternListToIgnore;
	}
	
	private PassTokenByPatternFilter(ITokenFilter targetFilter,  List<Pattern> patternListToIgnore){
		super(targetFilter);
		this.patternListToIgnore = patternListToIgnore;
	}
	
	@Override
	protected String doFilter(String token) {
		for (Pattern pattern : patternListToIgnore) {
			if(pattern.matcher(token).matches())
				return token;
		}
		return null;
	}
	
	public static final class FilterBuilder{
		List<Pattern> patternList = new ArrayList<Pattern>();
		
		public PassTokenByPatternFilter build(){
			return new PassTokenByPatternFilter(patternList);
		}
		
		public PassTokenByPatternFilter build(ITokenFilter targetFilter){
			return new PassTokenByPatternFilter(targetFilter, patternList);
		}
		
		public FilterBuilder addPattern(String pattern){
			patternList.add(Pattern.compile(pattern));
			return this;
		}
	}
	
}
