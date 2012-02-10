package ca.usask.cs.srlab.simcad.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.postprocess.DetectionSettings;
import ca.usask.cs.srlab.simcad.token.filter.ITokenFilter;
import ca.usask.cs.srlab.simcad.token.filter.NullFilter;

public final class DefaultTokenBuilder implements ITokenBuilder {

	private DetectionSettings detectionSettings;
	
	private ITokenFilter tokenFilterChain = NullFilter.INSTANCE; 

	@SuppressWarnings("unused")
	private DefaultTokenBuilder() {
	}

	public DefaultTokenBuilder(DetectionSettings detectionSettings) {
		super();
		this.detectionSettings = detectionSettings;
	}

	@Override
	public void setFilterChain(ITokenFilter tokenFilter){
		tokenFilterChain = tokenFilter;
	}
	
/* (non-Javadoc)
 * @see ca.usask.cs.srlab.simcad.hash.ITokenBuilder#generateToken(java.lang.String)
 */
@Override
public Collection<String> generateToken(CloneFragment cloneFragment) {
	
	List<String> tokenList = new ArrayList<String>();
	
	boolean fineGrainedToken = detectionSettings.getSimThreshold() > 0 ? true: false;
	
	String tokenSeparator = fineGrainedToken && cloneFragment.getLineOfCode() < 100 ? " \t\n\r\f":"\n";  //if block is big, split with line only!
	
	StringTokenizer stToken = new StringTokenizer(cloneFragment.getTransformedCodeBlock(), tokenSeparator);
	
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
//System.out.println(subToken);
						addToken(subToken, tokenList);
			    	}else{  //if string is greater than 10, break it more 
			    		String[] subSubTokens = subToken.split ("(?=[>])"); //split by camel case NOTE: removed split by _
						for(String subSubToken : subSubTokens){
//System.out.println(subToken);
							addToken(subSubToken, tokenList);
					    }
			    	}
			    }
	    	}else if(superToken.length() <= 10){
//System.out.println(superToken);
	    		addToken(superToken, tokenList);
	    	}else{  //if string is greater than 7, break it more 
				
				String[] subParts; 
				if(superToken.matches("[A-Z0-9|_|-|\\W]+")){ //uppercase constant declaration
					subParts = superToken.split ("(?=[_|.|>])"); //NOTE: removed split by _
				}else
					if(cloneFragment.getLineOfCode() < 7)
						subParts = superToken.split ("(?=[[A-Z]|_|.|>])"); //split by camel case. NOTE: removed split by [A-Z] _
					else
						subParts = superToken.split ("(?=[_|.|>])"); //split by camel case. NOTE: removed split by [A-Z] _
				
				for(String subToken : subParts){
//System.out.println("subToken);
					addToken(subToken, tokenList);
			    }
	    
	    	}
	    	//if(loc<6)
	    		//seconderyHashMinFreq = 0; //overriding the default one
    	}else{  
    		String modToken = superToken.trim() + lineNumberTag++ ; //to fix the bug of reordering
    		addToken(modToken, tokenList);
//TODO: seconderyHashMinFreq = 0; //overriding the default one
    	}
    }
	
    return tokenList;
}

private void addToken(String token, List<String> tokenList){
	String filteredToken = tokenFilterChain.doFilterAndInvokeNext(token);
	if(filteredToken != null)
		tokenList.add(filteredToken);
}




/*
 * Test code
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
