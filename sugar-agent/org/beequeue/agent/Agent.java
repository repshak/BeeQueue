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

import org.beequeue.util.Strings;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.cmd.Shell;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Agent {
	private static final String HOST_DIAG_DIR = "HOST_DIAG_DIR";

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

	public String host;
	public String timestamp;
    
    public Agent(String[] args) {
    	this.argsList = new ArrayList<String>(Arrays.asList(args));
	}


	private int run() {
		String hostDiagnosticDirectory = System.getenv(HOST_DIAG_DIR);
		if(Strings.isEmpty(hostDiagnosticDirectory)) {
			System.err.println("Enironment "+HOST_DIAG_DIR + " is not set.");
			return -1;
		}
		outputDirectory = new File(hostDiagnosticDirectory);
		if(!outputDirectory.isDirectory() && !outputDirectory.mkdirs() ){
			System.err.println("Cannot use or create "+ outputDirectory  );
			return -1;
		}
		try {
			this.timestamp = new SimpleDateFormat("YYYYMMDDhhmmss").format(new Date());
			this.host = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return -1;
		}
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
		f = new File(f, this.host); if( !f.isDirectory() && !f.mkdirs() ) throw new FileNotFoundException(f.toString());
		FileWriter w = new FileWriter(new File(f,timestamp+".txt"));
		ObjectWriter pp = om.writerWithDefaultPrettyPrinter();
		w.write(pp.writeValueAsString(systemMetric));
		w.close();
	}


}
