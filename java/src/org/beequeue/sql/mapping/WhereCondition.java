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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.beequeue.piles.LazyList;
import org.beequeue.piles.Piles;
import org.beequeue.sql.Index;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.util.BeeOperation;
import org.beequeue.util.Quadruple;
import org.beequeue.util.BeeStringTransformation;
import org.beequeue.util.Triple;
import org.beequeue.util.Tuple;

public class WhereCondition<T> {
  public static final String AND = " and ";
  
  public static final BeeOperation<FieldMap, String> TO_EQ = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " = ?";
    }
  };
  public static final BeeOperation<FieldMap, String> TO_GT = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " > ?";
    }
  };
  public static final BeeOperation<FieldMap, String> TO_LT = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " < ?";
    }
  };
  public static final BeeOperation<FieldMap, String> TO_GT_EQ = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " >= ?";
    }
  };
  public static final BeeOperation<FieldMap, String> TO_LT_EQ = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " <= ?";
    }
  };
  public static final BeeOperation<FieldMap, String> TO_NE = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " <> ?";
    }
  };

  public static final BeeOperation<FieldMap, String> TO_IS_NULL = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " is null";
    }
  };

  public static final BeeOperation<FieldMap, String> TO_IS_NOT_NULL = new BeeOperation<FieldMap, String>(){
    public String execute(FieldMap input) {
      return input.name + " is not null";
    }
  };


  private String where;
  private SqlPrepare<T> prepare;
  private List<FieldMap> fieldMaps ;

  private WhereCondition(String where) {
    this.where = where ;
    this.prepare = null ;
    this.fieldMaps = null;
  }

  private WhereCondition(String where, SqlPrepare<T> prepare, List<FieldMap> fieldMaps) {
    this.where = where;
    this.prepare = prepare;
    this.fieldMaps = fieldMaps;
  }

  public static <I> WhereCondition<I> build(String where){
    return new WhereCondition<I>(where);
  }

  @SuppressWarnings("unchecked")
  public static WhereCondition<Record> dynamic(final Map<FieldMap,BeeOperation<FieldMap,String>> conditionMap){
    final LazyList<FieldMap> fieldMaps = new LazyList<FieldMap>(conditionMap.keySet());
    WhereCondition<Record> whereCondition = new WhereCondition<Record>("",new SqlPrepare<Record>(){
      public void invoke(PreparedStatement pstmt, Record input, Index idx) throws SQLException {
        for (FieldMap fieldMap : fieldMaps) {
          fieldMap.dataType.set(pstmt, idx, input.get(fieldMap));
        }
      }}, fieldMaps);
    List<String> conditions = new ArrayList<String>();
    for (FieldMap fieldMap : fieldMaps) {
      BeeOperation<FieldMap, String> morph = conditionMap.get(fieldMap);
      if(morph == null){
        morph = TO_EQ ;
      }
      conditions.add(morph.execute(fieldMap));
    }
    whereCondition.append(combine(BeeStringTransformation.PASS_THRU, conditions ));
    return whereCondition;
  }

  @SuppressWarnings("unchecked")
  public static <I> WhereCondition<I> build(String where, final FieldMap<I> condMap){
    WhereCondition<I> whereCondition = new WhereCondition<I>(where,new SqlPrepare<I>(){
          public void invoke(PreparedStatement pstmt, I input, Index idx) throws SQLException {
            condMap.dataType.set(pstmt, idx, input);
          }},new LazyList<FieldMap>(condMap));
    return whereCondition;
  }

  @SuppressWarnings("unchecked")
  public static <I> WhereCondition<I> build(final FieldMap<I> condMap){
    WhereCondition<I> whereCondition = new WhereCondition<I>(combine(TO_EQ, condMap),new SqlPrepare<I>(){
          public void invoke(PreparedStatement pstmt, I input, Index idx) throws SQLException {
            condMap.dataType.set(pstmt, idx, input);
          }},new LazyList<FieldMap>(condMap));
    return whereCondition;
  }

  public static <T> String combine(BeeOperation<? super T, String> morph, T... condMaps) {
    return combine(morph, Arrays.asList(condMaps));
  }

  public static <T> String combine(BeeOperation<? super T, String> morph, List<T> asList ) {
    return Piles.buildListString(asList, AND , morph);
  }

  @SuppressWarnings("unchecked")
  public static <I1,I2> WhereCondition<Tuple<I1,I2>> build(FieldMap<I1> condMap1, FieldMap<I2> condMap2) {
    return build(combine(TO_EQ, condMap1, condMap2), condMap1, condMap2);
  }
  
  public static <I1,I2> WhereCondition<Tuple<I1,I2>> build(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2) {
    SqlPrepare<Tuple<I1, I2>> prepare = new SqlPrepare<Tuple<I1,I2>>(){
  
      public void invoke(PreparedStatement pstmt, Tuple<I1, I2> input, Index idx) throws SQLException {
        condMap1.dataType.set(pstmt, idx, input.o1);
        condMap2.dataType.set(pstmt, idx, input.o2);
      }};
    return new WhereCondition<Tuple<I1,I2>>(where,prepare,new LazyList<FieldMap>(condMap1,condMap2));
  }
  
  @SuppressWarnings("unchecked")
  public static <I1,I2,I3> WhereCondition<Triple<I1,I2,I3>> build(FieldMap<I1> condMap1, FieldMap<I2> condMap2, FieldMap<I3> condMap3) {
    return build(combine(TO_EQ, condMap1, condMap2, condMap3), condMap1, condMap2, condMap3);
  }
  public static <I1,I2,I3> WhereCondition<Triple<I1,I2,I3>> build(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2, final FieldMap<I3> condMap3) {
    SqlPrepare<Triple<I1, I2, I3>> prepare = new SqlPrepare<Triple<I1,I2,I3>>(){
  
      public void invoke(PreparedStatement pstmt, Triple<I1, I2, I3> input, Index idx) throws SQLException {
        condMap1.dataType.set(pstmt, idx, input.o1);
        condMap2.dataType.set(pstmt, idx, input.o2);
        condMap3.dataType.set(pstmt, idx, input.o3);
      }};
    return new WhereCondition<Triple<I1,I2,I3>>(where,prepare,new LazyList<FieldMap>(condMap1,condMap2,condMap3));
  }
  
  @SuppressWarnings("unchecked")
  public static <I1,I2,I3,I4> WhereCondition<Quadruple<I1,I2,I3,I4>> build(FieldMap<I1> condMap1, FieldMap<I2> condMap2, FieldMap<I3> condMap3, FieldMap<I4> condMap4) {
    return build(combine(TO_EQ, condMap1, condMap2, condMap3, condMap4), condMap1, condMap2, condMap3, condMap4);
  }
  public static <I1,I2,I3,I4> WhereCondition<Quadruple<I1,I2,I3,I4>> build(String where, final FieldMap<I1> condMap1, final FieldMap<I2> condMap2, final FieldMap<I3> condMap3, final FieldMap<I4> condMap4) {
    SqlPrepare<Quadruple<I1, I2, I3, I4>> prepare = new SqlPrepare<Quadruple<I1,I2,I3,I4>>(){
      public void invoke(PreparedStatement pstmt, Quadruple<I1, I2, I3, I4> input, Index idx) throws SQLException {
        condMap1.dataType.set(pstmt, idx, input.o1);
        condMap2.dataType.set(pstmt, idx, input.o2);
        condMap3.dataType.set(pstmt, idx, input.o3);
        condMap4.dataType.set(pstmt, idx, input.o4);
      }};
    return new WhereCondition<Quadruple<I1,I2,I3,I4>>(where,prepare,new LazyList<FieldMap>(condMap1,condMap2,condMap3,condMap4));
  }

  
  public SqlPrepare<T> prepare() {
    return prepare;
  }

  @Override
  public String toString() {
    return where == null || where.trim().equals("") ?  "" : " where "+where;
  }

  public static <I> WhereCondition<I> build(List<FieldMap> fields) {
    SqlPrepare<I> prepare = SqlHelper.prepare(fields);
    return new WhereCondition<I>(combine(TO_EQ, fields),prepare,new LazyList<FieldMap>(fields));
  }

  public static <I> WhereCondition<I> none() {
    return new WhereCondition<I>(null);
  }

  public List<FieldMap> getFieldMaps() {
    return fieldMaps;
  }
  
  public String getWhere() {
    return where;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public WhereCondition<T> append(String whereFragment){
    where += whereFragment;
    return this;
  }
}
