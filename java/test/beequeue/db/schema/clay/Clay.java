/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package beequeue.db.schema.clay;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.beequeue.util.ToStringUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class Clay {
	
	@Override public String toString() { return ToStringUtil.toString(this); }
	
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
