package org.beequeue.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeeQueueCommandLineInterface {
	private Entry[] entries;
	private String[] description=null;
	
	public BeeQueueCommandLineInterface description(String ... description){
		this.description = description;
		return this;
	}
	
	public BeeQueueCommandLineInterface(Entry... entries) {
		this.entries = entries;
	}

	public static abstract class Entry {
		public Entry(Pattern pattern, String... description) {
			super();
			this.pattern = pattern;
			this.description = description;
		}
		public Entry(String pattern, String... description) {
			this.pattern = Pattern.compile(pattern);
			this.description = description;
		}
		Pattern pattern;
		String[] description;
	
		boolean process(String arg, List<String> rest, BeeQueueCommandLineInterface cli){
			Matcher m = pattern.matcher(arg);
			boolean matches = m.matches();
			if(matches){
				extract(m,rest,cli);
			}
			return matches;
		}
		abstract void extract(Matcher m, List<String> rest, BeeQueueCommandLineInterface cli);
	}

	public List<String> process(String[] args){
		List<String> argList = new ArrayList<String>(Arrays.asList(args));
		if(args.length > 0) {
			String command = argList.remove(0);
			for (int i = 0; i < entries.length; i++) {
				Entry entry = entries[i];
				if(entry.process(command, argList,this)){
					break;
				}
			}
			if(printHelp) printHelp();
		}
		return argList ;
	}
	public boolean printHelp = false ;
	public void printHelp(){
		if(description!=null){
			for (int j = 0; j < description.length; j++) {
				String l = description[j];
				System.out.println(l);
			}
		}
		for (int i = 0; i < entries.length; i++) {
			Entry e = entries[i];
			System.out.println("  "+e.pattern);
			for (int j = 0; j < e.description.length; j++) {
				String l = e.description[j];
				System.out.println("    "+l);
			}
			System.out.println();
		}
		System.exit(-1);
	}
}
