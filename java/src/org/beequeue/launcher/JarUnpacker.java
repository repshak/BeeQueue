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
package org.beequeue.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarUnpacker {
	public static interface EntryFilter {
		boolean include(ZipEntry ze);
	}
	public static void unpack(Class<?> searchJar, EntryFilter filter,
			File destination) throws IOException {
		CodeSource src = searchJar.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL jar = src.getLocation();
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry ze = null;
			while ((ze = zip.getNextEntry()) != null) {
				if (filter.include(ze)) {
					String entryName = ze.getName();
					File f = new File(destination, entryName);
					if (ze.isDirectory()) {
						f.mkdirs();
					} else {
						FileOutputStream out = new FileOutputStream(f);
						copy(zip, out, 4096);
					}

				}
			}

		} else {
			throw new RuntimeException("cannot uppack jar specified by class:"
					+ searchJar);
		}
	}

	private static int copy(InputStream from, OutputStream to, int bufferSize)
			throws IOException {
		byte buffer[] = new byte[bufferSize];
		int count = 0;
		int nr;
		while ((nr = from.read(buffer)) > 0) {
			to.write(buffer, 0, nr);
			count += nr;
		}
		to.flush();
		return count;
	}
}
