package beequeue.db.schema.clay;

import org.jdom.Element;

public class ClayColumn {
	
	public ClayColumn(Element e) {
		this.autoIncrement = Boolean.parseBoolean(e.getAttributeValue("auto-increment"));
		this.mandatory = Boolean.parseBoolean(e.getAttributeValue("mandatory"));
		this.columnSize = Integer.parseInt(e.getAttributeValue("column-size"));
		this.decimalDigits = Integer.parseInt(e.getAttributeValue("decimal-digits"));
		this.defaultValue = e.getAttributeValue("default-value");
		this.name = e.getAttributeValue("name");
		this.remarks = e.getAttributeValue("remarks");
		this.dataType = e.getChild("data-type").getAttributeValue("name");
	}

	public String name;
	public String dataType;
	public int columnSize ;
	public String remarks;
	public int decimalDigits;
	public String defaultValue;
	public boolean mandatory;
	public boolean autoIncrement;
}
