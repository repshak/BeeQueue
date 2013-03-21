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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.beequeue.piles.BoundList;
import org.beequeue.piles.Lockable;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;

public class BuzzTable implements Iterable<BuzzRow>, Lockable{
	private static final String EOF = "EOF";
	public BuzzHeader columns = new BuzzHeader();
	public BoundList<BuzzRow> rows = new BoundList<BuzzRow>();
	
	public BuzzRow addNewRow(){
		if(columns.isUpdatesAllowed()) columns.preventUpdates();
		Row row = new Row();
		rows.add(row);
		return row;
	}
	
	@Override
	public boolean isUpdatesAllowed() {
		return rows.isUpdatesAllowed();
	}

	@Override
	public void preventUpdates() {
		columns.preventUpdates();
		rows.preventUpdates();
	}

	@Override
	public Iterator<BuzzRow> iterator() {
		return rows.iterator();
	}
	
	public int colIndex(String name) {
		Integer idx = columns.indexMap().get(name);
		BeeException.throwIfTrue(idx==null, "idx==null when name:"+name );
		return idx;
	}
	
	public class Row implements BuzzRow {
		private Object[] data = new Object[columns.size()];
		
		@Override
		public Object get(String name) {
			return get(colIndex(name));
		}


		@Override
		public void set(String name, Object v) {
			set(colIndex(name), v);
		}

		@Override
		public Object get(int idx) {
			return data[idx];
		}

		@Override
		public void set(int idx, Object v) {
			BeeException.throwIfTrue(!isUpdatesAllowed(), "!isUpdatesAllowed()");
			data[idx] = columns.get(idx).coerce(v);
		}


		@Override
		public String toString() {
			return ToStringUtil.toNotPrettyString(data);
		}
		
		
		
	}
	

	private Comparator<BuzzRow> comparator = null;
	public Comparator<BuzzRow> getComparator() {
		Comparator<BuzzRow> c = comparator;
		if(c == null){
			c = new RowComparator();
			comparator = c;
		}
		return c;
	}

	private class RowComparator implements Comparator<BuzzRow>{
		@Override
		public int compare(BuzzRow a, BuzzRow b) {
			for (int i = 0; i < columns.size() ; i++) {
				BuzzAttribute attr = columns.get(i);
				int r = attr.compare(a.get(i), b.get(i));
				if(r != 0){
					return r;
				}
			}
			return 0;
		}
		
	}
	
	public String header(){
		return ToStringUtil.toNotPrettyString(columns);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		writeTable(sw);
		return sw.toString();
	}
	
	public void sort(){
		Collections.sort(rows, getComparator());
	}

	public void writeTable(Writer w) {
		PrintWriter pw = new PrintWriter(w);
		try {
			pw.println(header());
			for (BuzzRow row : this) {
				pw.println(row.toString());
			}
			pw.println(EOF);
			pw.close();
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}
	public static BuzzTable readTable(Reader r) {
		BufferedReader br = new BufferedReader(r);
		try {
			String line = br.readLine();
			BuzzTable tab = new BuzzTable();
			tab.columns.addAll(BuzzHeader.TF.op_STRING_TO_OBJ.execute(line));
			while((line = br.readLine())!=null){
				if( !line.trim().equals(EOF) ){
					List<Object> rawData = (List<Object>)ToStringUtil.TF.op_STRING_TO_OBJ.execute(line);
					BuzzRow row = tab.addNewRow();
					for (int i = 0; i < rawData.size(); i++) {
						row.set(i, rawData.get(i));
					}
				}
			}
			br.close();
			return tab;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

}
