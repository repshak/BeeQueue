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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.beequeue.piles.flock.AnyFlock;
import org.beequeue.piles.flock.BooleanFlockImpl;
import org.beequeue.piles.flock.ByteFlockImpl;
import org.beequeue.piles.flock.DoubleFlockImpl;
import org.beequeue.piles.flock.FloatFlockImpl;
import org.beequeue.piles.flock.Flock;
import org.beequeue.piles.flock.IntegerFlockImpl;
import org.beequeue.piles.flock.LongFlockImpl;
import org.beequeue.piles.flock.ShortFlockImpl;
import org.beequeue.piles.flock.StringFlockImpl;


public abstract class DataType<T> {
	public final String name;
	public final Class<T> type;
	
	public DataType(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}
	
	abstract public Flock<T> newFlock();
	abstract public String o2s(T o) throws Exception ;
	abstract public T s2o(String s) throws Exception ;
	
	public static DataType<Boolean> BOOLEAN = new DataType<Boolean>("BOOLEAN", Boolean.class) {
		@Override public Flock<Boolean> newFlock() { return new BooleanFlockImpl(); }
		@Override public String o2s(Boolean o) throws Exception { return o.toString() ; }
		@Override public Boolean s2o(String s) throws Exception { return  s.length() > 0 && " YyTt".indexOf(s.charAt(0)) > 0; }
	};
	
	public static DataType<Short> SHORT = new DataType<Short>("SHORT", Short.class) {
		@Override public Flock<Short> newFlock() { return new ShortFlockImpl(); }
		@Override public String o2s(Short o) throws Exception { return o.toString() ; }
		@Override public Short s2o(String s) throws Exception { return  new Short(s) ; }
	};
	
	public static DataType<Integer> INTEGER = new DataType<Integer>("INTEGER", Integer.class) {
		@Override public Flock<Integer> newFlock() { return new IntegerFlockImpl(); }
		@Override public String o2s(Integer o) throws Exception { return o.toString() ; }
		@Override public Integer s2o(String s) throws Exception { return  new Integer(s) ; }
	};
	public static DataType<Double> DOUBLE = new DataType<Double>("DOUBLE", Double.class) {
		@Override public Flock<Double> newFlock() { return new DoubleFlockImpl(); }
		@Override public String o2s(Double o) throws Exception { return o.toString() ; }
		@Override public Double s2o(String s) throws Exception { return  new Double(s) ; }
	};

	public static DataType<Float> FLOAT = new DataType<Float>("FLOAT", Float.class) {
		@Override public Flock<Float> newFlock() { return new FloatFlockImpl(); }
		@Override public String o2s(Float o) throws Exception { return o.toString() ; }
		@Override public Float s2o(String s) throws Exception { return new Float(s) ; }
	};

	public static DataType<Byte> BYTE = new DataType<Byte>("BYTE", Byte.class) {
		@Override public Flock<Byte> newFlock() { return new ByteFlockImpl(); }
		@Override public String o2s(Byte o) throws Exception { return o.toString() ; }
		@Override public Byte s2o(String s) throws Exception { return  new Byte(s) ; }
	};

	public static DataType<Long> LONG = new DataType<Long>("LONG", Long.class) {
		@Override public Flock<Long> newFlock() { return new LongFlockImpl(); }
		@Override public String o2s(Long o) throws Exception { return o.toString() ; }
		@Override public Long s2o(String s) throws Exception { return  new Long(s) ; }
	};

	public static DataType<String> STRING = new DataType<String>("STRING", String.class) {
		@Override public Flock<String> newFlock() { return new StringFlockImpl(); }
		@Override public String o2s(String o) throws Exception { return o ; }
		@Override public String s2o(String s) throws Exception { return s ; }
	};
	
	public static SimpleDateFormat yyyymmdd() { return new SimpleDateFormat("yyyyMMdd"); }
	public static DataType<Date> DATE = new DataType<Date>("DATE", Date.class) {
		@Override public Flock<Date> newFlock() { return new AnyFlock<Date>(); }
		@Override public String o2s(Date o) throws Exception { return yyyymmdd().format(o) ; }
		@Override public Date s2o(String s) throws Exception { return yyyymmdd().parse(s) ; }
	};

	public static SimpleDateFormat yyyymmddhhmmss() { return new SimpleDateFormat("yyyyMMddhhmmss"); }
	public static DataType<Date> TIMESTAMP = new DataType<Date>("TIMESTAMP", Date.class) {
		@Override public Flock<Date> newFlock() { return new AnyFlock<Date>(); }
		@Override public String o2s(Date o) throws Exception { return yyyymmddhhmmss().format(o) ; }
		@Override public Date s2o(String s) throws Exception { return yyyymmddhhmmss().parse(s) ; }
	};
	public static SimpleDateFormat yyyymmddhhmmssz() { return new SimpleDateFormat("yyyyMMddhhmmssZ"); }
	public static DataType<Date> ZTIMESTAMP = new DataType<Date>("ZTIMESTAMP", Date.class) {
		@Override public Flock<Date> newFlock() { return new AnyFlock<Date>(); }
		@Override public String o2s(Date o) throws Exception { return yyyymmddhhmmssz().format(o) ; }
		@Override public Date s2o(String s) throws Exception { return yyyymmddhhmmssz().parse(s) ; }
	};

}
