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
package beehive.piles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Table<T> {
  //TODO rewrite using Map<Long,T> 
  
  List<List<T>> rows = new ArrayList<List<T>>();
  T defaultValue = null;
  
  
  public Table() {
  }
  

  public Table(T defaultValue) {
    this.defaultValue = defaultValue;
  }


  public T ensure(int row, int col){
    ensureInternal(row+1,col+1);
    return rows.get(row).get(col);
  }

  public T get(int row, int col){
    return rows.get(row).get(col);
  }
  
  public void  set(int row, int col, T val){
    ensureInternal(row+1,col+1);
    rows.get(row).set(col,val);
  }

  public void  fillDown(int row, int col, Collection<T> collection){
    ensureInternal(row+collection.size()+1,col+1);
    for (Iterator<T> iter = collection.iterator(); iter.hasNext();) {
      rows.get(row++).set(col,iter.next());
    }
  }
  
  public int getRowCount(){
    return rows.size();
  }
  
  public int getColumnCount(){
    return rows.size() == 0 ? 0 : rows.get(0).size() ;
  }

  public void ensureInternal(int rowCount, int columnCount) {
    int newRowCount = rows.size(); 
    int newColumnCount = getColumnCount();
    if(getColumnCount() < columnCount){
      newColumnCount = columnCount;
      for(int row = 0 ; row < rows.size(); row ++){
        List<T> rowData = rows.get(row);
        int delta = columnCount - rowData.size();
        for (int j = 0; j < delta; j++) {
          rowData.add(defaultValue);
        }
      }
    }
    if( rows.size() < rowCount ){
      newRowCount = rowCount;
      for(int i = rows.size(); i < newRowCount ; i ++){
        addRow(newColumnCount);
      }
    }
  }

  public void addRow() {
    addRow(getColumnCount());
  }
  
  private void addRow(int newColumnCount) {
    List<T> row = new ArrayList<T>(newColumnCount);
    for (int j = 0; j < newColumnCount; j++) {
      row.add(defaultValue);
    }
    rows.add(row);
  }


  public List<T> getRow(int row) {
    return rows.get(row);
  }

  public List<T> getColumn(int col){
    List<T> column = new ArrayList<T>();
    for (int i = 0; i < getRowCount(); i++) {
      column.add(get(i, col));
    }
    return column ;
  }

}
