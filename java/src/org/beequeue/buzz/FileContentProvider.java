package org.beequeue.buzz;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.beequeue.json.BuiltInType;
import org.beequeue.json.BuzzAttribute;
import org.beequeue.json.BuzzRow;
import org.beequeue.json.BuzzTable;
import org.beequeue.json.SortOrder;
import org.beequeue.util.BeeException;

public class FileContentProvider extends ContentProvider {
	private static final String TYPE = "TYPE";
	private static final String MODIFIED = "MODIFIED";
	private static final String SIZE = "SIZE";
	private static final String FILE_NAME = "FILE_NAME";
	public final File root;
	public FileContentProvider(File root) {
		this.root = root;
	}
	@Override
	public String getRoot() {
		return root.getPath();
	}
	
	private File file( BuzzPath relativePath){
		return new File(root,relativePath.toString());
	}
	
	
	public static enum FileType { 
		DIR, 
		FILE
		;

		public static  FileType typeOf(File file) {
			return file.isDirectory() ? DIR : FILE;
		}
	} ;
	@Override
	public BuzzContent getContent(final BuzzPath relativePath) {
		return new BuzzContent(){
			File file = file(relativePath);

			@Override
			public String getContentType() {
				return file.isDirectory() ? "text/plain" : relativePath.getMimeType("txt");
			}

			@Override
			public RetrievalMethod getMethod() {
				return file.isDirectory() ? RetrievalMethod.BUZZ_TABLE : RetrievalMethod.STREAM ;
			}

			@Override
			public InputStream getStream() {
				try {
					if( file.isDirectory() ) {
						throw new BeeException("directory need to be served with:"+ RetrievalMethod.BUZZ_TABLE );
					}
					return new FileInputStream(file);
				} catch (Exception e) {
					throw BeeException.cast(e)
					.memo("file", file);
				}
			}

			@Override
			public BuzzTable getBuzzTable() {
				try {
					if( !file.isDirectory() ) {
						throw new BeeException("file need to be served with:"+ RetrievalMethod.STREAM );
					}
					BuzzTable table = new BuzzTable();		
					table.header.add(BuzzAttribute.newAttr( FILE_NAME, BuiltInType.STRING, SortOrder.ASCENDING ));
					table.header.add(BuzzAttribute.newAttr( SIZE, BuiltInType.INTEGER, null));
					table.header.add(BuzzAttribute.newAttr( MODIFIED, BuiltInType.DATE, null));
					table.header.add(BuzzAttribute.newAttr( TYPE, BuiltInType.ENUM, null));
					File[] listFiles = file.listFiles();
					for (int i = 0; i < listFiles.length; i++) {
						populateFileRow(listFiles[i], table.newRow());
					}
					return table;
				} catch (Exception e) {
					throw BeeException.cast(e)
					.memo("file", file);
				}
			}

			
			
			
		};
	}
	
	public static  void populateFileRow(File fromFile, BuzzRow toRow) {
		toRow.set(FILE_NAME, fromFile.getName());
		toRow.set(SIZE, fromFile.length());
		toRow.set(MODIFIED, new Date(fromFile.lastModified()));
		toRow.set(TYPE, FileType.typeOf(fromFile));
		toRow.addToTargetTable();
	}


}
