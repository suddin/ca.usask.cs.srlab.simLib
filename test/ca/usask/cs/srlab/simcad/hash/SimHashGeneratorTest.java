package ca.usask.cs.srlab.simcad.hash;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.token.ITokenBuilder;
import ca.usask.cs.srlab.simcad.token.TokenBuilderFactory;


public class SimHashGeneratorTest {

	static SimhashGenerator simhashGenerator;
	static ITokenBuilder tokenBuilder;
	static IRegularHashGenerator regularHashGenerator;
	
	static DetectionSettings detectionSettings1;
	static DetectionSettings detectionSettings2;
	
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

	static String function1_type1 = 
		"protected String doFilter(String token) {\n"+
			"for(Pattern pattern : patternListToStrip){\n"+
				"Matcher m = pattern.matcher(token);\n"+
				"if(m.find()){\n"+
					"return m.group();\n"+
				"}\n"+
			"}\n"+
			"return token;\n"+
		"}";
	
	static String function1_type2 = 
		"protected String doFilter(String token) {\n"+
			"for(Pattern p : patternListToStrip){\n"+
				"Matcher m = p.matcher(token);\n"+
				"if(m.find()){\n"+
					"return m.group();\n"+
				"}\n"+
			"}\n"+
			"return token;\n"+
		"}";
	
	static String function1_type3 = 
		"protected String doFilter(String token) {\n"+
			"for(Pattern p : patternListToStrip){\n"+
				"Matcher m = p.matcher(token);\n"+
				"bpplean matchFound = m.find();\n"+
				"if(matchFound){\n"+
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
	
	CloneFragment cloneFragment1;
	CloneFragment cloneFragment2;
	
	@BeforeClass
	public static void setup(){
		detectionSettings1 = new DetectionSettings(Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP, Constants.CLONE_TYPE_1);
		detectionSettings2 = new DetectionSettings(Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP, Constants.CLONE_TYPE_1,  Constants.CLONE_TYPE_2,  Constants.CLONE_TYPE_3);
	}
	
	@Before
	public void subSetup(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		regularHashGenerator = HashGeneratorFactory.LoadRegularHashgenerator();
		simhashGenerator = new SimhashGenerator(tokenBuilder, regularHashGenerator);
	}
	
	private void switchToHighThresholdSettings(){
		tokenBuilder = TokenBuilderFactory.LoadTokenBuilder();
		simhashGenerator.updateTokebBuilder(tokenBuilder);
	}
	
	@Test
	public void testSimhashGenForSimilarCodeFragment(){
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type1);
		
		simhashGenerator.updateSimhash(cloneFragment1, function1);
		simhashGenerator.updateSimhash(cloneFragment2, function1_type1);
		
		Assert.assertEquals(cloneFragment1.getSimhash1(), cloneFragment2.getSimhash1());
		Assert.assertEquals(cloneFragment1.getSimhash2(), cloneFragment2.getSimhash2());
	}
	
	@Test
	public void testSimhashGenForNearSimilarType2CodeFragment(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type2);
		
		simhashGenerator.updateSimhash(cloneFragment1, function1);
		simhashGenerator.updateSimhash(cloneFragment2, function1_type2);
		
		Assert.assertTrue(cloneFragment1.getSimhash1() != cloneFragment2.getSimhash1() && cloneFragment1.getSimhash2() != cloneFragment2.getSimhash2());
		Assert.assertTrue(hamming_distance(cloneFragment1.getSimhash1(), cloneFragment2.getSimhash1()) < 13);
	}

	
	@Test
	public void testSimhashGenForNearSimilarType3CodeFragment2(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type3);
		
		simhashGenerator.updateSimhash(cloneFragment1, function1);
		simhashGenerator.updateSimhash(cloneFragment2, function1_type3);
		
		Assert.assertTrue(cloneFragment1.getSimhash1() != cloneFragment2.getSimhash1() && cloneFragment1.getSimhash2() != cloneFragment2.getSimhash2());
		Assert.assertTrue(hamming_distance(cloneFragment1.getSimhash1(), cloneFragment2.getSimhash1()) <= 13);
		Assert.assertTrue(hamming_distance(cloneFragment1.getSimhash2(), cloneFragment2.getSimhash2()) <= 13);
	}
	
	@Test
	public void testSimhashGenForDissimilarCodeFragment(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function2);
		
		simhashGenerator.updateSimhash(cloneFragment1, function1);
		simhashGenerator.updateSimhash(cloneFragment2, function2);
		
		Assert.assertTrue(cloneFragment1.getSimhash1() != cloneFragment2.getSimhash1() && cloneFragment1.getSimhash2() != cloneFragment2.getSimhash2());
		Assert.assertTrue(hamming_distance(cloneFragment1.getSimhash1(), cloneFragment2.getSimhash1()) > 12);
		Assert.assertTrue(hamming_distance(cloneFragment1.getSimhash2(), cloneFragment2.getSimhash2()) > 12);
	}
	
	private long hamming_distance(long l, long r) {
		return Long.bitCount(l^r);
	}
	
}
