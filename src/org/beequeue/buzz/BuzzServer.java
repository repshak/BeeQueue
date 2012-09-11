package org.beequeue.buzz;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class BuzzServer {
	private int port ;
	private File root;
	private Server server;
	


	public BuzzServer(int port) {
		super();
		this.port = port;
	}


	public File getRoot() {
		return root;
	}


	public void setRoot(File root) {
		this.root = root;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
	public void start(){
		this.server = new Server(port);
	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] { new BuzzHandler(root) });
	    this.server.setHandler(handlers);
		try {
			this.server.start();
			this.server.join();
		} catch (Throwable e) {
			throw new BeeException(e);
		}
	}




  public static void processError(BuzzContext ctx, int statusCode, String statusMessage,
		String message, String details, String moreInfo) throws IOException {
	Map<String,Object> object = new LinkedHashMap<String, Object>();
	object.put("status", statusCode );
	object.put("statusMessage", statusMessage );
	object.put("message", message );
	object.put("details", details );
	object.put("moreInfo", moreInfo );
	ctx.res.setStatus(statusCode);
	ctx.res.setContentType("text/plain");
	ctx.res.getOutputStream().print(ToStringUtil.toString(object));
	ctx.r.setHandled(true);
  }

}
