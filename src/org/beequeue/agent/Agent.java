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
package org.beequeue.agent;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.util.Strings;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.cmd.Shell;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Agent {
	public final static ObjectMapper om = new ObjectMapper();
	
	private static final String KILL_CMD = "kill:";
	private static final String PS_CMD = "ps";
	private static final String CPU_CMD = "cpu";
	private static final String MEM_CMD = "mem";

	public static void main(String[] args) {
		System.exit(new Agent(args).run());
	}

	final public List<String> argsList ;
	final public Shell shell = new Shell();
	final public Sigar sigar = shell.getSigar();
    final public SigarProxy proxy = shell.getSigarProxy();
    public File outputDirectory;

	public String timestamp;
    
    public Agent(String... args) {
    	this.argsList = new ArrayList<String>(Arrays.asList(args));
	}


	public int run() {
		outputDirectory = BeeQueueHome.instance.getHost();
		if(!outputDirectory.isDirectory() && !outputDirectory.mkdirs() ){
			System.err.println("Cannot use or create "+ outputDirectory  );
			return -1;
		}
		this.timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		int rc = 0;
		for (String command : argsList) {
			try{
				if(command.equals(PS_CMD)){
					rc |= PsCommand.go(this);
				}else if(command.equals(CPU_CMD)){
					rc |= CpuCommand.go(this);
				}else if(command.equals(MEM_CMD)){
					rc |= MemCommand.go(this);
				}else if(command.startsWith(KILL_CMD)){
					rc |= KillCommand.go(this, command.substring(KILL_CMD.length()).split(","));
				}
			}catch (Exception e) {
				e.printStackTrace();
				rc |= -1;
			}
			
		}
		return rc;
		
	}


	public void dump(String metricName, Object systemMetric) throws IOException {
		File f = outputDirectory;
		f = new File(f, metricName); if( !f.isDirectory() && !f.mkdirs() ) throw new FileNotFoundException(f.toString());
		File dumpFile = new File(f,timestamp+".txt");
		FileWriter w = new FileWriter(dumpFile);
		System.out.println("@Artifact: "+metricName+": "+dumpFile);
		ObjectWriter pp = om.writerWithDefaultPrettyPrinter();
		w.write(pp.writeValueAsString(systemMetric));
		w.close();
	}


}
