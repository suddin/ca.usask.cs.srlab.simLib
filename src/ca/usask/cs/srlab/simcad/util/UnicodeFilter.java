package ca.usask.cs.srlab.simcad.util;

public class UnicodeFilter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "String ACTION";
		for(Character c : s.toCharArray()){
			System.out.println(c + ": "+ Character.getType(c));
		}
		
		System.out.println("------------");
		
		StringBuilder sb = new StringBuilder(s.trim());
		int index = 0;
		for(Character c : sb.toString().toCharArray()){
			if(Character.getType(c) == Character.CONTROL)
				sb.deleteCharAt(index);
			else
				index++;
		}
		
		for(Character c : sb.toString().toCharArray()){
			System.out.println(c + ": "+ Character.getType(c));
		}
		
	}

}
