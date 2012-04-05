package beequeue.db.schema.clay;

import java.util.List;

import org.jdom.Element;

public class ClayPrimaryKey {
	public ClayPrimaryKey(Element e) {
		this.name = e.getAttributeValue("name");
		@SuppressWarnings("unchecked")
		List<Element> columnnodes = e.getChildren("primary-key-column");
		this.primaryKeyColumns = new String[columnnodes.size()];
		for (int i = 0; i < this.primaryKeyColumns.length; i++) {
			this.primaryKeyColumns[i] = columnnodes.get(i).getAttributeValue("name");
		}
	}
	public String name;
	public String[] primaryKeyColumns;
}
