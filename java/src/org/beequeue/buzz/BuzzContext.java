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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beequeue.util.BeeException;
import org.eclipse.jetty.server.Request;

public class BuzzContext {
	public final String target;
	public final Request r; 
	public final HttpServletRequest req;
	public final HttpServletResponse res;
	public final BuzzPath resoursePath;
	public final ContentProvider provider;
	public final BuzzHandler handler;

	public BuzzPath relativePath(){
		return this.resoursePath.subpath(1);
	}

	public String handlerId() {
		return resoursePath.size() >= 1 ? resoursePath.elementAt(0) : null;
	}
	
	public BuzzContext(String target, Request r, HttpServletRequest req, HttpServletResponse res, BuzzHandler handler) {
		try {
			this.target = target;
			this.resoursePath = BuzzPath.valueOf(target.substring(1));
			this.handler = handler;
			this.r = r;
			this.req = req;
			this.res = res;
			String handlerId = handlerId();
			this.provider = handler.roots.get(handlerId);
		} catch (Exception e) {
			throw BeeException.cast(e).memo("target", target);
		}
	}


	public boolean setHandled() {
		r.setHandled(true);
		return true;
	}

	public boolean process() throws IOException {
		if(provider == null) return false;
		BuzzPath relativePath = relativePath();
		BuzzContent content = provider.getContent(relativePath);
		res.setContentType(content.getContentType());
		RetrievalMethod method = content.getMethod();
		method.serve(content, this);
		return setHandled();	
	}

}
