package org.beequeue.util;


public enum BeeCondition implements BeeOperation<Integer, Boolean>{
	eq { @Override public boolean hasMatch(int compareResult) {
			return compareResult == 0;
	}},
	ne { @Override public boolean hasMatch(int compareResult) {
			return compareResult != 0;
	}},
	lte {@Override public boolean hasMatch(int compareResult) {
			return compareResult <= 0;
	}},
	gte { @Override public boolean hasMatch(int compareResult) {
			return compareResult >= 0;
	}},
	lt { @Override public boolean hasMatch(int compareResult) {
			return compareResult < 0;
	}},
	gt { @Override public boolean hasMatch(int compareResult) {
			return compareResult > 0;
	}};
	
	abstract public boolean hasMatch(int compareResult);
	
	@Override public Boolean execute(Integer input) {
		return hasMatch(input);
	}
}