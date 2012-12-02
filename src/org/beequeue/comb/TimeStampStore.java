package org.beequeue.comb;

import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;

public interface TimeStampStore {
	<T> TimeStampStreamReader<T> reader(Class<T> type, File file);
	<T> TimeStampStreamReader<T> reader(TypeReference<T> type, File file);
	<T> void append(File file, T toAdd);
	//<T> void merge(File destination, File ... sources );
}
