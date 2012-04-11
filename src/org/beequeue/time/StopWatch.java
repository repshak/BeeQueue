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
package org.beequeue.time;

public class StopWatch {
  private long timeStamp = System.currentTimeMillis();
  
  public long getTimeStamp() {
    return timeStamp;
  }

  public static String toSeconds(long delta) {
    return ((double)delta)/1000.0 + "s";
  }
  
  public String getSeconds() {
    return toSeconds(getMills());
  }

  public long getMills() {
    return System.currentTimeMillis() - timeStamp;
  }
  
  public void reset() {
    resetMills();
  }
  
  public long resetMills() {
    long prevTimeStamp = timeStamp;
    timeStamp = System.currentTimeMillis();
    return timeStamp - prevTimeStamp;
  }

  public String resetSeconds() {
    return toSeconds(resetMills());
  }

  public String toString() {
    return getSeconds();
  }
  
  
  
}
