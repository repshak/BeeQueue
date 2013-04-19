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

import org.beequeue.piles.Lockable;
import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;
import org.beequeue.util.TypeFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

public class BuzzHeader  implements Lockable{

	final public BuzzColumns columns = new BuzzColumns();
	
	@JsonInclude (Include.NON_NULL)
	public BuzzSchema schema = null;
	
	public static TypeFactory<BuzzHeader> TF = new TypeFactory<BuzzHeader>(BuzzHeader.class) ;
	
	public BuzzAttribute addAttribute( String name, SortOrder sort, final BuiltInType type, final BuiltInType contentType, final String className){
		final BuzzSchemaBuilder bsb = new BuzzSchemaBuilder(schema);
		return addAttribute(name, sort, new BeeOperation<Void, BuzzAttribute>() {
			@Override public BuzzAttribute execute(Void input) {
				BuzzAttribute attribute = new BuzzAttribute();
				attribute.className = className;
				attribute.contentType = contentType;
				attribute.type = type;
				if(className!=null && (schema == null || !schema.types.map().containsKey(className)) ){
					try {
						Class<?> forName = Class.forName(className);
						bsb.add(forName);
					} catch (ClassNotFoundException ignore) {}
				}
				return attribute;
			}
		},bsb);
	}

	public BuzzAttribute addAttribute( String name, SortOrder sort, final Class<?> type){
		final BuzzSchemaBuilder bsb = new BuzzSchemaBuilder(schema);
		return addAttribute(name, sort, new BeeOperation<Void, BuzzAttribute>() {
			@Override public BuzzAttribute execute(Void input) {
				return bsb.add(type);
			}
		},bsb);
	}
	
	public BuzzAttribute addAttribute( String name, SortOrder sort, final TypeReference<?> type){
		final BuzzSchemaBuilder bsb = new BuzzSchemaBuilder(schema);
		return addAttribute(name, sort, new BeeOperation<Void, BuzzAttribute>() {
			@Override
			public BuzzAttribute execute(Void input) {
				return bsb.add(type);
			}
		},bsb);
	}
	
	private BuzzAttribute addAttribute( String name, SortOrder sort, BeeOperation<Void, BuzzAttribute> configure, BuzzSchemaBuilder bsb ){
		BuzzAttribute attribute = configure.execute(null);
		attribute.name = name;
		attribute.sortOrder = sort;
		columns.add(attribute);
		BeeException.throwIfTrue(bsb==null, "bsb==null");
		if( bsb.schema != this.schema){
			this.schema = bsb.schema.types.size() == 0 ? null : bsb.schema;
		}
		return attribute;
	}

	public void resetHeader(BuzzHeader that) {
		this.columns.clear();
		this.columns.addAll(that.columns);
	}

	@Override
	public boolean equals(Object that) {
		if(this==that) 
			return true;
		if(that==null)
			return false;
		return toString().equals(that.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return columns.toString();
	}

	@Override
	public boolean isUpdatesAllowed() {
		return columns.isUpdatesAllowed();
	}

	@Override
	public void preventUpdates() {
		columns.preventUpdates();
	}

	
	
	

}
