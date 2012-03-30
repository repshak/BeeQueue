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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {

  public static String readAll(File file) throws IOException {
    FileReader fileReader = new FileReader(file);
    StringBuilder sb = new StringBuilder();
    char buf[] = new char[2048];
    int read;
    try{
      while((read = fileReader.read(buf))>0){
        sb.append(buf, 0, read);
      }
      return sb.toString();
    }finally{
      fileReader.close();
    }
  }
  
  public static void writeAll(File file, String content) throws IOException {
    Dirs.ensureParentDirExists(file);
    FileWriter fileWriter = new FileWriter(file);
    try{
      fileWriter.write(content);
    }finally{
      fileWriter.close();
    }
  }

  public static File absolute(String path) {
    File file = new File(path).getAbsoluteFile();
    try{
      file = file.getCanonicalFile();
    }catch (Exception ignore) {}
    return file;
  }



}
