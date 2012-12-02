package org.beequeue.comb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Date;

import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public class TimeStampStoreImpl implements TimeStampStore {

	private final class Reader<T> implements TimeStampStreamReader<T> {
		private final File file;
		private final Class<T> cls;
		private final TypeReference<T> ref;
		private Reader(TypeReference<T>ref,File file) {
			this.cls = null;
			this.ref = ref;
			this.file = file;
		}

		private Reader(Class<T> cls,File file) {
			this.cls = cls;
			this.ref = null;
			this.file = file;
		}

		private T factory(String s){
			if(cls!=null){
				return ToStringUtil.toObject(s, cls);
			}else{
				return ToStringUtil.toObject(s, ref);
			}
		}
		
		@Override
		public TimeStampStream<T> openStream(Date startFrom, IterationDirection direction) {
			try {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				return null;
			} catch (Exception e) {
				throw new BeeException(e);
			}
		}
	}

	@Override
	public <T> TimeStampStreamReader<T> reader(Class<T> type, File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> TimeStampStreamReader<T> reader(final TypeReference<T> type, final File file) {
		return new Reader<T>(file);
		
	}

	@Override
	public <T> void append(File file, T toAdd) {
		// TODO Auto-generated method stub

	}

}
