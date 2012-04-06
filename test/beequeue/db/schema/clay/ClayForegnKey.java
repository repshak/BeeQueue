package beequeue.db.schema.clay;

import java.util.List;

import org.jdom.Element;

public class ClayForegnKey {
	public ClayForegnKey(Element e) {
		this.name = e.getAttributeValue("name");
		this.referncedKey = e.getAttributeValue("referenced-key");
		this.referncedTable = e.getAttributeValue("referenced-table");
		@SuppressWarnings("unchecked")
		List<Element> columnnodes = e.getChildren("foreign-key-column");
		this.foreignKeyColumns = new ClayForegnKeyColumn[columnnodes.size()];
		for (int i = 0; i < this.foreignKeyColumns.length; i++) {
			this.foreignKeyColumns[i] = new ClayForegnKeyColumn(columnnodes.get(i));
		}
	}
	public String name;
	public String referncedKey;
	public String referncedTable;
    public ClayForegnKeyColumn[] foreignKeyColumns; 	
	

}
