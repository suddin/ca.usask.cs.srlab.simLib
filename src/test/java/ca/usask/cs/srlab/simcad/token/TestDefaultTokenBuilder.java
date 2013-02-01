package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.token.filter.IgnoreTokenByPatternFilter;
import ca.usask.cs.srlab.simcad.token.filter.StripTokenByPatternFilter;


public class TestDefaultTokenBuilder {

	static ITokenBuilder tokenBuilder;
	
	static String function1 = 
	"protected String doFilter(String token) {\n"+
		"for(Pattern pattern : patternListToStrip){\n"+
			"Matcher m = pattern.matcher(token);\n"+
			"if(m.find()){\n"+
				"return m.group();\n"+
			"}\n"+
		"}\n"+
		"return token;\n"+
	"}";
	
	static String function2 = 
	"public static String doLineRerinement(String rawData) {\n"+
		"String REGEX = \"(?<=:)[^/]++\";\n"+
		"Pattern p = Pattern.compile(REGEX);\n"+
	    "Matcher m = p.matcher(rawData);\n"+
	    "if(m.find()) {\n"+
	    	"return (m.group().trim()+\"\n\");\n"+
	    "}\n"+
		"return \"\";\n"+
	"}";
	
	static IgnoreTokenByPatternFilter filter_1;
	static IgnoreTokenByPatternFilter filter_11;
	static StripTokenByPatternFilter filter_12;
	static StripTokenByPatternFilter filter_13;
	
	@BeforeClass
	public static void setup(){
		
		filter_1 = IgnoreTokenByPatternFilter.getFilterBuilder().addPattern(".").build();
		filter_11 = IgnoreTokenByPatternFilter.getFilterBuilder().addPattern("\\}").addPattern("\\{").build();
		filter_12 = StripTokenByPatternFilter.getFilterBuilder().addPattern("[^)]++").build(filter_1);
		filter_13 = StripTokenByPatternFilter.getFilterBuilder().addPattern("[^{]++").addPattern("[^($]++").build(filter_1);
	}
	
	static int i = 0;
	@Before
	public void beforeTest(){
		System.out.println("---start test ("+ ++i +")---");
	}
	
	
	@After
	public void afterTest(){
		System.out.println("---end test ("+ i +")---\n");
	}
	
	@Test
	public void testGenerateTokenWithSettings2(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		Collection<String> result = tokenBuilder.generateToken(function1);
		for (String string : result) {
			System.out.println(string);
		}
		
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndSingleFilter(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		tokenBuilder.setFilterChain(filter_1);
		Collection<String> result = tokenBuilder.generateToken(function1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndSingleFilter11(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		tokenBuilder.setFilterChain(filter_11);
		Collection<String> result = tokenBuilder.generateToken(function1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndMultipleFilter1(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		tokenBuilder.setFilterChain(filter_12);
		Collection<String> result = tokenBuilder.generateToken(function1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	//@Test
	public void testGenerateTokenWithSettings2AndMultipleFilter2(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		tokenBuilder.setFilterChain(filter_13);
		Collection<String> result = tokenBuilder.generateToken(function1);
		for (String string : result) {
			System.out.println(string);
		}
	}
}
