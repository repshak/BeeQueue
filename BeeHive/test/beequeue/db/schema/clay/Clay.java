package beequeue.db.schema.clay;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.beequeue.util.ToStringUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class Clay 
extends ToStringUtil{
	
	public ClayTable[] tables;
	public Clay(InputStream is) throws JDOMException, IOException{
		SAXBuilder parser = new SAXBuilder();
		 Document doc = parser.build(is);
		 Element root = doc.getRootElement();
		 Element schema = root.getChild("database-model").getChild("schema-list").getChild("schema");
		 Element tableList = schema.getChild("table-list");
		 @SuppressWarnings("unchecked")
		List<Element> children = (List<Element>)tableList.getChildren();
		 tables = new ClayTable[children.size()];
		 for (int i = 0; i < this.tables.length; i++) {
			this.tables[i] = new ClayTable(children.get(i));
			
		}
	}
}
