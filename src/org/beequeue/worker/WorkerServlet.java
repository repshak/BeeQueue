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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.JsonTable;
import org.beequeue.util.Streams;

public class WorkerServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	

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
				dumpDataFromFileSystem(host, "host/" , q, out);
			}else if( ctx.equals("/home") ){
				File home = BeeQueueHome.instance.getHome();
				dumpDataFromFileSystem(home, "home/" , q, out);
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





	public void dumpDataFromFileSystem(File base, String prefix, String query, PrintWriter out)
			throws IOException, FileNotFoundException {
	}



	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}



}
