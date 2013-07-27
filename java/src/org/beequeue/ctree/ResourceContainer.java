package org.beequeue.ctree;

import java.io.File;
import java.io.InputStream;

import org.beequeue.buzz.BuzzPath;
import org.beequeue.json.BuzzTable;
import org.beequeue.json.SortOrder;
import org.beequeue.util.BeeException;

public class ResourceContainer {
	final public AccessList accessList;
	final public ContainerTreeName name;
	final public File storeDir;
	final public ResourceContainer source;
	
	public ResourceContainer(AccessList accessList, ContainerTreeName name, File storeDir, ResourceContainer source) {
		this.accessList = accessList ;
		BeeException.throwIfNull(name, "name has to be defined");
		this.name = name;
		BeeException.throwIfTrue(!(storeDir.isDirectory() || storeDir.mkdirs()),  "storeDir:"+storeDir+" is not directory");
		this.storeDir = storeDir;
		this.source = source;
	}
	
	
	public static class ResourceList extends BuzzTable {
		public ResourceList() {
			this.header.addAttribute("PATH", SortOrder.ASCENDING, BuzzPath.class);
			this.header.addAttribute("UID", SortOrder.ASCENDING, UserId.class);
			this.header.addAttribute("ACCESS", SortOrder.ASCENDING, Access.class);
			this.header.addAttribute("RESOURCE", null, StagedResource.class);
		}
	}

	public class StagedResource{
		final public BuzzPath path;
		final public File file;
		final public UserId userId;
		final public Access access;
		
		void close(){
			
		}

		private StagedResource(BuzzPath path, File file, UserId userId, Access access) {
			super();
			this.path = path;
			this.file = file;
			this.userId = userId;
			this.access = access;
		}
	}
	
	
	public InputStream stream(UserId who, BuzzPath path){
		return null;
	}
	
	public StagedResource stage(UserId who, BuzzPath path, boolean copy, StageMode stage ){
		return null;
    }
	
	public void store(UserId who, File from, BuzzPath to){
    }


}
