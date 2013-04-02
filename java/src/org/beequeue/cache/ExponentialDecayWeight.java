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
package org.beequeue.cache;

public class ExponentialDecayWeight {
	private long lastHitTimeMills ;
	private long weight ;

	public ExponentialDecayWeight() {
		this(0,0);
	}
	public ExponentialDecayWeight(long lastHitTimeMills, long weight) {
		this.lastHitTimeMills = lastHitTimeMills;
		this.weight = weight;
	}

	public long getLastHitTimeMills() {
		return lastHitTimeMills;
	}

	public long getWeight() {
		return weight;
	}

	final public void hit(long halfLife, long mass){
		hit(halfLife, mass, System.currentTimeMillis());
	}
	
	final public void hit(long halfLife, long mass, long currentTimeMillis) {
		this.weight = mass + adjustWeightToCurrentTime(currentTimeMillis,halfLife);
		this.lastHitTimeMills = currentTimeMillis;
	}
	
	final public long adjustWeightToCurrentTime(long currentTimeMillis, long halfLife) {
		long newWeight = this.weight;
		if(this.lastHitTimeMills != 0){
			long delta = currentTimeMillis - this.lastHitTimeMills ;
			while(delta > halfLife){
				delta = delta - halfLife;
				newWeight /= 2;
			}
			newWeight = newWeight - newWeight * delta / ( halfLife * 2 ) ;
		}
		return newWeight;
	}

}
