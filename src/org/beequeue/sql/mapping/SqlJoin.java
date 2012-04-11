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
package org.beequeue.sql.mapping;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.beequeue.piles.LazyList;
import org.beequeue.piles.Piles;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.util.Morph;
import org.beequeue.util.Quadruple;
import org.beequeue.util.StringMorph;
import org.beequeue.util.Triple;
import org.beequeue.util.Tuple;

public class SqlJoin  {
  
  private List<Tuple<TableMap,List<FieldMap>>> joinedTables = new ArrayList<Tuple<TableMap,List<FieldMap>>>();
  private StringBuffer whereSuffix = new StringBuffer();
  
  public SqlJoin t(TableMap table, FieldMap ... fieldMaps ){
    List<FieldMap> asList = Arrays.asList(fieldMaps);
    for (FieldMap map : asList) {
      assert table.getName().equals(map.table): "field map:"+map+" does not match table:"+table;
    }
    Tuple<TableMap, List<FieldMap>> t = new Tuple<TableMap, List<FieldMap>>(table,asList);
    joinedTables.add(t);
    return this;
  }
  
  public SqlJoin join(FieldMap from, FieldMap to){
    return join(this.joinedTables.size()-1,from, to);
  }
  
  public SqlJoin join(int fromIndex, FieldMap from, FieldMap to){
    if(whereSuffix.length() > 0){
      whereSuffix.append(WhereCondition.AND);
    }
    whereSuffix.append(tableAlias(fromIndex));
    whereSuffix.append(".");
    whereSuffix.append(from.name);
    whereSuffix.append("=");
    whereSuffix.append(tableAlias(this.joinedTables.size()));
    whereSuffix.append(".");
    whereSuffix.append(to.name);
    return this;
  }
    @SuppressWarnings("unchecked")
  public SqlJoin t(TableMap table, FieldSet ... selector){
    List<FieldMap> asList = FieldMap.match(table.getFields(), selector);
    Tuple<TableMap, List<FieldMap>> t = new Tuple<TableMap, List<FieldMap>>(table,asList);
    joinedTables.add(t);
    return this;
  }
  
  public <I> Select<Record[],I> select(String where){
      WhereCondition<I> whereCondition = WhereCondition.build(where);
      return new Select<Record[], I>(buildSql(whereCondition),buildFactory() ,whereCondition.prepare() );
  }

  public <I> Select<Record[],I> select(String where, final FieldMap<I> conditionMap){
    WhereCondition<I> whereCondition = WhereCondition.build(where,conditionMap);
    if( where == null ){
      resetWhere(whereCondition, conditionMap);
    }
    return new Select<Record[], I>(buildSql(whereCondition),buildFactory() ,whereCondition.prepare() );
  }

  @SuppressWarnings("unchecked")
  private <I> void resetWhere(WhereCondition<I> whereCondition, FieldMap... conditionMaps) {
    LazyList<String> whereExps = LazyList.morph(WhereCondition.TO_EQ, conditionMaps);
    final int index = this.joinedTables.size()-1;
    Morph<String, String> morph = new Morph<String, String>(){
      public String doIt(String input) {
        return tableAlias(index)+"."+input;
      }};
    whereCondition.setWhere(WhereCondition.combine(morph, whereExps));
  }
  
  public <I1,I2> Select<Record[],Tuple<I1,I2>> select(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2) {
    WhereCondition<Tuple<I1, I2>> whereCondition = WhereCondition.build(where,condMap1,condMap2);
    if( where == null ){
      resetWhere(whereCondition, condMap1, condMap2);
    }
    return new Select<Record[], Tuple<I1,I2>>(buildSql(whereCondition),buildFactory() ,whereCondition.prepare()  );
  }
  
  public <I1,I2,I3> Select<Record[],Triple<I1,I2,I3>> select(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2, final FieldMap<I3> condMap3) {
    WhereCondition<Triple<I1, I2, I3>> whereCondition = WhereCondition.build(where,condMap1,condMap2,condMap3);
    if( where == null ){
      resetWhere(whereCondition, condMap1, condMap2, condMap3);
    }
    return new Select<Record[], Triple<I1,I2,I3>>(buildSql(whereCondition),buildFactory() ,whereCondition.prepare()  );
  }
  
  public <I1,I2,I3,I4> Select<Record[],Quadruple<I1,I2,I3,I4>> select(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2, final FieldMap<I3> condMap3, final FieldMap<I4> condMap4) {
    WhereCondition<Quadruple<I1, I2, I3, I4>> whereCondition = WhereCondition.build(where,condMap1,condMap2,condMap3,condMap4);
    if( where == null ){
      resetWhere(whereCondition, condMap1, condMap2, condMap3, condMap4);
    }
    return new Select<Record[], Quadruple<I1,I2,I3,I4>>(buildSql(whereCondition),buildFactory() ,whereCondition.prepare()  );
  }

  private String buildSql(WhereCondition<?> whereCondition) {
    List<String> selectKludge = new ArrayList<String>();  
    List<String> fromKludge = new ArrayList<String>();  
    for (int i = 0; i < this.joinedTables.size(); i++) {
      Tuple<TableMap, List<FieldMap>> tableFields = this.joinedTables.get(i);
      String alias = tableAlias(i);
      fromKludge.add(tableFields.o1.getName()+" "+alias);
      for (FieldMap field : tableFields.o2) {
        selectKludge.add(alias+"."+field.name);
      }
    }
    if( whereCondition.toString().length() > 0 && whereSuffix.length() > 0 ){
      whereCondition.append(WhereCondition.AND);
      whereCondition.append(whereSuffix.toString());
    }
    return "select "+Piles.buildListString(selectKludge, ", ", StringMorph.TO_STRING) +
    " from " +Piles.buildListString(fromKludge, ", ", StringMorph.TO_STRING) + whereCondition.toString()  ;
  }

  private String tableAlias(int i) {
    return "t"+i;
  }

  private <I> JdbcFactory<Record[], ? super I> buildFactory() {
    JdbcFactory<Record[], ? super I> factory = new JdbcFactory<Record[], Object>(){
      
      @SuppressWarnings("unchecked")
      public Record[] newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
        Record[] records = new Record[joinedTables.size()];
        for (int i = 0; i < joinedTables.size(); i++) {
          Tuple<TableMap, List<FieldMap>> tableFields = joinedTables.get(i);
          records[i] = new Record();
          for (FieldMap field : tableFields.o2) {
            records[i].put(field,field.dataType.get(rs, idx));
          }
        }
        return records ;
      }};
      return factory;
  }
}
