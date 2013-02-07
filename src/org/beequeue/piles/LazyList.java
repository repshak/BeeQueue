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
package org.beequeue.piles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.beequeue.util.BeeOperation;
import org.beequeue.util.StringMorph;


public class LazyList<T> extends ArrayList<T> {
  private static final long serialVersionUID = 1L;

  public LazyList() {
    super();
  }

  public LazyList(Collection<? extends T> c) {
    super(c);
  }
  
  public LazyList(T... args) {
    super(Arrays.asList(args));
  }

  public LazyList(Iterator<? extends T> args) {
    while(args.hasNext()){
      add(args.next());
    }
  }
  
  public LazyList(Enumeration<? extends T> args) {
    while(args.hasMoreElements()){
      add(args.nextElement());
    }
  }

  public static <O,T> LazyList<T> morph(BeeOperation<? super O,? extends T > morph, Collection<? extends O> others) {
    return new LazyList<T>().morphInto(morph, others);
  }
  
  public static <O,T> LazyList<T> morph(BeeOperation<? super O,? extends T> morph, O... others) {
    return new LazyList<T>().morphInto(morph, others);
  }
  
  public static <O,T> LazyList<T> morph(BeeOperation<? super O,? extends T> morph, Iterator<? extends O> others) {
    return new LazyList<T>().morphInto(morph, others);
  }
  
  public static <O,T> LazyList<T> morph(BeeOperation<? super O,? extends T> morph, Enumeration<? extends O> others) {
    return new LazyList<T>().morphInto(morph, others);
  }

  public <O> LazyList<T> morphInto(BeeOperation<? super O,? extends  T> morph, O... others) {
    return morphInto(morph, Arrays.asList(others));
  }

  public <O> LazyList<T> morphInto(BeeOperation<? super O,? extends T> morph, Collection<? extends O> others) {
    for (O o : others) {
      add(morph.execute(o));
    }
    return this;
  }
  
  public <O> LazyList<T> morphInto(BeeOperation<? super O,? extends T> morph, Iterator<? extends O> others) {
    while (others.hasNext()) {
      add(morph.execute(others.next()));
    }
    return this;
  }
  
  public <O> LazyList<T> morphInto(BeeOperation<? super O,? extends T> morph, Enumeration<? extends O> others) {
    while (others.hasMoreElements()) {
      add(morph.execute(others.nextElement()));
    }
    return this;
  }

  public LazyList<T> append(T... args){
    List<T> list = Arrays.asList(args);
    return append(list);
  }

  public LazyList<T> append(Collection<? extends T> list) {
    addAll(list);
    return this;
  }



  public String toString(String separator, BeeOperation<Object,String> morph ){
    return Piles.buildListString(this, separator, morph);
  }

  public String toString(String separator){
    return toString(separator,new StringMorph.ToString<Object>());
  }
}
