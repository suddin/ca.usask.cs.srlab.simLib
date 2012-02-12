package ca.usask.cs.srlab.simcad.hash;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.usask.cs.srlab.simcad.Constants;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.model.FunctionCloneFragment;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
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
		detectionSettings1 = new DetectionSettings(0, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
		detectionSettings2 = new DetectionSettings(13, Constants.CLONE_GRANULARITY_FUNTIONS, Constants.CLONE_SET_TYPE_GROUP);
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
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1, function1, 0, 0);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type1, function1_type1, 0, 0);
		
		long sh1[] = simhashGenerator.generateSimhash(cloneFragment1);
		long sh2[] = simhashGenerator.generateSimhash(cloneFragment2);
		System.out.println(sh1[0]);
		System.out.println(sh2[0]);
		System.out.println(sh1[1]);
		System.out.println(sh2[1]);
		System.out.println("......");
		Assert.assertArrayEquals(sh1, sh2);
		
		switchToHighThresholdSettings();
		
		sh1 = simhashGenerator.generateSimhash(cloneFragment1);
		sh2 = simhashGenerator.generateSimhash(cloneFragment2);
		System.out.println(sh1[0]);
		System.out.println(sh2[0]);
		System.out.println(sh1[1]);
		System.out.println(sh2[1]);
		System.out.println("......");
		Assert.assertArrayEquals(sh1, sh2);
	}
	
	@Test
	public void testSimhashGenForNearSimilarType2CodeFragment(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1, function1, 0, 0);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type2, function1_type2, 0, 0);
		
		switchToHighThresholdSettings();
		
		long sh1[] = simhashGenerator.generateSimhash(cloneFragment1);
		long sh2[] = simhashGenerator.generateSimhash(cloneFragment2);
		
		System.out.println(sh1[0]);
		System.out.println(sh2[0]);
		System.out.println(sh1[1]);
		System.out.println(sh2[1]);
		System.out.println("......");
		Assert.assertTrue(sh1[0] != sh2[0] && sh1[1] != sh2[1]);
		
		System.out.println(hamming_distance(sh1[0], sh2[0]));
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) < 13);
		
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) < 13);
	}

	
	@Test
	public void testSimhashGenForNearSimilarType3CodeFragment2(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1, function1, 0, 0);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function1_type3, function1_type3, 0, 0);
		
		switchToHighThresholdSettings();
		
		long sh1[] = simhashGenerator.generateSimhash(cloneFragment1);
		long sh2[] = simhashGenerator.generateSimhash(cloneFragment2);
		
		System.out.println(sh1[0]);
		System.out.println(sh2[0]);
		System.out.println(sh1[1]);
		System.out.println(sh2[1]);
		System.out.println("......");
		Assert.assertTrue(sh1[0] != sh2[0] && sh1[1] != sh2[1]);
		
		System.out.println(hamming_distance(sh1[0], sh2[0]));
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) <= 13);
		
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) <= 13);
	}
	
	@Test
	public void testSimhashGenForDissimilarCodeFragment(){
		
		cloneFragment1 = new FunctionCloneFragment("fileName1", 11, 22, 1, function1, function1, 0, 0);
		cloneFragment2 = new FunctionCloneFragment("fileName2", 34, 55, 1, function2, function2, 0, 0);
		
		switchToHighThresholdSettings();
		
		long sh1[] = simhashGenerator.generateSimhash(cloneFragment1);
		long sh2[] = simhashGenerator.generateSimhash(cloneFragment2);
		
		System.out.println(sh1[0]);
		System.out.println(sh2[0]);
		System.out.println(sh1[1]);
		System.out.println(sh2[1]);
		System.out.println("......");
		Assert.assertTrue(sh1[0] != sh2[0] && sh1[1] != sh2[1]);
		
		System.out.println(hamming_distance(sh1[0], sh2[0]));
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) > 12);
		
		Assert.assertTrue(hamming_distance(sh1[0], sh2[0]) > 12);
	}
	
	private long hamming_distance(long l, long r) {
		return Long.bitCount(l^r);
	}
	
}
