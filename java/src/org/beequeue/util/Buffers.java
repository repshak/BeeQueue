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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class Buffers {
  public static final int BUFFER_SIZE = 1<<13;

  public static int adjustBufferSize(int bufferSize) {
    return bufferSize <= 0 ? Buffers.BUFFER_SIZE : bufferSize;
  }


  public static BufferedInputStream bufferInput(InputStream is) {
    return bufferInput(is, -1);
  }
  
  public static BufferedInputStream bufferInput(InputStream is, int bufferSize) {
    if (is instanceof BufferedInputStream) {
      return (BufferedInputStream) is;
    }else {
      return new BufferedInputStream(is,adjustBufferSize(bufferSize));
    }
  }

  public static BufferedOutputStream bufferOutput(OutputStream os) {
    return bufferOutput(os, -1);
  }

  public static BufferedOutputStream bufferOutput(OutputStream os, int bufferSize) {
    if (os instanceof BufferedOutputStream) {
      return (BufferedOutputStream) os;
    }else{
      return new BufferedOutputStream(os,adjustBufferSize(bufferSize));
    }
  }

  public static BufferedWriter bufferWriter(Writer writer, int bufferSize) {
    if (writer instanceof BufferedWriter) {
      return (BufferedWriter) writer;
    } else {
      return new BufferedWriter(writer, adjustBufferSize(bufferSize));
    }
  }

  public static BufferedReader bufferReader(Reader reader) {
    return bufferReader(reader,-1);
  }

  public static BufferedReader bufferReader(Reader reader, int bufferSize) {
    if (reader instanceof BufferedReader) {
      return (BufferedReader)reader;
    } else {
      return new BufferedReader(reader, adjustBufferSize(bufferSize));
    }
  }

  public static BufferedReader bufferReader(InputStream is) {
    return bufferReader(is,-1);
  }

  public static BufferedReader bufferReader(InputStream is, int bufferSize) {
    return bufferReader(new InputStreamReader(is), bufferSize);
  }
  
}
