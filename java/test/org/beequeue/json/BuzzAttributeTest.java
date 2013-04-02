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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class BuzzAttributeTest {

	@Test
	public void test() {
		BuzzTable buzzTable = new BuzzTable();
		buzzTable.header.add(attr( "I1", BuiltInType.INTEGER, SortOrder.DESCENDING ));
		buzzTable.header.add(attr( "S2", BuiltInType.STRING, SortOrder.ASCENDING ));
		buzzTable.header.add(attr( "D3", BuiltInType.DATE, SortOrder.DESCENDING ));
		buzzTable.header.add(attr( "F4", BuiltInType.FLOAT, null ));
		buzzTable.header.add(attr( "F5", BuiltInType.FLOAT, null ));
		BuzzRow row = buzzTable.newRow();
		row.set("I1", "5");
		assertEquals(row.get("I1"), new Long(5));
		row.set("S2", 34);
		assertEquals(row.get("S2"), "34");
		buzzTable.addRow(row);
		try{
			buzzTable.header.add(attr( "F6", BuiltInType.FLOAT, null ));
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
		//todo tostring
		System.out.println(buzzTable.toString());
		buzzTable.sort();
		StringWriter w = new StringWriter();
		buzzTable.writeTable(w);
		String orig = w.toString();
		System.out.println(orig);
		StringReader r = new StringReader(orig);
		BuzzTable readTable = BuzzTable.readTable(r);
		String copy = readTable.toString();
		System.out.println(copy);
		Assert.assertEquals(orig, copy);
		Map<String,List<Object>> map = (Map<String, List<Object>>) ToStringUtil.toObject(copy, Object.class);
		System.out.println(map.get("header"));
		System.out.println(map.get("rows"));
		System.out.println(ToStringUtil.toObject(copy, BuzzTable.class).getRowCount());
		
	}

	BuzzAttribute attr( String name, BuiltInType type, SortOrder sort){
		BuzzAttribute a = new BuzzAttribute();
		a.name = name;
		a.type = type;
		a.sortOrder = sort;
		return a;
	}
}
