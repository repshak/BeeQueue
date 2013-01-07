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
package org.beequeue.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utf8TextContent {
	public static final Charset UTF8 = Charset.forName("UTF-8");
	final public int maxSize;
	private int size = 0;
	private List<String> buffer = new ArrayList<String>();

	public Utf8TextContent(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public Utf8TextContent(File inFile, int maxSize) {
		this.maxSize = maxSize;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile),UTF8));
			String row = null;
			while(null != (row = in.readLine())){
				int at = buffer.size();
				add(at, row.trim());
			}
		} catch (Exception e) {
			throw BeeException.cast(e);
		}finally{
			try { in.close(); }catch (Exception ignore) {}
		}
	}

	public boolean canAdd(){
		return size < maxSize;
	}
	
	public void set(int at, String s) {
		String z = buffer.get(at);
		buffer.set(at, s);
		size += s.length()-z.length();
	}
	
	public void add(int at, String s) {
		buffer.add(at, s);
		size += s.length()+1;
	}
	
	public String get(int at) {
		return buffer.get(at);
	}
	
	public String remove(int at) {
		String s = buffer.remove(at);
		size -= s.length()+1;
		return s;
	}
}
