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

public abstract class FillCommand extends FlockCommand{
  public FillCommand(int to, int size) {
    super(to,size);
  }
  protected void fill(long[] destination, long empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(float[] destination, float empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(int[] destination, int empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(double[] destination, double empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(byte[] destination, byte empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(boolean[] destination, boolean empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(short[] destination, short empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(Object[] destination, Object empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
}
