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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.agent.Agent;
import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.Streams;
import org.beequeue.util.ToStringUtil;

public class WorkerServlet  extends HttpServlet {

	private static final Agent AGENT = new Agent();

	private static final long serialVersionUID = 1L;

	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() { 
				try{
					TransactionContext.push();
					/* Check if host object initialized and if not make sure that 
					 * host/group exists in db */
					Coordiantor coordinator = Singletons.getCoordinator();
					coordinator.ensureHost(WorkerData.instance);
					
					/* Check CPU & Memory and up update 
					 * statistics */
					AGENT.runStatistics();
					coordinator.storeStatistics(WorkerData.instance);
					
					/* Check all active workers in the same group,
					 * calculate next beat time
					 * Update status of the workers and beet time 
					 * on the same machine if necessary.
					 */
					coordinator.ensureWorker(WorkerData.instance);
					
					/* Process all messages and create stages, create runs for 
					 * unblocked stages. Pick runs to execute. Execute.
					 */

					/* Run ps. Identify all processes that currently executed.
					 * Check all processes that finished on host an update ther stages 
					 * appropriately
					 */
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					TransactionContext.pop();
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		String q = request.getPathInfo();
		String ctx = request.getServletPath();
		try{
			TransactionContext.push();
			if( ctx.equals("/host") ){
				File host = BeeQueueHome.instance.getHost();
				dumpDataFromFileSystem(host, q, out);
			}else if( ctx.equals("/home") ){
				File home = BeeQueueHome.instance.getHome();
				dumpDataFromFileSystem(home, q, out);
			}else if( ctx.equals("/db") ){
				Coordiantor c = Singletons.getCoordinator();
				out.println(c.selectAnyTable(q));
			}else if( ctx.equals("/query") ){
				Coordiantor c = Singletons.getCoordinator();
				out.println(c.query(q));
			}
		}catch (Exception e) {
			throw new ServletException(e);
		}finally{
			TransactionContext.pop();
		}
		
	}





	public void dumpDataFromFileSystem(File base, String query, PrintWriter out)
			throws IOException, FileNotFoundException {
		if( query != null && !query.equals("") && !query.equals("/") ){
			base = new File(base,query.substring(1));
		}
		if( base.exists() ){
			if(base.isDirectory() ){
				out.println(ToStringUtil.toString(base.list()));
			}else{
				Streams.copyAndClose(new FileReader(base), out);
			}
		}
	}



	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}



}
