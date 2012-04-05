package beequeue.db.schema.clay;

import java.util.List;

import org.beequeue.util.ToStringUtil;
import org.jdom.Element;


public class ClayTable extends ToStringUtil{

	public ClayTable(Element e) {
		name = e.getAttributeValue("name");
		primaryKey = new ClayPrimaryKey(e.getChild("primary-key"));
		@SuppressWarnings("unchecked")
		List<Element> columnList = (List<Element>)(e.getChild("column-list").getChildren());
		columns = new ClayColumn[columnList.size()];
		for (int i = 0; i < this.columns.length; i++) {
			this.columns[i] = new ClayColumn(columnList.get(i));
			
		}
		Element foreignKeysList = e.getChild("foreign-key-list");
		if(foreignKeysList != null){
			@SuppressWarnings("unchecked")
			List<Element> foreignKeyList = (List<Element>)(foreignKeysList.getChildren());
			foreignKeys = new ClayForegnKey[foreignKeyList.size()];
			for (int i = 0; i < this.foreignKeys.length; i++) {
				this.foreignKeys[i] = new ClayForegnKey(foreignKeyList.get(i));
				
			}
		}
	}
	public String name;
	public ClayColumn[] columns;
	public ClayPrimaryKey primaryKey;
	public ClayForegnKey[] foreignKeys;
}
