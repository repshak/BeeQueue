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
package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzAttribute {
	@JsonInclude  (Include.NON_NULL)
	public String name = null;
	public BuiltInType type;
	/** populated only for collections  */ @JsonInclude  (Include.NON_NULL)
	public BuiltInType contentType=null;
	
	/** populated for non primitive types only */ @JsonInclude  (Include.NON_NULL)
	public String className = null;
	
	public void copyTypeId(BuzzAttribute that) {
		this.type = that.type;
		this.contentType = that.contentType;
		this.className = that.className;
		
	}
	public void copyTypeIdAsContent(BuzzAttribute that) {
		this.contentType = that.type;
		this.className = that.className;
		
	}
}
