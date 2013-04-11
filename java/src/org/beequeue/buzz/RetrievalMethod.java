package org.beequeue.buzz;

import java.io.IOException;

import org.beequeue.util.Streams;

public enum RetrievalMethod { 
	STREAM {
		@Override
		public void serve(BuzzContent content, BuzzContext ctx) throws IOException {
			Streams.copyAndClose(content.getStream(), ctx.res.getOutputStream());
		}
	}, 
	READER{
		@Override
		public void serve(BuzzContent content, BuzzContext ctx) throws IOException {
			Streams.copyAndClose(content.getReader(), ctx.res.getWriter());
		}
		
		
	}, 
	BUZZ_TABLE{
		@Override
		public void serve(BuzzContent content, BuzzContext ctx) throws IOException {
			content.getBuzzTable().writeTable(ctx.res.getWriter());
		}
	};
	
	abstract public void serve(BuzzContent content, BuzzContext ctx) throws IOException;
	}