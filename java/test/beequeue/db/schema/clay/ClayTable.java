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

import java.util.List;

import org.beequeue.util.ToStringUtil;
import org.jdom.Element;


public class ClayTable {
	@Override public String toString() { return ToStringUtil.toString(this); }

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
