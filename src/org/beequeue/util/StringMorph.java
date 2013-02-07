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
/**
 * string transition 
 */

public interface StringMorph {
	
  public class ToString<T> implements BeeOperation<T, String> {
    public String execute(T input) {
      return String.valueOf(input);
    }
  }
  BeeOperation<Object,String> TO_STRING = new ToString<Object>();
  
  Transformation<String> PASS_THRU = new PassThruTransformation<String>();
	
	public static class Regex implements Transformation<String>{
		
		private String regex;
		private String replacement;

		public Regex(String regex, String replacement) {
			super();
			this.regex = regex;
			this.replacement = replacement;
		}

		public String execute(String text) {
			return text.replaceAll(regex,replacement);
		}
	}
	
	public static class Prefix implements Transformation<String>{
	  
	  private String prefix;
	  
	  public Prefix(String prefix) {
	    this.prefix = prefix;
	  }
	  
	  public String execute(String text) {
	    return prefix + text;
	  }
	}

  public static class Suffix implements Transformation<String>{
    
    private String suffix;


    public Suffix(String suffix) {
      this.suffix = suffix;
    }


    public String execute(String text) {
      return text + suffix;
    }
  }


}
