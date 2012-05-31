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

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string utils
 */
public class Strings {

  public static Comparator<String> REVERSE_STRING_COMPARATOR = new Comparator<String>(){
    public int compare(String o1, String o2) {
      return -o1.compareTo(o2);
    }};

  public static boolean isEmpty(String s) {
    return !hasSome(s);
  }
  
  public static boolean hasSome(String s) {
    return s != null && s.trim().length() > 0;
  }

  public static int toInteger(String s) {
    return Integer.parseInt(s.replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
  }

  /**
   * check input against list of regex'es
   * 
   * @param input
   * @param regexs
   * @return <code>null</code> if list of regex'es empty, <code>true</code>
   *         if any of regex'es match, or <code>false</code> otherwise
   */
  public static Boolean matchesAny(String input, List<Pattern> regexs) {
    if (regexs == null || regexs.size() == 0) {
      return null;
    }
    for (Pattern p : regexs) {
      Matcher m = p.matcher(input);
      if (m.matches()) {
        return true;
      }
    }
    return false;
  }

	public static String repeat(String repeatThat, String separateWith,
			int howManyTimes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < howManyTimes; i++) {
			if (i > 0) {
				sb.append(separateWith);
			}
			sb.append(repeatThat);
		}
		return sb.toString();
	}

	public static String join(String delimeter, String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if(i > 0 ){
				sb.append(delimeter);
			}
			sb.append(args[i]);
		}
		return sb.toString();
	}

}
