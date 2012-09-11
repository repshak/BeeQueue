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