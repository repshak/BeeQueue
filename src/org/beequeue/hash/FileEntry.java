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
package org.beequeue.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.beequeue.util.Dirs;

import com.fasterxml.jackson.annotation.JsonValue;

public class FileEntry implements Comparable<FileEntry>{

	public final HashKey code;
	public final boolean executible;
	public final String path;
	public final String s;
	
	public FileEntry(HashKey code, boolean executible, String path) {
		this.code = code;
		this.executible = executible;
		this.path = path;
		this.s = "" + code + "," + (executible ? "x" : "-") + "," + path ;
	}
	
	public static FileEntry valueOf(String s){
		String[] split = s.split(",", 3);
		return new FileEntry( HashKey.valueOf( split[0]) , 
				split[1].equals("x") , split[2]  );
	}

	public HashInput input(File base) throws FileNotFoundException{
		File file = file(base);
		return new HashInput(this.code, new FileInputStream(file));
	}
	
	public HashOutput output(File base) throws FileNotFoundException{
		File file = file(base);
		Dirs.ensureParentDirExists(file);
		return new HashOutput(this.code, new FileOutputStream(file));
	}

	public File file(File base) {
		return new File(base,path);
	}
	
	@Override @JsonValue
	public String toString() {
		return s;
	}
	
	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEntry) {
			FileEntry that = (FileEntry) obj;
			return this.s.equals(that.s);
		}
		return false;
	}

	@Override
	public int compareTo(FileEntry that) {
		return this.s.compareTo(that.s);
	}

	public void updateAttributes(File writeTo) {
		if(executible){
			file(writeTo).setExecutable(true);
		}
	}

		
}
