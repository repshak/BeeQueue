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
