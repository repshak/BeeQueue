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
package org.beequeue.msg;

import java.util.ArrayList;
import java.util.List;

import org.beequeue.template.AttributeType;
import org.beequeue.template.MessageTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.Nulls;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *  MessageName:Attribute1:Attribute2:...:AttributeN
 *  
 */
public class MessageLocator implements Comparable<MessageLocator> {
	public final String name;
	public final String[] attributes;
	public final String locator; 
	
	public MessageLocator(String locator) {
		List<StringBuilder> split = splitAndUnescape(locator);
		this.name = split.remove(0).toString();
		this.attributes = new String[split.size()];
		for (int i = 0; i < this.attributes.length; i++) {
			this.attributes[i] = split.get(i) == null ? null : split.get(i).toString();
		}
		this.locator = buildToString(name, attributes, locator.length()+5);
	}

	public MessageLocator(String name, String[] attributes) {
		this.name = name;
		this.attributes = attributes;
		this.locator = buildToString(name, attributes, 16+name.length()*(1+attributes.length));
	}
	
	public static MessageLocator valueOf(String s) {
		return new MessageLocator(s);
	}
	
	@Override @JsonValue
	public String toString() {
		return locator;
	}

	@Override
	public int hashCode() {
		return locator.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MessageLocator) {
			MessageLocator that = (MessageLocator) obj;
			return this.locator.equals(that.locator);
		}
		return false;
	}

	public MessageLocator extractMessageKind(MessageTemplate template){
		if( this.attributes.length != template.keyColumns().length || !this.name.equals(template.messageName) ){
			throw new BeeException("template does not comply with message locator:").addPayload(template, this);
		}
		String kindAttributes[] = new String[template.columns.length];
		for (int i = 0; i < template.columns.length; i++) {
			kindAttributes[i] =  template.columns[i].attrType == AttributeType.PARALLEL ?  this.attributes[i] : null ;
		}
		return new MessageLocator(this.name, kindAttributes);
	}
	
	public boolean match(MessageLocator input) {
		if( this.name.equals(input.name) && this.attributes.length == input.attributes.length ){
			for (int i = 0; i < this.attributes.length; i++) {
				String matchAttribute = this.attributes[i];
				if(matchAttribute != null && !matchAttribute.equals(input.attributes[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(MessageLocator that) {
		int rc = name.compareTo(that.name);
		if(rc ==0){
			rc = Nulls.compare(this.attributes, that.attributes, true, true);
		}
		return rc;
	}

	private static String buildToString(String name, String[] attributes, int guessSize) {
		StringBuilder sb = new StringBuilder(guessSize);
		escape(sb, name);
		for (int i = 0; i < attributes.length; i++) {
			sb.append(':');
			escape(sb, attributes[i] );
		}
		String toString = sb.toString();
		return toString;
	}

	private static void escape(StringBuilder sb, String s){
		if(s == null){
			sb.append('*');
		}else{
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				if( ch == '\\' || ch == ':' || ch == '*' ){
					sb.append('\\');
				}
				sb.append(ch);
			}
		}
	}
	

	private static List<StringBuilder> splitAndUnescape(String s){
		List<StringBuilder> split = new ArrayList<StringBuilder>();
		StringBuilder sb = new StringBuilder();
		split.add(sb);
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if( ch == ':' ){
				sb = new StringBuilder();
				split.add(sb);
			}else{
				if( ch == '*' && sb.length() == 0 ){
					split.set(split.size()-1, null);
				}else{
					if(split.get(split.size()-1)==null){
						split.set(split.size()-1, sb);
					}
					if( ch == '\\' ){
						ch = s.charAt(++i);
					}				
				}
				sb.append(ch);
			}
		}
		return split;
	}


}
