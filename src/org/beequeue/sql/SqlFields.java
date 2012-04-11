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
/**
 * 
 */
package org.beequeue.sql;

import java.util.List;

import org.beequeue.piles.LazyList;
import org.beequeue.piles.Piles;
import org.beequeue.util.Morph;
import org.beequeue.util.StringMorph;

/**
 *
 * 
 */

public class SqlFields<T> implements  SqlMorph<T>{
  private static final String COMMA = ", ";
  
  private static Morph<Object,String> VALUE_PART_OF_INSERT = new Morph<Object,String>(){
    public String doIt(Object input) {
      return "?";
    }
  };

  private static Morph<Object,String> SET_PART_OF_UPDATE = new Morph<Object,String>(){
    public String doIt(Object input) {
      return String.valueOf(input)+ " = ?";
    }
  };
  
  public static class Alias implements Morph<Object,String>
  {
    private String alias;
    
    public Alias(String alias) {
      this.alias = alias;
    }

    public String doIt(Object input) {
      return alias + "." + String.valueOf(input);
    }
  }  

  private List<? extends Object> fields ;
  private Morph<Object,String> morph;
  
  public SqlFields(List<?> fields) {
    this.fields = fields;
    this.morph = StringMorph.TO_STRING;
  }
  
  public SqlFields(List<?> fields, Morph<Object,String> morph) {
    this.fields = fields;
    this.morph = morph;
  }
  
  public SqlFields<T> applyAlias(String alias){
    fields = LazyList.morph(new Alias(alias), fields);
    return this;
  }

  public String change(String sql, T input) {
    return sql
    .replace("%%%", Piles.buildListString(fields, COMMA, morph))
    .replace("???", Piles.buildListString(fields, COMMA, VALUE_PART_OF_INSERT))
    .replace("===", Piles.buildListString(fields, COMMA, SET_PART_OF_UPDATE))
    ;
  }
}
