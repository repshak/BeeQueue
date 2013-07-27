package org.beequeue.ctree;

import org.beequeue.hash.ContentKey;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ContainerTreeName extends ContentKey implements Comparable<ContainerTreeName>{

	@JsonCreator
	public ContainerTreeName(String name) {
		super(name);
	}

	@Override public int compareTo(ContainerTreeName that) {
		return doCompare(that);
	}
}
