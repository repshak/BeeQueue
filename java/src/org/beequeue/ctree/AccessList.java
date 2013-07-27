package org.beequeue.ctree;

import java.io.Reader;

import org.beequeue.buzz.BuzzPath;
import org.beequeue.json.BuzzHeader;
import org.beequeue.json.BuzzTable;
import org.beequeue.json.SortOrder;
import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;

public class AccessList extends BuzzTable{

	public static final String ACCESS = "ACCESS";
	public static final String PATH = "PATH";
	public static final String UID = "UID";

	public AccessList() {
		this.header.addAttribute(UID, SortOrder.ASCENDING, UserId.class);
		this.header.addAttribute(PATH, SortOrder.ASCENDING, BuzzPath.class);
		this.header.addAttribute(ACCESS, SortOrder.ASCENDING, Access.class);
		this.header.preventUpdates();
	}
	
	public static BeeOperation<String, BuzzTable> ACCESSLIST_TABLE_CONSTRUCTOR = new BeeOperation<String, BuzzTable>(){
		@Override
		public BuzzTable execute(String headerLine) {
			AccessList tab = new AccessList();
			BeeException.throwIfTrue(!BuzzHeader.TF.op_OBJ_TO_COMPACT.execute(tab.header).equals(headerLine), 
					"!BuzzHeader.TF.op_OBJ_TO_COMPACT.execute(tab.header).equals(headerLine)",
					headerLine);
			return tab;
		}};
	
	public static AccessList readTable(Reader r){
		return (AccessList)BuzzTable.readTable(r, ACCESSLIST_TABLE_CONSTRUCTOR);
	}
}
