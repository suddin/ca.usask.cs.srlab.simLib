package ca.usask.cs.srlab.simcad.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractStringByPattern {

  public static void main(String args[]) {
	  
	String s1 = ": new           #59                 // class java/awt/Insets";
	String s2 = ": aload         4";
	
	String s3 = ": getfield      #147                // Field entry:Lar/com/jkohen/awt/TextFieldHistory;\n" +
			": aload         5	\n" +
			": invokevirtual #79                 // Method java/awt/GridBagLayout.setConstraints:(Ljava/awt/Component;Ljava/awt/GridBagConstraints;)V \n" +
			": aload_0       ";
    
	Pattern pat = Pattern.compile("(?<=:)[^/]++");
	//Pattern pat = Pattern.compile("^:.+(?=//)");
	//Pattern pat = Pattern.compile("[^:]+(?!=//)");
	
    Matcher mat = pat.matcher(s1);

    while (mat.find())
      System.out.println("Match# " + mat.group());
  }
}