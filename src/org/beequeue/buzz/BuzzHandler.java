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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.util.Throwables;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

final class BuzzHandler extends AbstractHandler {
	public final File root;
	public final Map<BuzzPath, BuzzRcConfig> rcConfigs;

	public BuzzHandler(File root) {
		this.root = root;
		this.rcConfigs = BuzzRcConfig.read(root);
	}
	

	@Override
	public void handle(String target, Request r, HttpServletRequest req, HttpServletResponse res) 
	throws IOException, ServletException {
		BuzzContext ctx = new BuzzContext(target, r, req, res , this); 
		BuzzResourceController[] buzzRCs = ctx.getResourceController();
		try{
			boolean process = false;
			for (int i = 0; i < buzzRCs.length; i++) {
				if(process = buzzRCs[i].process( ctx )){
					break;
				}
			}
			if(!process){
				BuzzServer.processError(ctx, HttpServletResponse.SC_NOT_FOUND, "Resource not found", "No applicable resource controller", null, null);
			}
		}catch (BuzzException e) {
			BuzzServer.processError(ctx, e.statusCode, e.getMessage(), e.toString(), Throwables.toString(e), null);
		}catch (Exception e) {
			BuzzServer.processError(ctx, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", e.toString(), Throwables.toString(e), null);
		}
	}
}
