package org.beequeue.json;

import org.beequeue.piles.MapList;
import org.beequeue.util.BeeException;
import org.beequeue.util.TypeFactory;

public class BuzzHeader extends MapList<String, BuzzAttribute> {
	private static final long serialVersionUID = 1L;
	public static TypeFactory<BuzzHeader> TF = new TypeFactory<BuzzHeader>(BuzzHeader.class) ;
	
	public BuzzHeader() {
		super(BuzzAttribute.op_GET_NAME);
	}

	public int colIndex(String name) {
		Integer idx = indexMap().get(name);
		BeeException.throwIfTrue(idx==null, "idx==null when name:"+name );
		return idx;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BuzzHeader) {
			BuzzHeader that = (BuzzHeader) o;
			return this.toString().equals(that.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	private volatile String stringRepresentation = null;
	@Override
	public String toString() {
		String s = this.stringRepresentation;
		if(s==null){
			s = TF.op_OBJ_TO_COMPACT.execute(this);
			this.stringRepresentation = s;
		}
		return s;
	}

	@Override
	public void refresh() {
		this.stringRepresentation = null;
		super.refresh();
	}
	
	

}
