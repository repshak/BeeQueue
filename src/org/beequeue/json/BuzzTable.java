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
	private static final String BOH = "{\"header\": ";
	private static final String EOH = ",\"rows\":[";
	private static final String EOR = ",";
	private static final String EOF = "null]}";
	
	public final BuzzHeader header = new BuzzHeader();
	private final BoundList<BuzzRow> rows = new BoundList<BuzzRow>();
	
	public BuzzRow newRow(){
		return new BuzzRow(header);
	}

	public void addRow(int at, BuzzRow row){
		BeeException.throwIfTrue(!header.equals(row.header()), "!header.equals(row.header())");
		if(header.isUpdatesAllowed()) header.preventUpdates();
		row.preventUpdates();
		rows.add(at, row);
	}
	
	public void addRow(BuzzRow row){
		addRow(rows.size(),row);
	}
	
	public void setRow(int at, BuzzRow row){
		BeeException.throwIfTrue(rows.size() <= at, "rows.size() <= at");
		BeeException.throwIfTrue(!header.equals(row.header()), "!header.equals(row.header())");
		BuzzRow prevRow = rows.get(at);
		row.setPreviousVersion(prevRow);
		row.preventUpdates();
		rows.set(at, row);
	}

	public BuzzRow deleteRow(int at){
		BeeException.throwIfTrue(rows.size() <= at, "rows.size() <= at");
		BuzzRow prevRow = rows.get(at);
		rows.remove(at);
		return prevRow;
	}

	public BuzzRow getRow(int at){
		BeeException.throwIfTrue(rows.size() <= at, "rows.size() <= at");
		return rows.get(at);
	}
	
	@Override
	public boolean isUpdatesAllowed() {
		return rows.isUpdatesAllowed();
	}

	@Override
	public void preventUpdates() {
		header.preventUpdates();
		rows.preventUpdates();
	}

	@Override
	public Iterator<BuzzRow> iterator() {
		return rows.iterator();
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
			for (int i = 0; i < header.size() ; i++) {
				BuzzAttribute attr = header.get(i);
				int r = attr.compare(a.get(i), b.get(i));
				if(r != 0){
					return r;
				}
			}
			return 0;
		}
		
	}
	
	public String header(){
		return ToStringUtil.toNotPrettyString(header);
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
	
	public int binarySearch(BuzzRow row){
		return Collections.binarySearch(rows, row, getComparator());
	}

	public void writeTable(Writer w) {
		PrintWriter pw = new PrintWriter(w);
		try {
			pw.println(BOH + header() + EOH);
			for (BuzzRow row : this) {
				pw.println(row.toString() + EOR);
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
			String line = trimBoth(BOH,EOH,br.readLine());
			
			BuzzTable tab = new BuzzTable();
			tab.header.addAll(BuzzHeader.TF.op_STRING_TO_OBJ.execute(line));
			while((line = br.readLine())!=null){
				if( !line.trim().equals(EOF) ){
					@SuppressWarnings("unchecked")
					List<Object> rawData = (List<Object>)ToStringUtil.TF.op_STRING_TO_OBJ.execute(trimEnd(EOR, line));
					tab.addRow(new BuzzRow(tab.header, rawData));
				}
			}
			br.close();
			return tab;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}
	
	private static String trimBoth(String front, String end, String line) {
		BeeException.throwIfTrue(!line.startsWith(front)|| !line.endsWith(end), "!line.startsWith(front)|| !line.endsWith(end)");
		return line.substring(front.length(), line.length()-end.length());
	}
	private static String trimEnd( String end, String line) {
		BeeException.throwIfTrue(!line.endsWith(end), "!line.endsWith(end)");
		return line.substring(0, line.length()-end.length());
	}
	

	public int getRowCount(){
		return this.rows.size();
	}

}
