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
package org.beequeue.buzz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.beequeue.util.JsonTable;
import org.beequeue.util.Streams;

public class FileSystemBuzzResourceController 
implements BuzzResourceController{
	private static final String INDEX_TXT = "index.txt";
	public File root ;
	public String rootIndexPage = INDEX_TXT;
	public String indexPage = INDEX_TXT;
	@Override
	public boolean process(BuzzContext ctx) throws IOException {
		BuzzPath relativePath = ctx.relativePath();
		File resolveTo = new File(root, relativePath.toString());
		if( resolveTo.exists() && resolveTo.isDirectory() ){
			relativePath = relativePath.addElements(relativePath.size == 0 ? rootIndexPage : indexPage);
			resolveTo = new File(root,relativePath.toString());
		}
		boolean serveDirectoryContent = false;
		if(relativePath.name().equals(INDEX_TXT) ){
			if(!resolveTo.exists()){
				serveDirectoryContent = true;
			}	
		}
		if( serveDirectoryContent ){
				BuzzPath dirPath = ctx.ancestor.add(relativePath.parent());
				String drill = dirPath.toString() + "/%0%";
				String parent = dirPath.parent().toString();
				JsonTable jsonTable = new JsonTable(resolveTo.toString());
				jsonTable.add("FILE_NAME",resolveTo.getParentFile().list());
				jsonTable.parent = parent;
				jsonTable.drill = drill;
				ctx.res.setContentType("text/plain");
				ctx.res.getWriter().println(jsonTable.toJsonTable());
				return ctx.setHandled();
		}else if(resolveTo.exists()){
			ctx.res.setContentType(relativePath.getMimeType("txt"));
			Streams.copyAndClose(new FileReader(resolveTo), ctx.res.getWriter());
			return ctx.setHandled();
		}
		return false;
		
		
		
	}

}
