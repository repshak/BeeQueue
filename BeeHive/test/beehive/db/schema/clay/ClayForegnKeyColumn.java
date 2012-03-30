package beehive.db.schema.clay;

import org.jdom.Element;

public class ClayForegnKeyColumn {
	public ClayForegnKeyColumn(Element e) {
		this.columnName = e.getAttributeValue("column-name");
		this.referncedKeyColumnName = e.getAttributeValue("referenced-key-column-name");
	}
	public String columnName;
	public String referncedKeyColumnName;

}
