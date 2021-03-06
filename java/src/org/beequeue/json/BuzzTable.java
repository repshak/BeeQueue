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
import org.beequeue.util.BeeOperation;
import org.beequeue.util.ToStringUtil;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuzzTable implements Iterable<BuzzRow>, Lockable{
	private static final String BEGINING_OF_THE_HEADER = "{\"header\": ";
	private static final String END_OF_THE_HEADER = ",\"rows\":[";
	private static final String END_OF_ROW = ",";
	private static final String END_OF_LAST_ROW = "]}";
	
	public final BuzzHeader header = new BuzzHeader();
	private final BoundList<BuzzRow> rows = new BoundList<BuzzRow>();
	
	public BuzzTable() {
	}

	@JsonCreator
	public BuzzTable(@JsonProperty("header") BuzzHeader header, @JsonProperty("rows") List<List<Object>> rows) {
		this.header.columns.addAll(header.columns);
		for (int i = 0; i < rows.size() ; i++) {
			List<Object> rawData = rows.get(i);
			if(rawData!=null){
				addRow(new BuzzRow(this.header, rawData));
			}
		}
	}

	public BuzzRow newRow(){
		BuzzRow buzzRow = new BuzzRow(header);
		buzzRow.setTargetTable(this);
		return buzzRow;
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
		header.columns.preventUpdates();
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
			for (int i = 0; i < header.columns.size() ; i++) {
				BuzzAttribute attr = header.columns.get(i);
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
			pw.println(BEGINING_OF_THE_HEADER + header() + END_OF_THE_HEADER);
			for (int i = 0; i < rows.size(); i++) {
				boolean lastRow = i+1==rows.size();
				String rowDelimiter = lastRow ? END_OF_LAST_ROW : END_OF_ROW;
				pw.println(rows.get(i).toString() +  rowDelimiter);
				
			}
			pw.close();
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}
	
	public static BeeOperation<String, BuzzTable> DEFAULT_TABLE_CONSTRUCTOR = new BeeOperation<String, BuzzTable>(){
		@Override
		public BuzzTable execute(String headerLine) {
			BuzzTable tab = new BuzzTable();
			tab.header.reset(BuzzHeader.TF.op_STRING_TO_OBJ.execute(headerLine));
			return tab;
		}};
	
	public static BuzzTable readTable(Reader r, BeeOperation<String, BuzzTable> tableConstructor) {
		BufferedReader br = new BufferedReader(r);
		try {
			String line = trimBoth(BEGINING_OF_THE_HEADER,END_OF_THE_HEADER,br.readLine());
			BuzzTable tab = tableConstructor.execute(line);
			while((line = br.readLine())!=null){
				String trimedLine = line.trim();
				if( trimedLine.endsWith(END_OF_ROW) ){
					trimedLine = trimEnd(END_OF_ROW, trimedLine);
				}else if( trimedLine.endsWith(END_OF_LAST_ROW) ){
					trimedLine = trimEnd(END_OF_LAST_ROW, trimedLine);
				}else{
					throw new BeeException("unxpected ending of the line").memo("line", trimedLine);
				}
				@SuppressWarnings("unchecked")
				List<Object> rawData = (List<Object>)ToStringUtil.TF.op_STRING_TO_OBJ.execute(trimedLine);
				tab.addRow(new BuzzRow(tab.header, rawData));
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
