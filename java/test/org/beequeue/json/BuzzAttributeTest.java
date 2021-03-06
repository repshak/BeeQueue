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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.beequeue.util.BeeException;
import org.beequeue.util.Files;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class BuzzAttributeTest {

	@Test
	public void test() {
		BuzzTable buzzTable = new BuzzTable();
		buzzTable.header.addAttribute( "I1", SortOrder.DESCENDING, BuiltInType.INTEGER, null, null );
		buzzTable.header.addAttribute( "S2", SortOrder.ASCENDING, BuiltInType.STRING, null, null);
		buzzTable.header.addAttribute( "D3", SortOrder.DESCENDING, BuiltInType.DATE,  null, null);
		buzzTable.header.addAttribute( "F4", null, BuiltInType.FLOAT, null, null );
		buzzTable.header.addAttribute( "F5", null, BuiltInType.FLOAT, null, null );
		BuzzRow row = buzzTable.newRow();
		row.set("I1", "5");
		assertEquals(row.get("I1"), new Long(5));
		row.set("S2", 34);
		assertEquals(row.get("S2"), "34");
		buzzTable.addRow(row);
		try{
			buzzTable.header.columns.add(BuzzAttribute.newAttr( "F6", BuiltInType.FLOAT, null ));
			fail("BeeException !updatesAllowed expected");
		}catch (BeeException e) {
			assertEquals(e.getMessage(), "!updatesAllowed");
		}
		row = buzzTable.newRow();
		row.set("I1", 7);
		row.set("S2", "33");
		buzzTable.addRow(row);
		row = buzzTable.newRow();
		row.set("I1", 5);
		row.set("S2", "33");
		buzzTable.addRow(row);
		row = buzzTable.newRow();
		row.set("I1", 5);
		row.set("S2", "33");
		row.set("D3", "2013-03-08T20:11:11.012+01:30");
		buzzTable.addRow(row);
		System.out.println(buzzTable.toString());
		buzzTable.sort();
		StringWriter w = new StringWriter();
		buzzTable.writeTable(w);
		String orig = w.toString();
		System.out.println(orig);
		StringReader r = new StringReader(orig);
		BuzzTable readTable = BuzzTable.readTable(r,BuzzTable.DEFAULT_TABLE_CONSTRUCTOR);
		String copy = readTable.toString();
		System.out.println(copy);
//		dumpContent(copy, "table.json");
		Assert.assertEquals(orig, copy);
		Map<String,List<Object>> map = (Map<String, List<Object>>) ToStringUtil.toObject(copy, Object.class);
		System.out.println(map.get("header"));
		System.out.println(map.get("rows"));
		System.out.println(ToStringUtil.toObject(copy, BuzzTable.class).getRowCount());
		
	}

	public static enum Abc {
		A, B, C
	}
	
	public static class Ooo6 {
		public Map<String,Abc> map;
		public String name;
		public double weight;
	}
	@Test
	public void testWithSchema()  {
		BuzzTable buzzTable = new BuzzTable();
		buzzTable.header.addAttribute("I1", SortOrder.DESCENDING, Integer.class );
		buzzTable.header.addAttribute( "S2", SortOrder.ASCENDING, String.class);
		buzzTable.header.addAttribute("D3", SortOrder.DESCENDING, Date.class);
		buzzTable.header.addAttribute( "F4", null, Float.class );
		buzzTable.header.addAttribute( "F5", null, Float.class );
		buzzTable.header.addAttribute( "E6", null, Abc.class );
		buzzTable.header.addAttribute( "O7", null, Ooo6.class );
		buzzTable.header.addAttribute( "L8", null, BuzzLink.class );
		BuzzRow row = buzzTable.newRow();
		row.set("I1", "5");
		assertEquals(row.get("I1"), new Long(5));
		row.set("S2", 34);
		assertEquals(row.get("S2"), "34");
		buzzTable.addRow(row);
		try{
			buzzTable.header.columns.add(BuzzAttribute.newAttr( "F6", BuiltInType.FLOAT, null ));
			fail("BeeException !updatesAllowed expected");
		}catch (BeeException e) {
			assertEquals(e.getMessage(), "!updatesAllowed");
		}
		row = buzzTable.newRow();
		row.set("I1", 7);
		row.set("S2", "33");
		buzzTable.addRow(row);
		row = buzzTable.newRow();
		row.set("I1", 5);
		row.set("S2", "33");
		row.set("O7", new Ooo6());
		buzzTable.addRow(row);
		row = buzzTable.newRow();
		row.set("I1", 5);
		row.set("S2", "33");
		row.set("D3", "2013-03-08T20:11:11.012+01:30");
		row.set("L8", new BuzzLink().init("MSN","http://msn.com"));
		Ooo6 o7 = new Ooo6();
		o7.map = new LinkedHashMap<String, BuzzAttributeTest.Abc>();
		o7.map.put("AA", Abc.C);
		row.set("O7", o7);
		buzzTable.addRow(row);
		System.out.println(buzzTable.toString());
		buzzTable.sort();
		StringWriter w = new StringWriter();
		buzzTable.writeTable(w);
		String orig = w.toString();
		System.out.println(orig);
		StringReader r = new StringReader(orig);
		BuzzTable readTable = BuzzTable.readTable(r,BuzzTable.DEFAULT_TABLE_CONSTRUCTOR);
		String copy = readTable.toString();
		System.out.println(copy);
//		dumpContent(copy, "object.json");
		Assert.assertEquals(orig, copy);
		Map<String,List<Object>> map = (Map<String, List<Object>>) ToStringUtil.toObject(copy, Object.class);
		System.out.println(map.get("header"));
		System.out.println(map.get("rows"));
		System.out.println(ToStringUtil.toObject(copy, BuzzTable.class).getRowCount());
		
	}
	public void dumpContent(String content, String file) {
		try {
			File d = new File("js/test/data/table");
			d.mkdirs();
			Files.writeAll(new File(d, file), content);
		} catch (Exception e) {
			throw BeeException.cast(e).memo("file",file);
		}
	}
}
