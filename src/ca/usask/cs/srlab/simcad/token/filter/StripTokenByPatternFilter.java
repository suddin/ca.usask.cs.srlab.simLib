package ca.usask.cs.srlab.simcad.token.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StripTokenByPatternFilter extends AbstractTokenFilter{

	protected static List<Pattern> patternListToStrip = new ArrayList<Pattern>();
	
	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private StripTokenByPatternFilter() {
	}
	
	private StripTokenByPatternFilter(ITokenFilter targetFilter) {
		super(targetFilter);
	}
	
	@Override
	protected String doFilter(String token) {
		for(Pattern pattern : patternListToStrip){
			Matcher m = pattern.matcher(token);
			if(m.find()){
				return m.group();
			}
		}
		return token;
	}
	
	public static final class FilterBuilder{
		
		public StripTokenByPatternFilter build(){
			return new StripTokenByPatternFilter();
		}
		
		public StripTokenByPatternFilter build(ITokenFilter targetFilter){
			return new StripTokenByPatternFilter(targetFilter);
		}
		
		public FilterBuilder addPattern(String pattern){
			patternListToStrip.add(Pattern.compile(pattern));
			return this;
		}
	}
	
	/*
	//test code backup
	public static String doDataRerinement(String rawData) {
		String lines [] = rawData.split("\n");
		StringBuffer sb = new StringBuffer();
		String REGEX = "(?<=:)[^/]++";
		Pattern p = Pattern.compile(REGEX);
		
		for(String ln : lines){
			if(ln.length() > 0){
	              Matcher m = p.matcher(ln); // get a matcher object
	              if(m.find()) {
	                  sb.append(m.group().trim()+"\n");
	              }
			}
		}
		return sb.toString();
	}

	public static String doLineRerinement(String rawData) {
		String REGEX = "(?<=:)[^/]++";
		Pattern p = Pattern.compile(REGEX);
	    Matcher m = p.matcher(rawData); // get a matcher object
	    if(m.find()) {
	    	return (m.group().trim()+"\n");
	    }
		return "";
	}
	 */
}
