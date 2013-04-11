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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.util.BeeException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

final class BuzzHandler extends AbstractHandler {
	public final Map<String, ContentProvider> roots;

	public BuzzHandler(Map<String, ContentProvider> roots) {
		this.roots = roots;
	}


	@Override
	public void handle(String target, Request r, HttpServletRequest req, HttpServletResponse res) 
	throws IOException, ServletException {
		BuzzContext ctx = new BuzzContext(target, r, req, res , this); 
		boolean process;
		try{
			process = ctx.process();
			if(!process){
				throw new BeeException()
				.memo(BuzzServer.STATUS_CODE,  HttpServletResponse.SC_NOT_FOUND) ;
			}
		}catch (Exception e) {
			BuzzServer.processError(ctx, 
					BeeException.cast(e)
					.memo( "target", target)
					.memo( "roots", roots.keySet()) );
		}
	}
}
