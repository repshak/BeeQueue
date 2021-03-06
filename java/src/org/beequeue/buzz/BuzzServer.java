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
package org.beequeue.buzz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.util.BeeException;
import org.beequeue.util.DevNullStream;
import org.beequeue.util.Streams;
import org.beequeue.util.Throwables;
import org.beequeue.util.ToStringUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;

public class BuzzServer {
	
	public static final String STATUS_MESSAGE = "StatusMessage";
	public static final String STATUS_CODE = "StatusCode";

	private static final String ROOT_KEY = "r";
	
	private static final String SHUT_DOWN_QUERY = "/SH/U7/d0/w4";
	public class ShutDownHandler extends AbstractHandler {
		@Override
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			if(target.equals(SHUT_DOWN_QUERY)){
				System.out.println();
				System.exit(0);
			}

		}
	}

	private static void shutdownLocalhost(int port) {
		try {
			URL url = new URL("http://localhost:" + port + SHUT_DOWN_QUERY);
			InputStream in = url.openStream();
			Streams.copy(in, new DevNullStream());
		} catch (Exception e) {}
	}

	private int port ;
	private Map<String, ContentProvider > roots = new LinkedHashMap<String, ContentProvider>();
	private Server server;
	


	public BuzzServer(int port) {
		super();
		shutdownLocalhost(port);
		this.port = port;
	}




	public File getRoot() {
		return new File(roots.get(ROOT_KEY).getRoot());
	}


	public void setRoot(File root) {
		this.roots.put(ROOT_KEY, new FileContentProvider(root)) ;
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
	    handlers.setHandlers(new Handler[] { new ShutDownHandler(), new BuzzHandler(roots) });
	    this.server.setHandler(handlers);
		try {
			this.server.start();
			this.server.join();
		} catch (Throwable e) {
			throw new BeeException(e);
		}
	}




	public static void processError(BuzzContext ctx, BeeException e)
			throws IOException {
		int statusCode;
		String statMessage = null;
		try {
			Map<String, Object> vals = e.getMemoValues();
			statusCode = ((Number) vals.get(STATUS_CODE)).intValue();
			statMessage = (String) vals.get(STATUS_MESSAGE);
		} catch (Exception e1) {
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}

		Map<String, Object> object = new LinkedHashMap<String, Object>();
		object.put("status", statusCode);
		object.put("statusMessage", statMessage);
		object.put("stack", Throwables.toString(e));

		ctx.res.setStatus(statusCode);
		ctx.res.setContentType("text/plain");
		ctx.res.getOutputStream().print(ToStringUtil.toString(object));
		ctx.setHandled();
	}

	public static void setStatusCode(BeeException be, int statusCode) {
		be.memo(BuzzServer.STATUS_CODE, statusCode);
	}

}
