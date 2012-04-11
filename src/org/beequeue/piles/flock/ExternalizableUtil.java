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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExternalizableUtil {

  public static void writeCompact(ObjectOutput out, IntegerFlockImpl index, int dictionarySize) 
  throws IOException {
    //TODO pack
    index.writeExternal(out);
  }

  public static void readCompact(ObjectInput in, IntegerFlockImpl index, int dictionarySize) 
  throws IOException, ClassNotFoundException {
    //TODO unpack
    index.readExternal(in);
  }
  
  public static String readNullableUTF(DataInput in) throws IOException{
    boolean isNull = in.readBoolean();
    String s = null;
    if(!isNull){
      s = in.readUTF();
    }
    return s;
  }
  
  public static void writeNullableUTF(DataOutput out, String s) throws IOException{
    boolean isNull = s == null ;
    out.writeBoolean(isNull);
    if(!isNull){
      out.writeUTF(s);
    }
  }

}
