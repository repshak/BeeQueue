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
package org.beequeue.template;

import org.beequeue.util.DataType;

public class MessageAttribute {
	public String name;
	public AttributeType attrType;
	public DataType<?> dataType;

	public MessageAttribute() {
	}

	public MessageAttribute(String name, AttributeType attrType,
			DataType<?> dataType) {
		super();
		this.name = name;
		this.attrType = attrType;
		this.dataType = dataType;
	}

	
}
