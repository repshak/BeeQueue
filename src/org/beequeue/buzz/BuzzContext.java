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
		this.r = r;
		this.req = req;
		this.res = res;
		this.handler = handler;
		this.resoursePath = BuzzPath.valueOf(target.substring(1));
		this.ancestor = this.resoursePath.findAncestor(handler.rcConfigs.keySet());
		System.out.println(target);
	}

	public boolean setHandled() {
		r.setHandled(true);
		return true;
	}

}
