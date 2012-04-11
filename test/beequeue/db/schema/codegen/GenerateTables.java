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

import org.junit.Test;

import beequeue.db.schema.clay.Clay;

public class GenerateTables  {

  @Test
  public void test() throws Exception{
	  Clay clay = new Clay(new FileInputStream("design/dbModel.clay"));
      System.out.println(clay);
	  
//	  List<Table> list = MySqlDao.instance.get();
//    GroovyTemplate groovyTemplate = new GroovyTemplate(getClass(),"TableMapCode.gtemplate");
//    for (Table table : list) {
//      File file = new File("test/codegen/db/"+table.getName()+".java");
//      groovyTemplate.generate(new LazyMap<String, Object>().add("table", table), file);
//    }

  }



}
