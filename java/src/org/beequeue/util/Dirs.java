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

import java.io.File;
import java.io.IOException;

public class Dirs {
  public static void ensureParentExists(String fileToSave) {
    ensureParentDirExists(new File(fileToSave));
  }

  public static void ensureParentDirExists(File file) {
    File dir = file.getParentFile();
    if( !dir.isDirectory() && !dir.exists()){
      for(int i = 0 ; i < 5 ;i++){
        dir.mkdirs();
        if(dir.isDirectory() && dir.exists()) {
          break;
        } else {
          try {
            Thread.sleep(20L);
          } catch (InterruptedException ignore) {}
        }
      }
    }
  }

  public static File createTmpDir() throws IOException {
    File tmpDir = File.createTempFile("tmp", ".dir");
    tmpDir.delete();
    tmpDir.mkdirs();
    return tmpDir;
  }

  public static boolean deleteDirectory(File path) {
    if( path.exists() ) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
         if(files[i].isDirectory()) {
           deleteDirectory(files[i]);
         }
         else {
           files[i].delete();
         }
      }
    }
    return( path.delete() );
  }

}
