package org.beequeue.buzz;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.GroovyTemplate;
import org.beequeue.worker.Singletons;

public class QueryResourceController implements BuzzResourceController {
	public Map<String,String> queries;
	
	@Override
	public boolean process(BuzzContext ctx) {
		try{
			TransactionContext.push();
			Coordiantor c = Singletons.getCoordinator();
			ServletOutputStream out = ctx.res.getOutputStream();
			ctx.res.setContentType("text/plain");
			String prefix = ctx.ancestor.toString();
			BuzzPath relativePath = ctx.relativePath();
			if( prefix.equals("db") ){
				out.println(c.selectAnyTable(relativePath.toString()));
			}else{
				String queryTemplate = queries.get(ctx.resoursePath.elementAt(0));
				HashMap<String, String> context = new HashMap<String, String>();
				for (int i = 1; i < relativePath.size; i++) {
					context.put("v"+i, relativePath.elementAt(i));
				}
				String q = new GroovyTemplate(queryTemplate).generate(context);
				out.println(c.query(q));
			}
			return ctx.setHandled();
		}catch(Exception e){
			throw new BuzzException(500, e);
		}finally{
			TransactionContext.pop();
		}
	}
}
