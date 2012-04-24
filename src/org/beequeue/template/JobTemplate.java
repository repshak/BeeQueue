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
/**
 * JobTemplate has many stages, they may depend on each other, but no circular dependencies allowed. 
 * 
 * @author sergeyk
 *
 */
public class JobTemplate {
	public String jobName;
	public StageTemplate[] stages;
	public MessageFilter filters[] = {new MessageFilter("true")};
}
