package org.beequeue.util;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonTable {
	String title;
	public String parent; 
	public String drill;
	List<Object> header ; 
	List<List<Object>> data ;
	
	public JsonTable(String title) {
		super();
		this.title = title;
		this.parent = null;
		this.drill = null;
		this.header = new ArrayList<Object>();
		this.data = new ArrayList<List<Object>>();
	}
	
	public JsonTable(String title, String parent, String drill,
			List<Object> header, List<List<Object>> data) {
		super();
		this.title = title;
		this.parent = parent;
		this.drill = drill;
		this.header = header;
		this.data = data;
	}



	public static String queryToJson(ResultSet rs, String title, String parent, String drill) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		List<Object> header = buildHeader(md);
		List<List<Object>> data = buildData(rs, header);
		rs.close();
		return new JsonTable(title, parent, drill, header, data).toJsonTable();
	}



	public String toJsonTable() {
		Map<String,Object> d = new LinkedHashMap<String, Object>();
		d.put("tabularData",true);
		if( drill != null ) d.put("drill", drill);
		if( parent != null ) d.put("parent", parent);
		d.put("title", title);
		d.put("aoColumns", header);
		d.put("aaData", data);
		return ToStringUtil.toString(d);
	}



	public static List<List<Object>>  buildData(ResultSet rs, List<Object> header)
			throws SQLException {
		List<List<Object>> data = new ArrayList<List<Object>>();
		while(rs.next()){
			ArrayList<Object> row = new ArrayList<Object>();
			data.add(row);
			for (int i = 0; i < header.size(); i++) {
				Object object = rs.getObject(i+1);
				if(object instanceof Clob){
				Clob c = (Clob) object;
				object = c.getSubString(1, (int)c.length());
				}
				row.add(object);
				
			}
		}
		return data;
	}



	public static ArrayList<Object> buildHeader(ResultSetMetaData md)
			throws SQLException {
		ArrayList<Object> header = new ArrayList<Object>();
		for (int i = 0; i < md.getColumnCount(); i++) {
			header.add(buildColumn(md.getColumnName(i+1)));
		}
		return header;
	}
	
	public static LinkedHashMap<String, Object> buildColumn(String columnName) {
		LinkedHashMap<String, Object> col = new LinkedHashMap<String, Object>();
		col.put("sTitle", columnName);
		return col;
	}

	public void add(String columnName, String[] columnData) {
		int at = this.header.size();
		this.header.add(buildColumn(columnName));
		for (int i = 0; i < columnData.length; i++) {
			while( this.data.size() <= i ){
				this.data.add(new ArrayList<Object>());
			}
			List<Object> list = this.data.get(i);
			while( list.size() <= at ){
				list.add(null);
			}
			list.set(at, columnData[i]);
		}
	}

}
