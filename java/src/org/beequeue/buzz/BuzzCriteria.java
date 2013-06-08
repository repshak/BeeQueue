package org.beequeue.buzz;

import java.util.ArrayList;
import java.util.List;

import org.beequeue.util.BeeCondition;
import org.beequeue.util.BeeException;
import org.beequeue.util.BeeTokenizer;
import org.beequeue.util.PercentEncoding;
import org.beequeue.util.Strings;

/**
<xmp>
File criteria
branch/sec/ti/on//fi/le/pa/th

dbcriteria
branch/sec/ti/ondb//index/a/b/b
this shortcut for to:
branch/sec/ti/ondb//index?eq=a/b/c

branch/sec/ti/ondb//index?gte=a1/b1/c1&lt=a2/b2/c3
</xmp>
will use URLencoding to escape simbols
http://en.wikipedia.org/wiki/Percent-encoding
  */

public class BuzzCriteria {
	public static final String DELIMITERS = "/?&=" ;
	
	BuzzPath section ;
	BuzzPath path ;
	BuzzBound[] bounds;
	
	public BuzzCriteria(String s){
		BeeTokenizer bt = new BeeTokenizer(s, DELIMITERS);
		try {
			List<String> sectionList = new ArrayList<String>();
			List<String> pathList = new ArrayList<String>();
			List<BuzzBound> boundsList = new ArrayList<BuzzBound>();
			String delimiter;
			for (;;) {
				sectionList.add(PercentEncoding.decode(bt.nextValue()));
				delimiter = bt.nextDelimiter();
				if (delimiter.equals("//")) {
					break;
				} else{
					bt.assertDelimiter(delimiter, "/");
				}
			}
			for (;;) {
				pathList.add(PercentEncoding.decode(bt.nextValue()));
				delimiter = bt.nextDelimiter();
				if (Strings.isEmpty(delimiter) ) {
					break;
				} else if(delimiter.equals("?")){
					for (;;) {
						String op = bt.nextValue();
						delimiter = bt.nextDelimiter();
						bt.assertDelimiter(delimiter, "=");
						List<String> opPath = new ArrayList<String>();
						for (;;) {
							opPath.add(PercentEncoding.decode(bt.nextValue()));
							delimiter = bt.nextDelimiter();
							if (Strings.isEmpty(delimiter) || delimiter.equals("&")) {
								break;
							}else{
								bt.assertDelimiter(delimiter, "/");
							}
						}
						boundsList.add(new BuzzBound(op, opPath));
						if (Strings.isEmpty(delimiter)) {
							break;
						}
					}
					break;
				} else {
					bt.assertDelimiter(delimiter, "/");
				}
			}
			this.section = new BuzzPath(sectionList);
			this.path = new BuzzPath(pathList);
			this.bounds = boundsList.toArray(new BuzzBound[boundsList.size()]);
		} catch (Exception e) {
			throw BeeException.cast(e)
				.memo("bt",bt.toString());
		}
	}


	public static class BuzzBound {
		public final BeeCondition condition;
		public final BuzzPath path;
		
		public BuzzBound(String op, List<String> path){
			this.condition = BeeCondition.valueOf(op);
			this.path = new BuzzPath(path);
		}
		
		@Override
		public String toString() {
			return this.condition.name()+"="+this.path.toString(PercentEncoding.ENCODE);
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.section.appendItself(sb, PercentEncoding.ENCODE);
		sb.append("//"); 
		this.path.appendItself(sb, PercentEncoding.ENCODE);
		if(bounds.length > 0){
			sb.append('?'); 
			for (int i = 0; i < bounds.length; i++) {
				if(i>0){
					sb.append('&');
				}
				sb.append(this.bounds[i].condition.name());
				sb.append('=');
				this.bounds[i].path.appendItself(sb, PercentEncoding.ENCODE);
				
			}
		}
		return sb.toString();
	}
	
	

}
