package org.beequeue.util;

import java.io.IOException;
import java.io.OutputStream;
/**
 * Inspired by /dev/null
 * 
 * 
 * @author sergeyk
 */
public class DevNullStream extends OutputStream {
  public static final	DevNullStream DEV_NULL = new DevNullStream();
  
  public static DevNullStream getInstance(){ return DEV_NULL; }
  
  @Override public void write(int b) throws IOException {}
  @Override public void write(byte[] b) throws IOException {}
  @Override public void write(byte[] b, int off, int len) throws IOException {}

}
