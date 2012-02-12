package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import ca.usask.cs.srlab.simcad.SimcadException;

public class GenericSourceTransformer implements
		IXMLFragmentDataProviderTransformer {

	@Override
	public String transform(Object sourceFileName) {
		String newFileName = ((String) sourceFileName).subSequence(0, ((String) sourceFileName).lastIndexOf('.'))+ ".new";
		File newf = new File(newFileName);
		
		if (newf.exists())
			newf.delete();
		
		try{
			BufferedReader input = new BufferedReader(new FileReader((String) sourceFileName));
			PrintWriter output = new PrintWriter(new FileWriter(newFileName));
			
			String line;
	
			while ((line = input.readLine()) != null) {
				String changedLine = line;
				output.println(changedLine);
			}
			
			input.close();
			output.close();
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new SimcadException("unable to fix xml :" + sourceFileName, e);
		}
		return newFileName;
	}

}
