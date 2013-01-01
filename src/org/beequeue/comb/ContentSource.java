package org.beequeue.comb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.beequeue.util.BeeException;

public interface ContentSource {
	InputStream get();
	long length();
	
	class FileBased implements ContentSource {
		private File file;
		
		public FileBased(File file) {
			this.file = file;
		}

		@Override
		public InputStream get() {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new BeeException(e);
			}
		}

		@Override
		public long length() {
			return file.length();
		}
		
	}
	
	
}
