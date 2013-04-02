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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

public class BuzzContext {
	String target;
	Request r; 
	HttpServletRequest req;
	HttpServletResponse res;
    BuzzPath resoursePath;
    Map<String, String> params;
    BuzzPath ancestor ;
    
	BuzzHandler handler;

	public BuzzResourceController[] getResourceController() {
		return handler.rcConfigs.get(this.ancestor).resourceControllers;
	}

	public BuzzPath relativePath(){
		return this.resoursePath.subpath(ancestor.size);
	}
	public BuzzContext(String target, Request r, HttpServletRequest req, HttpServletResponse res, BuzzHandler handler) {
		this.target = target;
		this.resoursePath = BuzzPath.valueOf(target.substring(1));
		this.handler = handler;
		this.ancestor = this.resoursePath.findAncestor(handler.rcConfigs.keySet());
		this.r = r;
		this.req = req;
		this.res = res;
	}

	public boolean setHandled() {
		r.setHandled(true);
		return true;
	}

}
