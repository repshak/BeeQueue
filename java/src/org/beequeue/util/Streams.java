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
package org.beequeue.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class Streams {

  public static int copy(InputStream from, Writer to)
  throws IOException{
    return copy(from, to,-1);
  }
  
  public static int copy(InputStream from, Writer to, int bufferSize) 
  throws IOException {
    return copy(new InputStreamReader(from), to, -1);
  }


  public static int copyAndClose(Reader from, Writer to)
  throws IOException{
    return copyAndClose(from, to,-1);
  }
  
  public static int copyAndClose(InputStream from, OutputStream to)
  throws IOException{
    return copyAndClose(from, to, -1);
  }
  
  public static int copyAndClose(InputStream from, OutputStream to, int bufferSize) 
  throws IOException {
    try {
      return copy(from, to, bufferSize);
    } finally {
      try { from.close(); } catch (Throwable ignore) {}
      try { to.close(); } catch (Throwable ignore) {}
    }
  }
  
  public static int copyAndClose(Reader from, Writer to, int bufferSize) 
  throws IOException {
    try {
      return copy(from, to, bufferSize);
    } finally {
      try { from.close(); } catch (Throwable ignore) {}
      try { to.close(); } catch (Throwable ignore) {}
    }
  }
  
  public static int copy(Reader from, Writer to) throws IOException {
    return copy(from, to, -1);
  }

  public static int copy(Reader from, Writer to, int bufferSize)
      throws IOException {
    char buffer[] = new char[Buffers.adjustBufferSize(bufferSize)];
    int count = 0 ;
    int nr;
    while( ( nr = from.read(buffer) ) > 0 ){
      to.write(buffer,0,nr);
      count += nr;
    }
    to.flush();
    return count;
  }

  public static int copy(InputStream from, OutputStream to) throws IOException {
    return copy(from, to, -1);
  }

  public static int copy(InputStream from, OutputStream to, int bufferSize)
      throws IOException {
    byte buffer[] = new byte[Buffers.adjustBufferSize(bufferSize)];
    int count = 0 ;
    int nr;
    while( ( nr = from.read(buffer) ) > 0 ){
      to.write(buffer,0,nr);
      count += nr;
    }
    to.flush();
    return count;
  }

}
