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
import java.util.List;

import org.beequeue.piles.LazyList;
import org.beequeue.sql.DalException;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlFields;
import org.beequeue.sql.SqlMorph;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;

public class TableMap<T>  {
  
  String name;
  Class<T> mappedToClass = null;
  FieldMap<? extends EnumFactory<T>> factory = null;
  List<FieldMap> fields;
 
  public TableMap(String name, Class<T> mappedToClass, List<FieldMap> fields) {
    super();
    this.name = name;
    this.mappedToClass = mappedToClass;
    this.fields = fields;
  }
  
  public TableMap(String name, FieldMap<? extends EnumFactory<T>> factory, List<FieldMap> fields) {
    super();
    this.name = name;
    this.factory = factory;
    this.fields = fields;
  }

  public T newObject(Record record){
    T newInstance;
    if( mappedToClass != null ){
      try {
        newInstance = mappedToClass.newInstance();
      } catch (Exception e) {
        throw new DalException(e);
      }
    }else{
      EnumFactory<T> enumFactory = record.get(factory);
      newInstance = enumFactory.newObject();
    }
    record.build(newInstance);
    return newInstance;
  }
  
  
  public List<FieldMap> getFields() {
    return fields;
  }
  
  public String getName() {
    return name;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TableMap) {
      TableMap that = (TableMap) obj;
      return name.equals(that.name);
      
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  public <I> Update<I> delete(FieldMap<I> key){
    return delete(WhereCondition.build(key));
  }
  
  public <I> Update<I> delete(WhereCondition<I> whereCondition){
    return new Update<I>("delete from "+getName()+whereCondition.toString(), whereCondition.prepare());
  }

  public <I> Update<I> update(FieldSet... selector){
    List<FieldMap> updateFields = FieldMap.match(getFields(), selector);
    List<FieldMap> primaryKeys = FieldMap.match(getFields(), Fields.PRIMARY_KEY);
    
    return update(updateFields, primaryKeys);
  }


  public <I> Update<I> update(List<FieldMap> updateFields, List<FieldMap> primaryKeys) {
    return update(WhereCondition.build(primaryKeys),updateFields);
  }

  public <I> Update<I> update(WhereCondition<?> where, FieldMap... updateFields ) {
    return update(where, new LazyList<FieldMap>(updateFields) );
  }
  
  public <I> Update<I> update(WhereCondition<?> where, List<FieldMap> updateFields ) {
    List<FieldMap> bindFields = new ArrayList<FieldMap>(updateFields);
    bindFields.addAll(where.getFieldMaps());
    
    SqlPrepare<I> prepare = SqlHelper.prepare(bindFields);
    SqlFields<I> sqlFields = new SqlFields<I>(updateFields);

    return new Update<I>("update  "+getName()+" set ===" + where, prepare, sqlFields);
  }

  public <I> Update<I> insert( FieldSet... selector){
    List<FieldMap> updateFields = FieldMap.match(getFields(), selector);
    SqlPrepare<I> prepare = SqlHelper.prepare(updateFields);
    SqlFields<I> sqlFields = new SqlFields<I>(updateFields);
    return new Update<I>("insert into  "+getName()+" (%%%) values (???)", prepare, sqlFields);
  }

  @SuppressWarnings("unchecked")
  public  <V,I> Select<V,I> selectValue( FieldMap<I> key, String whereField, final FieldMap<V> val){
    return selectValue(SqlHelper.where(key, whereField), val);
  }
  public <V, I> Select<V, I> selectValue(WhereCondition<I> where, final FieldMap<V> val) {
    JdbcFactory<V, ? super I> factory = new JdbcFactory<V, I>(){
      public V newInstance(ResultSet rs, I input, Index idx) throws SQLException {
        return val.dataType.get(rs, idx);
      }};
    return masterSelect(where, new LazyList<FieldMap>(val), factory);
  }

  public <I> Select<Record,I> selectRecord( FieldMap<I> key, String whereField, FieldMap... anyFields){
    return selectRecord(SqlHelper.where(key, whereField), anyFields);
  }
  public <I> Select<Record, I> selectRecord(WhereCondition<I> where, FieldMap... anyFields) {
    return selectRecord(where, new LazyList<FieldMap>(anyFields));
  }

  public <I> Select<Record, I> selectRecord(FieldMap<I> key, String whereField, FieldSet... selector) {
    return selectRecord(SqlHelper.where(key, whereField), selector);
  }
  
  public <I> Select<Record, I> selectRecord(WhereCondition<I> where, FieldSet... selector) {
    return selectRecord(where, FieldMap.match(getFields(), selector)) ;
  }
  
  public <I> Select<Record, I> selectRecord(FieldMap<I> key, String whereField, final List<FieldMap> fieldList) {
    return selectRecord(SqlHelper.where(key, whereField), fieldList);
  }
  
  public <I> Select<Record, I> selectRecord(WhereCondition<I> where, final List<FieldMap> lazyList) {
    JdbcFactory<Record, ? super I> factory = new JdbcFactory<Record, I>(){
      public Record newInstance(ResultSet rs, I input, Index idx) throws SQLException {
        return SqlHelper.buildRecord(lazyList, rs, idx);
      }};
    return masterSelect(where, lazyList, factory);
  }
  
  public <I> Select<T, I> select(FieldMap<I> key, String whereField, FieldSet... selector) {
    return select(SqlHelper.where(key, whereField), selector);
  }

  public <I> Select<T, I> select(WhereCondition<I> where, FieldSet... selector) {
    final List<FieldMap> selectFields = FieldMap.match(getFields(), selector);
    JdbcFactory<T, ? super I> factory = new  JdbcFactory<T, I>(){
      public T newInstance(ResultSet rs, I input, Index idx) throws SQLException {
        Record record = SqlHelper.buildRecord(selectFields, rs, idx);
        return TableMap.this.newObject(record);
      }
    };
    return masterSelect(where, selectFields, factory);
  }

  private <I, O> Select<O, I> masterSelect(WhereCondition<I> whereCondition, final List<FieldMap> selectFields, JdbcFactory<O, ? super I> factory) {
    SqlMorph<? super I> sqlTransition = new SqlFields<I>(selectFields);
    return new Select<O, I>("select %%% from " +getName() + whereCondition, factory, whereCondition.prepare(), sqlTransition);
  }

  @Override
  public String toString() {
    return getName();
  }
  
  
  
}
