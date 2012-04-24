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

public enum CalendarConstant {
  Year(Calendar.YEAR,0),
  Month(Calendar.MONTH,0),
  Day_of_the_Month(Calendar.DAY_OF_MONTH,1),
  Day_of_the_Week(Calendar.DAY_OF_WEEK,Calendar.SUNDAY),
  Hour(Calendar.HOUR_OF_DAY,0),
  Minute(Calendar.MINUTE,0),
  Second(Calendar.SECOND,0),
  Millisecond(Calendar.MILLISECOND,0),
  Week(Calendar.WEEK_OF_YEAR,0),
  
  Sun(Calendar.DAY_OF_WEEK,Calendar.SUNDAY),
  Mon(Calendar.DAY_OF_WEEK,Calendar.MONDAY),
  Tue(Calendar.DAY_OF_WEEK,Calendar.TUESDAY),
  Wed(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY),
  Thu(Calendar.DAY_OF_WEEK,Calendar.THURSDAY),
  Fri(Calendar.DAY_OF_WEEK,Calendar.FRIDAY),
  Sat(Calendar.DAY_OF_WEEK,Calendar.SATURDAY),
  
  Jan(Calendar.MONTH,Calendar.JANUARY),
  Feb(Calendar.MONTH,Calendar.FEBRUARY),
  Mar(Calendar.MONTH,Calendar.MARCH),
  Apr(Calendar.MONTH,Calendar.APRIL),
  May(Calendar.MONTH,Calendar.MAY),
  Jun(Calendar.MONTH,Calendar.JUNE),
  Jul(Calendar.MONTH,Calendar.JULY),
  Aug(Calendar.MONTH,Calendar.AUGUST),
  Sep(Calendar.MONTH,Calendar.SEPTEMBER),
  Oct(Calendar.MONTH,Calendar.OCTOBER),
  Nov(Calendar.MONTH,Calendar.NOVEMBER),
  Dec(Calendar.MONTH,Calendar.DECEMBER);
  
  public final int calendarComponent;
  public final int defaultValue;
  
  private CalendarConstant(int calendarComponent, int defaultValue) {
    this.calendarComponent = calendarComponent;
    this.defaultValue = defaultValue;
  }
  
  public void reset(Calendar c) {
    set(c, defaultValue);
  }

  public void set(Calendar c, int val) {
    c.set(calendarComponent, val);
  }

  public void add(Calendar c, int val) {
    c.add(calendarComponent, val);
  }

  public int get(Calendar c) {
    return c.get(calendarComponent);
  }
  
}
