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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	public static Date toMonthsFirstDate(String yyyymm) {
		return toDate(Integer.parseInt(yyyymm.substring(0, 4)),Integer.parseInt(yyyymm.substring(4, 6)),1);
	}

	public static  Date toDate(String yyyymmdd) {
		String[] split = yyyymmdd.split("-");
		if( split.length == 1 ){
			split = yyyymmdd.split("/");
		}
		if( split.length == 1 ){
			split = new String[]{yyyymmdd.substring(0, 4),yyyymmdd.substring(4, 6),yyyymmdd.substring(6, 8)};
		}
		return toDate(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
	}
	
	public static Date toDate(int year, int mon, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year,mon-1,day);
		return cal.getTime();
	}

	public static Date buildRelativeDate(int min) {
		return CalendarUtil.buildRelativeCalendar(min).getTime();
	}
	

	public static Date buildRelativeDate(int hours, int min) {
		return CalendarUtil.buildRelativeCalendar(hours, min).getTime();
	}

  public static DateFormat formatter(String template, String zoneName) {
    return formatter(template, TimeZone.getTimeZone(zoneName));
  }

  public static DateFormat formatter(String template, TimeZone zone) {
    DateFormat format = new SimpleDateFormat(template);
    format.setTimeZone(zone);
    return format;
  } 

  public static String toString(String template, TimeZone zone, Date date) {
    return formatter(template, zone).format(date);
  } 

  public static String toString(String template, String zoneName, Date date) {
    return formatter(template, zoneName).format(date);
  } 
	
}
