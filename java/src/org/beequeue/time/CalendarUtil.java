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
package org.beequeue.time;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	//Wed Jan 1, 2003
	public static Calendar get20030101(){
		Calendar wed20030101 = Calendar.getInstance();
		wed20030101.clear();
		wed20030101.set(2003,0,1,0,0,0);
		return wed20030101; 
	}

	//Sun Jan 1, 2006
	public static Calendar get20060101(){
		Calendar sun20060101 = Calendar.getInstance();
		sun20060101.clear();
		sun20060101.set(2006,0,1,0,0,0);
		return sun20060101;
	}

	//Jan 1, 1960
	public static Calendar get19600101(){
		Calendar c19600101 = Calendar.getInstance();
		c19600101.clear();
		c19600101.set(1960,0,1,0,0,0);
		return c19600101; 
	}

	public static Calendar buildRelativeCalendar(int hours, int min) {
		return buildRelativeCalendar(min+hours*60);
	}
	
	public static Calendar buildRelativeCalendar(int min) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE,min);
		return cal;
	}
	
	public static Calendar getNow() {
		return Calendar.getInstance();
	}

	public static Calendar getYesterday() {
		Calendar cal = getNow();
		cal.add(Calendar.DAY_OF_YEAR,-1);
		return cal;
	}

	public static Calendar toCalendar(Date toDate) {
		Calendar cal = Calendar.getInstance();;
		cal.setTime(toDate);
		return cal;
	}
}
