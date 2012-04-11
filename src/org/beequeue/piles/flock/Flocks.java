package org.beequeue.piles.flock;

import org.beequeue.util.Nulls;

public class Flocks {
  
  public static <T> AnyFlock<T> anyFlock(T... vals) {
    return AnyFlock.build(vals);
  }

  public static <T> AnyFlock<T> anyFlock() {
    return new AnyFlock<T>();
  }

  
  public static BooleanFlock booleanFlock(boolean... vals) {
    return BooleanFlockImpl.build(vals);
  }

  public static BooleanFlock booleanFlock() {
    return new BooleanFlockImpl();
  }

  
  public static ByteFlock byteFlock(byte... vals) {
    return ByteFlockImpl.build(vals);
  }

  
  public static ByteFlock byteFlock() {
    return new ByteFlockImpl();
  }

  
  public static DoubleFlock doubleFlock(double... vals) {
    return DoubleFlockImpl.build(vals);
  }
  
  
  public static DoubleFlock doubleFlock() {
    return new DoubleFlockImpl();
  }
  
  
  public static FloatFlock floatFlock(float... vals) {
    return FloatFlockImpl.build(vals);
  }

  
  public static FloatFlock floatFlock() {
    return new FloatFlockImpl();
  }

  public static IntegerFlock integerFlock(int... vals) {
    return IntegerFlockImpl.build(vals);
  }

  public static IntegerFlock integerFlockEnumeration(int start, int end) {
    IntegerFlock build = new IntegerFlockImpl(end-start);
    for (int i = start; i < end; i++) {
      build.add(i);
    }
    return build;
    
  }

  public static IntegerFlock integerFlock() {
    return new IntegerFlockImpl();
  }

  public static LongFlock longFlock(long... vals) {
    return LongFlockImpl.build(vals);
  }

  
  public static LongFlock longFlock() {
    return new LongFlockImpl();
  }

  public static ShortFlock shortFlock(short... vals) {
    return ShortFlockImpl.build(vals);
  }

  
  public static ShortFlock shortFlock() {
    return new ShortFlockImpl();
  }

  /**
   * remove first occurrence of val
   * @param <T>
   * @param flock
   * @param val
   */
  public static <T> void removeValue(Flock<T> flock, T val) {
    for( int i = 0; i < flock.size(); i++) {
      if( Nulls.equals(val,flock.getValue(i)) ){
        flock.remove(i, 1);
        break;
      }
    }
  }
  
}
