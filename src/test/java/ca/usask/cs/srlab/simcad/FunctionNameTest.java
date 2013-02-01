package ca.usask.cs.srlab.simcad;

public class FunctionNameTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(getFunctionName("static String getFunctionName(String codeBlock){"));
		System.out.println(getFunctionName("static String getFunctionName ( String codeBlock){"));
		System.out.println(getFunctionName("static String getFunctionName  (String codeBlock) {"));
		System.out.println(getFunctionName("static String getFunctionName  (String codeBlock) \n{"));
		System.out.println(getFunctionName("static String getFunctionName  (String codeBlock, int fwwqfw, \nString fasda) \n{"));
		System.out.println(getFunctionName("static String getFunctionName \n(String codeBlock){"));
		
	}

	
	static String getFunctionName(String codeBlock) {
		int function_end = codeBlock.lastIndexOf(")") + 1;
		int bracket_start = codeBlock.lastIndexOf("(");
		int function_start = 0;
		boolean overTheName = false;
		for (int i = bracket_start - 1; i > 0; i--) {
			if (!overTheName && !Character.isWhitespace(codeBlock.charAt(i))) {
				overTheName = true;
				continue;
			}

			if (overTheName == true
					&& Character.isWhitespace(codeBlock.charAt(i))) {
				function_start = i;
				break;
			}
		}
		return codeBlock.substring(function_start, function_end).replace("\n", "");
	}
	
}
