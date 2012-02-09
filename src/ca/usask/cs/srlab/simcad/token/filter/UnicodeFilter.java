package ca.usask.cs.srlab.simcad.token.filter;


public final class UnicodeFilter extends AbstractTokenFilter{

	public static FilterBuilder getFilterBuilder(){
		return new FilterBuilder();
	}
	
	private UnicodeFilter() {
	}
	
	private UnicodeFilter(ITokenFilter targetFilter) {
		super(targetFilter);
	}
	
	@Override
	protected String doFilter(String token) {
		StringBuilder sb = new StringBuilder(token);
		int index = 0;
		for(Character c : sb.toString().toCharArray()){
			if(Character.getType(c) == Character.CONTROL)
				sb.deleteCharAt(index);
			else
				index++;
		}
		return sb.toString();
	}
	
	public static final class FilterBuilder{
		
		public UnicodeFilter build(){
			return new UnicodeFilter();
		}
		
		public UnicodeFilter build(ITokenFilter targetFilter){
			return new UnicodeFilter(targetFilter);
		}
		
	}
}
