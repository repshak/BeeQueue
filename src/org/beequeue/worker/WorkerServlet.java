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
package org.beequeue.worker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.agent.Agent;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.util.Streams;
import org.beequeue.util.ToStringUtil;

public class WorkerServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() { 
				new Agent(new String[]{ "ps", "mem", "cpu"}).run(); 
			}
		}, 0, 10, TimeUnit.MINUTES);
		System.out.println("static-init");
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("init");
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String q = request.getPathInfo();
		File f = BeeQueueHome.instance.getHost();
		if( q != null && !q.equals("") && !q.equals("/") ){
			f = new File(f,q.substring(1));
		}
		if( f.exists() ){
			response.setContentType("text/plain");
			if(f.isDirectory() ){
				out.println(ToStringUtil.toString(f.list()));
			}else{
				Streams.copyAndClose(new FileReader(f), out);
			}
		}
	}


	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		super.doHead(req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAny(req, resp);
	}


	protected void doAny(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
	}
	
	

}
