package codegen;


class ApplyLicense {

  static void main(args) {
	def BEGIN_LICENSE = '==== BEGIN LICENSE ====='
	def END_LICENSE = '===== END LICENSE ======'
  
  	def LICENSE = 
"""/** ${BEGIN_LICENSE}
   Copyright 2012 - BeeQueue

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ${END_LICENSE} */
""";
 	["java/src","java/test"].each{
	 	new File(it).eachFileRecurse { 
	      if ( it.name =~ /\.java$/ ) {
	        def text = it.getText().replaceAll("\\r","");
	        def license = LICENSE.replaceAll("\\r","");
	        if( ! text.startsWith(license) ){
		        def list = [];
		        boolean add = true ;
		        it.eachLine{
		          if(it =~ /${BEGIN_LICENSE}/ ){
		            add = false
		          }
		          if(add){
		            list << it
		          }
		          if(it =~ /${END_LICENSE}/ ){
		            add = true
		          }
		        }
		        it.renameTo(new File("${it.absolutePath}~"))
		        def out = it 
		        out.write(LICENSE);
		        list.each{out.append("${it}\n")}
		        println it.absolutePath;
	        }
	      } 
	    }
	  }
  }

}