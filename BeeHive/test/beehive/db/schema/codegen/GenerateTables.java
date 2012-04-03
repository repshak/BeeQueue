package beehive.db.schema.codegen;



import java.io.FileInputStream;

import org.junit.Test;

import beehive.db.schema.clay.Clay;

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
