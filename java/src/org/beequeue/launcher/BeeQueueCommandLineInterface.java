/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
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
			argList.add(0,command);
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
