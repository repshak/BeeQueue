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
package org.beequeue.piles.flock;

public class Sortings {
    /**
     * Sorting and  binary search are adapted from java, java in turn take them from:
     * 
     * Sorts the specified array of longs into ascending numerical order.
     * The sorting algorithm is a tuned quicksort, adapted from Jon
     * L. Bentley and M. Douglas McIlroy's "Engineering a Sort Function",
     * Software-Practice and Experience, Vol. 23(11) P. 1249-1265 (November
     * 1993).  This algorithm offers n*log(n) performance on many data sets
     * that cause other quicksorts to degrade to quadratic performance.
     * 
     */
    public static <T,V> void sort(Flock<T> list, FlockComparator<T,V> comparator){
      sort1(list, comparator, 0, list.size());
    }
    
    /**
     * Sorts the specified sub-array of doubles into ascending order.
     */
    private static <T,V> void sort1(Flock<T> x, FlockComparator<T,V> comparator, int off, int len) {
      // Insertion sort on smallest arrays
      if (len < 7) {
        for (int i = off; i < len + off; i++)
          for (int j = i; j > off && comparator.compare(x, j - 1, j) > 0; j--)
            x.swap(j, j - 1);
        return;
      }
    
      // Choose a partition element, v
      int m = off + (len >> 1);       // Small arrays, middle element
      if (len > 7) {
        int l = off;
        int n = off + len - 1;
        if (len > 40) { // Big arrays, pseudomedian of 9
          int s = len / 8;
          l = med3(x, comparator, l, l + s, l + 2 * s);
          m = med3(x, comparator, m - s, m, m + s);
          n = med3(x, comparator, n - 2 * s, n - s, n);
        }
        m = med3(x, comparator, l, m, n); // Mid-size, med of 3
      }
      V v = comparator.get(x,m);
    
      // Establish Invariant: v* (<v)* (>v)* v*
      int a = off, b = a, c = off + len - 1, d = c;
      while (true) {
        int rc ;
        while (b <= c && (rc =comparator.compareIt(x,b,v)) <= 0) {
          if ( rc == 0 ){
            x.swap(a++, b);
          }
          b++;
        }
        while (c >= b && (rc =comparator.compareIt(x,c,v)) >= 0) {
          if( rc == 0 ){
            x.swap(c, d--);
          }
          c--;
        }
        if (b > c)
          break;
        x.swap(b++, c--);
      }
    
      // Swap partition elements back to middle
      int s, n = off + len;
      s = Math.min(a - off, b - a);
      x.vecswap(off, b - s, s);
      s = Math.min(d - c, n - d - 1);
      x.vecswap(b, n - s, s);
    
      // Recursively sort non-partition-elements
      if ((s = b - a) > 1)
        sort1(x, comparator, off, s);
      if ((s = d - c) > 1)
        sort1(x, comparator, n - s, s);
    }


    /**
     * Returns the index of the median of the three indexed doubles.
     */
    private static <T,V> int med3(Flock<T> x, FlockComparator<T,V> comparator, int a, int b, int c) {
      return (comparator.compare(x,a, b) < 0 ? 
          (comparator.compare(x,b, c) < 0 ? b : comparator.compare(x,a, c) < 0 ? c : a) : 
          (comparator.compare(x,b, c) > 0 ? b : comparator.compare(x,a, c) > 0 ? c : a));
    }
    
    public static class BinarySearchStatus { 
      public final int position, low, high;
      public BinarySearchStatus(int position, int low, int high) {
        this.position = position;
        this.low = low;
        this.high = high;
      }
      public boolean isNotFound(){
        return position < 0;
      }
      public int getPosition(){
        return isNotFound() ? -position-1 : position ;
      }
    }
    
    public static <T,V> int binarySearch(Flock<T> list, FlockComparator<T,V> comparator, V key, int low,int high) {
      return binarySearchStatus(list, comparator, key, low, high).position;
    }
    public static <T,V> BinarySearchStatus binarySearchStatus(Flock<T> list, FlockComparator<T,V> comparator, V key, int low,int high) {
      while (low <= high) {
        int mid = (low + high) >> 1;
        int cmp = comparator.compareIt(list,mid, key);
        if (cmp < 0)
          low = mid + 1;
        else if (cmp > 0)
          high = mid - 1;
        else
          return new BinarySearchStatus(mid,low,high); // key found
      }
      return new BinarySearchStatus(-(low + 1),low,high);  // key not found.
    }
    
    public static <T,V> BinarySearchStatus binarySearchStatus(Flock<T> list, FlockComparator<T,V> comparator, V key) {
      return binarySearchStatus(list, comparator, key, 0, list.size()-1);
    }
    public static <T,V> int binarySearch(Flock<T> list, FlockComparator<T,V> comparator, V key){
      return binarySearch(list, comparator, key, 0, list.size()-1);
    }
    
    public static <T,V> int findFirst(Flock<T> list, final FlockComparator<T,V> comparator, V key){
      FlockComparator<T,V> lowerBoundaryComporator = new FlockComparator<T,V>(){
        public int compare(Flock<T> flock, int at1, int at2) {
          return comparator.compare(flock, at1, at2);
        }
        
        public int compareIt(Flock<T> flock, int at, V val) {
          int rc = comparator.compareIt(flock, at, val);
          return rc == 0 ? ( at==0 || 0 != comparator.compareIt(flock, at-1, val) ? 0 : 1 ): rc ;
        }
        
        public int compareValue(V v1, V v2) {
          return comparator.compareValue(v1, v2);
        }
        
        public V get(Flock<T> flock, int at) {
          return comparator.get(flock, at);
        }};
        return binarySearch(list, lowerBoundaryComporator, key);
    }
    
    public static <T,V> int findFirstOld(Flock<T> list, final FlockComparator<T,V> comparator, V key){
      return goUp(list, comparator, binarySearch(list, comparator, key), key);
    }

    public static <T,V> int findLast(Flock<T> list, final FlockComparator<T,V> comparator, V key){
      FlockComparator<T,V> upperBoundaryComporator = new FlockComparator<T,V>(){
        public int compare(Flock<T> flock, int at1, int at2) {
          return comparator.compare(flock, at1, at2);
        }
        
        public int compareIt(Flock<T> flock, int at, V val) {
          int rc = comparator.compareIt(flock, at, val);
          return rc == 0 ? ( at==flock.size() - 1 || 0 != comparator.compareIt(flock, at+1, val) ? 0 : -1 ): rc ;
        }
        
        public int compareValue(V v1, V v2) {
          return comparator.compareValue(v1, v2);
        }
        
        public V get(Flock<T> flock, int at) {
          return comparator.get(flock, at);
        }};
        return binarySearch(list, upperBoundaryComporator, key);
    }
    
    public static <T,V> int findLastOld(Flock<T> list, FlockComparator<T,V> comparator,  V key){
      return goDown(list, comparator, binarySearch(list, comparator, key),key);
    }
    
    private static <T,V> int goUp(Flock<T> list, FlockComparator<T,V> comparator,  int i,V key) {
      while(i > 0 && comparator.compareIt(list,i-1,key)==0){
        i--;
      }
      return i;
    }
    
    private static <T,V> int goDown(Flock<T> list, FlockComparator<T,V> comparator,  int i, V key) {
      while(i >= 0 && i < (list.size()-1) && comparator.compareIt(list,i+1,key)==0){
        i++;
      }
      return i;
    }
    
    public static <T,V> int[] findRange(Flock<T> list, FlockComparator<T,V> comparator,  V key){
      int i = binarySearch(list, comparator, key);
      if(i < 0){
        return null;
      }else{
        return new int[]{
            goUp(list, comparator, i,key),
            goDown(list, comparator, i,key)
        };
      }
    }
}
