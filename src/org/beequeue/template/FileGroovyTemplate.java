package org.beequeue.template;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.beequeue.util.Tuple;
import org.beequeue.util.Tuples;
import org.codehaus.groovy.control.CompilationFailedException;

public class FileGroovyTemplate {
	GroovyTemplate fileName;
	GroovyTemplate body;
	
	  public Tuple<File,Boolean> generate(Map<String,?> context, File writeTo) throws IOException, CompilationFailedException, ClassNotFoundException {
		    String file = fileName.generate(context);
		    File fileToWriteTo = new File(file);
			boolean newContentGenerated = body.generate(context, fileToWriteTo);
			return Tuples.build(fileToWriteTo, newContentGenerated);
	  }
	
	
}
