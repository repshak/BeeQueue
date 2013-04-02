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
