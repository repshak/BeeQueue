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
package beehive.util;


import java.io.Serializable;

public class Range<T extends Comparable<T>> implements Comparable<Range<T>>,Serializable{
  private static final long serialVersionUID = 1L;
  
  public final T start;
  public final T end ;
  
  public Range(T start, T end) {
    assert start != null ;
    assert end != null ;
    assert less(end,start) : "end:"+end+" is smaller than start:" + start ;
    this.start = start;
    this.end = end;
  }
  
  boolean less(T o1, T o2){ 
      return o1.compareTo(o2) < 0 ;
  }
  boolean greater(T o1, T o2){
      return o1.compareTo(o2) > 0 ;
  }

  public boolean overlap(Range<T> that){
      return less( this.start,that.end) && less(that.start,this.end); 
  }

  public String toString() {
      return "Range: ["+start+"-"+end+"]";
  }

  public boolean contains(Range<T> that) { 
    return !less(that.start,this.start) && !greater(that.end,this.end) ;
  }

  public boolean contains(T point) { 
      return !less(point,this.start) && !greater(point,this.end) ;
  }
  
  public boolean containsStartInclusive(T point) { 
    return !less(point,this.start) && less(point,this.end) ;
  }

  /**
   * @return the start
   */
  public T getStart() {
    return start;
  }

  /**
   * @return the end
   */
  public T getEnd() {
    return end;
  }

  public int compareTo(Range<T> that) {
    int rc = this.getStart().compareTo(that.getStart()) ;
    if(rc == 0 ){
      rc = this.getEnd().compareTo(that.getEnd()) ;
    }
    return rc;
  }
}

