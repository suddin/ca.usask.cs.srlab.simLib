package ca.usask.cs.srlab.simcad.token;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTokenBuilder implements Iterable<String>, ITokenBuilder {

	private TokenBuildStrategy buildStrategy;

	@SuppressWarnings("unused")
	private DefaultTokenBuilder() {
	}

	public DefaultTokenBuilder(TokenBuildStrategy strategy) {
		super();
		this.buildStrategy = strategy;
	}

	@Override
	public void setBuildStrategy(TokenBuildStrategy strategy) {
		this.buildStrategy = strategy;
	}

	@Override
	public TokenBuildStrategy getBuildStrategy() {
		return buildStrategy;
	}
  	
  LinkedList<String> tokenQueue = new LinkedList<String>();


  /**
   * Retrieves, but does not remove, token from this queue.
   * 
   * @return token from this queue, or <tt>null</tt> if this queue is empty.
   */
  public String peek() {
    return tokenQueue.peek();
  }

  /**
   * Retrieves and removes token from this queue.
   * 
   * @return token from this queue, or <tt>null</tt> if this queue is empty.
   */
  public String poll() {
    return tokenQueue.poll();
  }

  public int size() {
    return tokenQueue.size();
  }

  public void add(String token) {
    tokenQueue.addLast(token);
  }

  public boolean isNextStringValue(String expectedValue) {
    String nextToken = tokenQueue.peek();
    if (nextToken == null) {
      // queue is empty
      return false;
    }
    return nextToken.equals(expectedValue);
  }

  public Iterator<String> iterator() {
    return tokenQueue.iterator();
  }

  public void pushForward(List<String> matchedStringList) {
    ListIterator<String> iter = matchedStringList.listIterator(matchedStringList.size());
    while (iter.hasPrevious()) {
      tokenQueue.addFirst(iter.previous());
    }
  }


/* (non-Javadoc)
 * @see ca.usask.cs.srlab.simcad.hash.ITokenBuilder#generateToken(java.lang.String)
 */
@Override
public Collection<String> generateToken(String rawData) {
	// TODO Auto-generated method stub
	return tokenQueue;
}


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


/*


//Map<String, Short> tokenMap = new 
//TreeMap<String, Short>();

if(method == 1){
	String[] parts = rawData.split("\\W+");//"(?<=\\G...)");
	for(String token : parts){
    	short num = tokenMap.get(token) == null ? 0 : (short)(tokenMap.get(token));
		tokenMap.put(token, ++num);
    }
}
else if(method == 2){
	String[] parts = rawData.split("(?<=\\G...)");
	for(String token : parts){
    	short num = tokenMap.get(token) == null ? 0 : (short)(tokenMap.get(token));
		tokenMap.put(token, ++num);
    }
}if(method == 3){
	
	String tokenSeparator;
	tokenSeparator = fineGrainedToken && loc < 100 ? " \t\n\r\f":"\n";  //if block is big, split with line only!
	
	StringTokenizer stToken = new StringTokenizer(rawData, tokenSeparator);
	
	int lineNumberTag = 1234; //any number to start with  
	
    while(stToken.hasMoreElements()) {
    	
    	String superToken = stToken.nextToken();
    	//if(superToken.length() == 0){System.out.println("################gotcha!##############");continue;}
    	
    	if(fineGrainedToken){
    		//superToken.length() > 13 && 
	    	if(superToken.contains(".")){
	    		String[] subParts = superToken.split ("(?=[.])"); //split by .
				for(String subToken : subParts){
					if(subToken.length() <= 10){
			    		short num = tokenMap.get(subToken) == null ? 0 : (short)(tokenMap.get(subToken));
//System.out.println(subToken);
			    		tokenMap.put(subToken, ++num);
			    	}else{  //if string is greater than 10, break it more 
			    		String[] subSubTokens = subToken.split ("(?=[>])"); //split by camel case NOTE: removed split by _
						for(String subSubToken : subSubTokens){
					    	short num = tokenMap.get(subSubToken) == null ? 0 : (short)(tokenMap.get(subSubToken));
//System.out.println(subToken);
							tokenMap.put(subSubToken, ++num);
					    }
			    	}
			    }
	    	}else if(superToken.length() <= 10){
	    		short num = tokenMap.get(superToken) == null ? 0 : (short)(tokenMap.get(superToken));
//System.out.println(superToken);
	    		tokenMap.put(superToken, ++num);
	    	}else{  //if string is greater than 7, break it more 
				
				String[] subParts; 
				if(superToken.matches("[A-Z0-9|_|-|\\W]+")){ //uppercase constant declaration
					subParts = superToken.split ("(?=[_|.|>])"); //NOTE: removed split by _
				}else
					if(loc < 7)
						subParts = superToken.split ("(?=[[A-Z]|_|.|>])"); //split by camel case. NOTE: removed split by [A-Z] _
					else
						subParts = superToken.split ("(?=[_|.|>])"); //split by camel case. NOTE: removed split by [A-Z] _
				
				for(String subToken : subParts){
			    	short num = tokenMap.get(subToken) == null ? 0 : (short)(tokenMap.get(subToken));
//System.out.println("subToken);
					tokenMap.put(subToken, ++num);
			    }
	    
	    	}
	    	//if(loc<6)
	    		//seconderyHashMinFreq = 0; //overriding the default one
    	}else{  
    		String modToken = superToken.trim() + lineNumberTag++ ; //to fix the bug of reordering
    		short num = tokenMap.get(modToken) == null ? 0 : (short)(tokenMap.get(modToken));
	        tokenMap.put(modToken, ++num);
	        seconderyHashMinFreq = 0; //overriding the default one
    	}
    }
}else if(method == 4){
	
//	String tokenSeparator;
//	tokenSeparator = "\n\r\f";  //if block is big, split with line only!
//	
//	StringTokenizer stToken = new StringTokenizer(refinedData, tokenSeparator);
//	
	//String refinedData = doDataRerinement(rawData);
	
	String lines[] = rawData.split("\n");
	int lineNumberTag = 1010;
    //while(stToken.hasMoreElements()) {
    for(String superToken:lines){
    	superToken = doLineRerinement(superToken);
    	if(superToken.length() == 0) continue;
		//String superToken = stToken.nextToken();
		String modToken = superToken.trim() + lineNumberTag++; 
		short num = tokenMap.get(modToken) == null ? 0 : (short)(tokenMap.get(modToken));
        tokenMap.put(modToken, ++num);
	}
	
}else if(method == 5){
	
	//String refinedData = doDataRerinement(rawData);
	
	String lines[] = rawData.split("\n");
	int lineNumberTag = 1010;
    //while(stToken.hasMoreElements()) {
    for(String superToken:lines){
    	if(superToken.length() == 0) continue;
		//String superToken = stToken.nextToken();
    	String subTokens[] = superToken.split("//");
    	for(String subToken : subTokens){
    		String modToken = null;
    		if(subToken.startsWith(":")){
    			modToken = subToken.substring(1).trim() + lineNumberTag++; 
    		}else
    			modToken = subToken.trim();
			short num = tokenMap.get(modToken) == null ? 0 : (short)(tokenMap.get(modToken));
        	tokenMap.put(modToken, ++num);
    	}
	}
}
*/

}
