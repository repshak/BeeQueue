package org.beequeue.command;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.launcher.BeeQueueJvmHelpeer;
import org.beequeue.launcher.BeeQueueJvmStatus;
import org.beequeue.util.ToStringUtil;


public class RunServer {
	public static void main(String[] args) {
		if( args.length == 1 ){
			try{
				makeSureThatPortAvailable();
				BeeQueueHome.instance.setCloudName(args[0]);
				BeeQueueHome.instance.runServer();
			}catch(Exception e){
				die(e.toString(),null);
			}
		}else{
			die("RunServer <cloud name>",null);
		}
 	}

	public static void die(String errText,Object o) {
		System.out.println(errText+ (o != null ? ToStringUtil.toString(o) : ""));
		System.exit(-1);
	}
	
	public static void makeSureThatPortAvailable() throws InterruptedException {
		File jvmCsv = BeeQueueHome.instance.jvmCsv(BeeQueueHome.instance.getPort());
		BeeQueueJvmHelpeer jvmStatus = new BeeQueueJvmHelpeer(BeeQueueHome.instance.getPort());
		if( jvmStatus.starting != null ){
			BeeQueueHome.die(null, "It's odd. Some worker already starting. Kill all workers running and delete file:"+ jvmCsv);
		}
		boolean portAvailable = false;
		for (int i = 0; i < 6; i++) {
			jvmStatus.ensureMeInList(BeeQueueHome.instance.getPort(), BeeQueueJvmStatus.STARTING);
			jvmStatus.write(BeeQueueHome.instance.getPort());
			if(portAvailable(BeeQueueHome.instance.getPort())){
				portAvailable = true;
				break;
			}
			Thread.sleep(15000L);
		}
		if(!portAvailable){
			portAvailable = killAllWorkersExceptItself();
		}
		if(!portAvailable){
			BeeQueueHome.die(null, "Gave up to get port. Kill process mannualy, also you may inspect file:"+ jvmCsv);
		}
	}

	public static boolean portAvailable(int port) {
		ServerSocket ss = null;
	    try {
			ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException ignore) {
	    } finally {
            try { ss.close(); } catch (Exception ignore) {}
	    }
	    return false;
	}

	private static boolean killAllWorkersExceptItself() {
		// TODO add smart killing logic
		//return portAvailable();
		return false;
		
	}


}
