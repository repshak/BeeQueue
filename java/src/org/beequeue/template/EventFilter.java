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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.beequeue.msg.BeeQueueEvent;

public class EventFilter {
	public String expression;


	public EventFilter() {}


	public EventFilter(String expression) {
		this.expression = expression;
	}


	public boolean evalFilter(BeeQueueEvent msg){
		Binding binding = new Binding();
		binding.setVariable("msg", msg);
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(expression);
		if( !(value instanceof Boolean) ){
			throw new RuntimeException("Boolean value expected out of expression: "+expression);
		}
		return (Boolean)value;
	}
}
