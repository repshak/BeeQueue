package org.beequeue.ctree;

import org.beequeue.hash.ContentKey;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserId extends ContentKey implements Comparable<UserId>{

	@JsonCreator
	public UserId(String name) {
		super(name);
	}

	@Override public int compareTo(UserId that) {
		return doCompare(that);
	}
}
