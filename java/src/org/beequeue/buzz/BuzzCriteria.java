package org.beequeue.buzz;

import java.net.URLEncoder;

import org.beequeue.util.BeeCondition;

/**
<xmp>
File criteria
/branch/sec/ti/on//fi/le/pa/th

dbcriteria
/branch/sec/ti/ondb//index/a/b/b
this shortcut for to:
/branch/sec/ti/ondb//index?eq=a/b/c

/branch/sec/ti/ondb//index?gte=a1/b1/c1&lt=a2/b2/c3
</xmp>
will use URLencoding to escape simbols
http://en.wikipedia.org/wiki/Percent-encoding
  */

public class BuzzCriteria {
	BuzzPath section ;
	BuzzPath path ;
	String params;

	public static class Criterion {
		public final BeeCondition condition;
		public final BuzzPath path;
		public Criterion(String op, String path){
			this.condition = BeeCondition.valueOf(op);
			this.path = new BuzzPath(path);
		}
		
	}
	
	

}
