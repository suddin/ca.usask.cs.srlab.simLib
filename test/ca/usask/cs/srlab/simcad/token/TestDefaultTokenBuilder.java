package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.token.filter.ITokenFilter;
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
	
	static CloneFragment cloneFragment1;
	static CloneFragment cloneFragment2;
	
	static DetectionSettings detectionSettings1;
	static DetectionSettings detectionSettings2;
	
	static IgnoreTokenByPatternFilter filter_1;
	static IgnoreTokenByPatternFilter filter_11;
	static StripTokenByPatternFilter filter_12;
	static StripTokenByPatternFilter filter_13;
	
	@BeforeClass
	public static void setup(){
		detectionSettings1 = new DetectionSettings(0, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		detectionSettings2 = new DetectionSettings(12, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1, function1, 1234, 5678);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function2, function2, 4321, 8765);
		
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
	
	//@Test
	public void testGenerateTokenWithSettings1(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings1);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
		
	}
	
	@Test
	public void testGenerateTokenWithSettings2(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings2);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
		
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndSingleFilter(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings2);
		tokenBuilder.setFilterChain(filter_1);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndSingleFilter11(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings2);
		tokenBuilder.setFilterChain(filter_11);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGenerateTokenWithSettings2AndMultipleFilter1(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings2);
		tokenBuilder.setFilterChain(filter_12);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	//@Test
	public void testGenerateTokenWithSettings2AndMultipleFilter2(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder(detectionSettings2);
		tokenBuilder.setFilterChain(filter_13);
		Collection<String> result = tokenBuilder.generateToken(cloneFragment1);
		for (String string : result) {
			System.out.println(string);
		}
	}
}
