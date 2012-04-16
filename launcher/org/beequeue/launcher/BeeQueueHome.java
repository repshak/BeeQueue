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

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.beequeue.launcher.JarUnpacker.EntryFilter;


public class BeeQueueHome {
	public static final String BQ_HOME = "BQ_HOME";
	
	public static final BeeQueueHome instance = new BeeQueueHome();
	
	private File home;
	
	private BeeQueueHome() {
		try{
			String homeEnv = System.getenv(BQ_HOME);
			if( homeEnv == null || homeEnv.trim().length()==0 ){
				home = new File( new File(".").getAbsoluteFile(), "bq-home" );
			}else{
				home = new File(homeEnv).getAbsoluteFile();
			}
			home = home.getCanonicalFile();
			if(!home.isDirectory() && !home.mkdirs()){
				throw new IOException("Cannot create:"+home);
			}
			EntryFilter ef = new EntryFilter() {
				
				@Override
				public boolean include(ZipEntry ze) {
					return ze.getName().startsWith("web");
				}
			};
			JarUnpacker.unpack(BeeQueueHome.class, ef, home);
		}catch (Exception e) {
			System.err.println("Cannot establish home directory: " + home+" error:" + e.toString());
			System.exit(-1);
		}
		
	}

	public File getHome() {
		return home;
	}
	
	
	
	
}
