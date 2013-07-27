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
package org.beequeue.buzz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;
import org.beequeue.util.Nulls;
import org.beequeue.util.Strings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class BuzzPath implements Comparable<BuzzPath> {

	public static final BuzzPath EMPTY = new BuzzPath();

	private final String[] pathElements;
	private final int begin ;
	private final int end ;
	public final int size;
	
	static class Extension {
		String extension = null;
	}
	private Extension extension = null;
	
	
	public int size(){
		return size;
	}
	
	public String elementAt(int i){
		return pathElements[begin + i];
	}
	
	/**
	 * last element in the path often represent short name of object or file
	 * @return
	 */
	public String name(){
		return size > 0 ? pathElements[end - 1] : null ;
	}

	public String extension(){
		if(extension == null){
			String name = name();
			Extension e = new Extension();
			int dotPosition = name.lastIndexOf('.');
			if(dotPosition>0){
				e.extension = name.substring(dotPosition+1);
			}
			this.extension = e;
		}
		return extension.extension;
	}
	
	public String getMimeType(String defaultExtension){
		return BuzzMime.get(Nulls.fallback(extension(), defaultExtension));
	}
	
	@JsonCreator
	public static BuzzPath valueOf(String path) {
		return new BuzzPath(path.split("/+"));
	}

	private BuzzPath(String[] pathElements, int begin, int end ) {
		this.pathElements = pathElements;
		this.begin = begin;
		this.end = end;
		this.size = end-begin ;
	}
	
	public BuzzPath() {
		this(new String[0],0,0);
	}

	public BuzzPath( String... pathElements) {
		this(EMPTY,pathElements,0,pathElements.length);
	}
	
	public BuzzPath( List<String> pathElements ) {
		this(pathElements.toArray(new String[pathElements.size()]),0,pathElements.size());
		checkForEmpty();
	}

	private void checkForEmpty() {
		for (int i = 0; i < this.pathElements.length; i++) {
			if(Strings.isEmpty(this.pathElements[i])) 
					throw new BeeException("path mailformed: empty elements in the middle")
					.memo("pathElements",(Object)pathElements);
		}	
	}

	public BuzzPath( BuzzPath copy, String[] pathElements, int start, int end) {
		while(end > 0 && end > start && Strings.isEmpty(pathElements[end-1]) ){
			end--;
		}
		
		this.pathElements = new String[copy.size + end - start];
		int i = 0;
		for (; i < copy.size; i++) {
			this.pathElements[i] = copy.elementAt(i);
		}
		for (; i < this.pathElements.length; i++) {
			String element = pathElements[i-copy.size+start];
			if(Strings.isEmpty(element)) 
					throw new BeeException("path mailformed: empty elements in the middle")
					.memo("pathElements",(Object)pathElements);
			this.pathElements[i] = element;
		}	
		this.begin = 0 ;
		this.size = this.end  = this.pathElements.length;
	}
	
	@Override
	public int hashCode() {
		int hc = this.size;
		for (int i = this.begin; i < this.end; i++) {
			hc+=this.pathElements[i].hashCode();
		}
		return hc;
	}


	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof BuzzPath) ) return false;
		BuzzPath that = (BuzzPath) obj;
		int sz = this.size;
		if(sz != that.size) return false;
		for (int i = 0; i < sz; i++) {
			if( !this.elementAt(i).equals(that.elementAt(i)) ) return false;
		}
		return true;
	}


	@Override @JsonValue
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if(i > 0){
				sb.append("/");
			}
			sb.append(pathElements[begin + i]);
		}
		return sb.toString();
	}

	public String toString(BeeOperation<String, String> encode) {
		StringBuilder sb = new StringBuilder();
		appendItself(sb, encode);
		return sb.toString();
	}

	public StringBuilder appendItself(StringBuilder sb, BeeOperation<String, String> encode) {
		for (int i = 0; i < size; i++) {
			if(i > 0){
				sb.append("/");
			}
			sb.append(encode.execute(pathElements[begin + i]));
		}
		return sb;
	}


	@Override
	public int compareTo(BuzzPath that) {
		int rc = 0;
		int i = 0;
		for (; i < this.size; i++) {
			if( i >= that.size ) return 1;
			rc = this.elementAt(i).compareTo(that.elementAt(i));
			if(rc!=0)break;
		}		
		if( rc == 0 && i < that.size ) return -1;
		return rc;
	}

	public boolean startsWith(BuzzPath that){
		if(that.size > this.size ){
			return false;
		}
		for (int i = 0; i < that.size; i++) {
			if(!this.elementAt(i).equals(that.elementAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public BuzzPath subpath(int begin){
			return subpath(begin, size);
	}

	public BuzzPath subpath(int begin,int end){
		if(end > size) end = size ;
		if(begin >= 0  && begin <= end ){
			return new BuzzPath(this.pathElements, this.begin + begin, this.begin + end);
		}else{
			throw new BeeException( "Cannot subpath")
			.memo("path",toString())
			.memo("end",begin)
			.memo("end",end);
		}
	}
	
	public BuzzPath parent(){
		if(this.size == 0) return null;
		return subpath(0, this.size-1 );
	}
	
	public BuzzPath findAncestor(Collection<BuzzPath> set){
		BuzzPath key = this;
		for(;;) {
			if(set.contains(key)){
				return key;
			}
			if((key = key.parent()) == null){
				return null;
			}
		}
	}	

	public List<BuzzPath> findAllAncestors(Collection<BuzzPath> set){
		List<BuzzPath> allAncestors=new ArrayList<BuzzPath>();
		BuzzPath key = this;
		for(;;) {
			if(set.contains(key)){
				allAncestors.add(key);
			}
			if((key = key.parent()) == null){
				break;
			}
		}
		return allAncestors;
	}

	public BuzzPath addElements(String ... elementsToAdd) {
		return new BuzzPath(this, elementsToAdd,0,elementsToAdd.length);
	}	

	public BuzzPath add(BuzzPath toAdd) {
		if(toAdd.begin == toAdd.end){
			return this;
		}
		return new BuzzPath(this, toAdd.pathElements, toAdd.begin, toAdd.end);
	}	
}
