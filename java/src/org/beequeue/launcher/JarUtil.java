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
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;
import org.beequeue.util.Streams;
import org.beequeue.util.Tuple;
import org.beequeue.util.Tuples;

public class JarUtil {
	
	public static void unpack(Class<?> classFromJar, final BeeOperation<ZipEntry,Boolean> filter,
			final File destination) throws IOException {
		BeeOperation<Tuple<ZipInputStream,ZipEntry>, Void> entryOp = new  BeeOperation<Tuple<ZipInputStream,ZipEntry>, Void>() {
			@Override
			public Void execute( Tuple<ZipInputStream,ZipEntry> input) {
				ZipEntry ze = input.o2;
				if (filter.execute(ze)) {
					String entryName = ze.getName();
					File f = new File(destination, entryName);
					if (ze.isDirectory()) {
						f.mkdirs();
					} else {
						try {
							FileOutputStream out = new FileOutputStream(f);
							Streams.copy(input.o1, out);
							out.close();
						} catch (Exception e) {
							throw BeeException.cast(e).memo("f", f);
						}
					}
					
				}
				return null;
			}
		};
		searchJar(classFromJar, entryOp);
	}
	
	public static List<ZipEntry> listZip(Class<?> classFromJar, final BeeOperation<ZipEntry,Boolean> filter) 
			throws IOException {
		final ArrayList<ZipEntry> list = new ArrayList<ZipEntry>();
		searchJar(classFromJar, new  BeeOperation<Tuple<ZipInputStream,ZipEntry>, Void>() {
			@Override
			public Void execute( Tuple<ZipInputStream,ZipEntry> input) {
				ZipEntry ze = input.o2;
				if (filter.execute(ze)) {
					list.add(ze);
				}
				return null;
			}
		});
		return list;
	}
	
	public static void searchJar(Class<?> classFromJar, BeeOperation<Tuple<ZipInputStream,ZipEntry>, Void> entryOp) throws IOException {
		try {
			CodeSource src = classFromJar.getProtectionDomain().getCodeSource();
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				ZipEntry ze = null;
				while ((ze = zip.getNextEntry()) != null) {
					entryOp.execute(Tuples.build(zip, ze));
				}
				zip.close();
			} else {
				throw new BeeException("cannot find jar specified");
			}
		} catch (Exception e) {
			throw BeeException.cast(e)
				.memo("classFromJar", classFromJar);
		}
	}

}
