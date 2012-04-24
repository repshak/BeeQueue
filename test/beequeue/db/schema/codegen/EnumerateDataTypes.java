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
package beequeue.db.schema.codegen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jdom.JDOMException;
import org.junit.Test;

import beequeue.db.schema.clay.Clay;
import beequeue.db.schema.clay.ClayColumn;

public class EnumerateDataTypes {

	@Test
	public void test() throws Exception {
		Set<String> dataTypes = new HashSet<String>();
		collectDataTypeUsage("design/dbModel.clay", dataTypes);
		collectDataTypeUsage("design/dbModelWithObjectsDesignedAsTables.clay", dataTypes);
		System.out.println(dataTypes);
	}

	public void collectDataTypeUsage(String model, Set<String> dataTypes)
			throws JDOMException, IOException, FileNotFoundException {
		Clay clay = new Clay(new FileInputStream(model));
		for (int i = 0; i < clay.tables.length; i++) {
			ClayColumn[] columns = clay.tables[i].columns;
			for (int j = 0; j < columns.length; j++) {
				dataTypes.add(columns[j].dataType);
			}
		}
	}

}
