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
package org.beequeue.util;

import java.io.InputStream;
import java.util.Iterator;


public class Bytes implements Iterable<Bytes>{
	private byte[] bytes ;
	private int size ;
	private InputStream in ; 
	private int countRead = 0 ;
	
	
	public Bytes(byte[] bytes, InputStream in) {
		this.bytes = bytes;
		this.size = 0;
		this.in = in;
	}

	public Bytes(byte[] bytes) {
		this.bytes = bytes;
		this.size = bytes.length;
		this.in = null;
	}
	
	public Bytes(String s) {
		this.bytes = s.getBytes(UtfUtils.UTF8);
		this.size = this.bytes.length;
		this.in = null;
	}
	

	public byte[] bytes() {
		return bytes;
	}

	public int size() {
		return size;
	}

	private Bytes read(){
		if(this.bytes == null){
			return null;
		}else if(in!=null){
			try {
				this.size = in.read(this.bytes);
				if( this.size > 0  ){
					this.countRead += this.size;
					return this ;
				}else{
					this.bytes = null;
					this.in.close();
					this.in = null;
					return null ;
				}
					
			} catch (Exception e) {
				throw BeeException.cast(e);
			}
		}else{
			if( this.countRead == 0 ) {
				this.countRead += this.size;
				return this;
			}else{
				this.bytes = null;
				return null;
			}
		}
	}
	

	@Override
	public Iterator<Bytes> iterator() {
		return new Iterator<Bytes>() {
			private Bytes freshBytes = null ;
			private Bytes freshBytes(boolean cleanup){
				if(freshBytes==null){
					freshBytes = Bytes.this.read();
				}
				Bytes toReturn = freshBytes;
				if(cleanup) {
					freshBytes = null;
				}
				return toReturn;
			}
			
			@Override public void remove() { throw new UnsupportedOperationException(); }
			
			@Override public Bytes next() { return freshBytes(true); }

			@Override public boolean hasNext() { return freshBytes(false) != null; }
			
		};
	}
}
