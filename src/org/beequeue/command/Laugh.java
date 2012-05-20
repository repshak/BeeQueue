package org.beequeue.command;

import org.beequeue.util.ToStringUtil;

public class Laugh {
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			System.out.println(i);
			System.out.println(string);
			System.out.println(ToStringUtil.toString(args));
		}
	}

}
