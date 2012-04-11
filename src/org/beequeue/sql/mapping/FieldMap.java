/** ==== BEGIN LICENSE =====
   Copyright 2004-2007 - Wakeup ORM

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
import java.sql.ResultSet;
import java.util.List;

import org.beequeue.piles.LazyList;
import org.beequeue.sql.DalException;
import org.beequeue.sql.Index;
import org.beequeue.util.BeanUtil;
import org.beequeue.util.Filter;


public class FieldMap<T> {
  public class OneFieldSet implements FieldSet {

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof FieldMap.OneFieldSet) {
        FieldMap.OneFieldSet that = (OneFieldSet) obj;
        return getName().equals(that.getName()) && this.getTable().equals(that.getTable());
      }
      return false;
    }
    
    public String getName(){
      return FieldMap.this.name;
    }

    public String getTable(){
      return FieldMap.this.table;
    }

    @Override
    public int hashCode() {
      return getName().hashCode();
    }

  }

  private static final class FieldFilter implements Filter<FieldMap> {
    private final FieldSet target;

    private FieldFilter(FieldSet target) {
      this.target = target;
    }

    public Boolean doIt(FieldMap input) {
      return input.criteria.contains(target);
    }
  }

  final public String table;

  final public String name;

  final public String beanPath;

  final public DataType<T> dataType;

  final private List<FieldSet> criteria;
  
  final public FieldSet set ;

  public FieldMap(String table, String name, String beanPath,
      DataType<T> typeMap, FieldSet... criteria) {
    super();
    this.table = table;
    this.name = name;
    this.beanPath = beanPath;
    this.dataType = typeMap;
    this.set = new OneFieldSet();
    this.criteria = new LazyList<FieldSet>(criteria).append(set);
  }

  public static List<FieldMap> match(List<FieldMap> fields, FieldSet... targets) {
    List<FieldMap> keepMatches = fields;
    for (int i = 0; i < targets.length; i++) {
      FieldSet target = targets[i];
      keepMatches = Filter.Util.keepMatches(keepMatches,
          new FieldFilter(target));
    }
    return keepMatches;
  }

  public static List<FieldMap> exclude(List<FieldMap> fields,
      FieldSet... targets) {
    List<FieldMap> throwAwayMatches = fields;
    for (int i = 0; i < targets.length; i++) {
      FieldSet target = targets[i];
      throwAwayMatches = Filter.Util.throwAwayMatches(throwAwayMatches,
          new FieldFilter(target));
    }
    return throwAwayMatches;
  }

  public void populateBean(Object bean, ResultSet rs, Index idx)
      throws DalException {
    try {
      T t = dataType.get(rs, idx);
      populateBean(bean, t);
    } catch (Exception e) {
      throw new DalException("populateBean: bean=" + bean + " field=" + name, e);
    }
  }

  public void populateBean(Object bean, T t) {
    BeanUtil.populateProperty(bean, beanPath, t);
  }

  public void bindValue(PreparedStatement pstmt, Index idx, Object bean)
      throws DalException {
    try {
      T extractProperty = extractProperty(bean);
      dataType.set(pstmt, idx, extractProperty);
    } catch (Exception e) {
      throw new DalException("bindValue: bean=" + bean + " field=" + name, e);
    }
  }

  @SuppressWarnings("unchecked")
  public T extractProperty(Object bean) {
    T extractProperty = (T) BeanUtil.extractProperty(bean, beanPath);
    return extractProperty;
  }

  @Override
  public String toString() {
    return table + "." + name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FieldMap<?>) {
      FieldMap<?> that = (FieldMap<?>) obj;
      return this.name.equals(that.name) && this.table.equals(that.table);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

}
