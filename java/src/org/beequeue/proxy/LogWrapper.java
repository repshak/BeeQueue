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
package org.beequeue.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.beequeue.piles.LazyList;
import org.beequeue.time.StopWatch;
import org.beequeue.util.ToStringUtil;


public class LogWrapper<T> {
  
  private static final String METHOD = " method:";
  private static final String WRAPPED = " wrapped:";
  public final Class<T> interfaceClass ;
  public final boolean logAll ;
  private final Logger log;
  private final LazyList<String> logMethods = new LazyList<String>();
  private final Map<String,LogWrapper> wrapMethods = new TreeMap<String, LogWrapper>();  
  
  public LogWrapper(Class<T> interfaceClass, boolean logAll) {
    assert interfaceClass.isInterface();
    this.interfaceClass = interfaceClass;
    this.logAll = logAll;
    this.log = Logger.getLogger(interfaceClass.getName());
  }

  public static <T> LogWrapper<T> build(Class<T> interfaceClass){
    return new LogWrapper<T>(interfaceClass,false);
  }

  private  static Map<Class,LogWrapper> map = new TreeMap<Class, LogWrapper>();
  
  @SuppressWarnings("unchecked")
  public static <T> LogWrapper<T> cache(Class<T> interfaceClass, boolean logall){
    LogWrapper<T> logWrapper = map.get(interfaceClass);
    if(logWrapper == null){
      synchronized (map) {
        if( (logWrapper= map.get(interfaceClass)) == null){
          replaceInCache(interfaceClass, logWrapper = build(interfaceClass,logall));
        }
      }
    }
    return logWrapper;
    
    
  }

  public static <T> void replaceInCache(Class<T> interfaceClass, LogWrapper<T> logWrapper) {
    synchronized (map) {
      map.put(interfaceClass, logWrapper);
    }
  }
  
  public static <T> LogWrapper<T> build(Class<T> interfaceClass, boolean logall){
    return new LogWrapper<T>(interfaceClass,logall);
  }

  public LogWrapper<T> wrapMethod(String method, LogWrapper wrapWith) {
    wrapMethods.put(method, wrapWith);
    return this;
  }

  public LogWrapper<T> logMethod(String ... methods) {
    logMethods.append(methods);
    return this;
  }
  
  @SuppressWarnings("unchecked")
  public T warp(final Object original, final StopWatch sw){
    assert interfaceClass.isAssignableFrom(original.getClass());
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
        new Class[]{interfaceClass}, 
        new InvocationHandler(){
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            StopWatch local = new StopWatch();
            boolean doLog = logAll || logMethods.contains(name);
            LogWrapper wrap = wrapMethods.get(name);
            String fragment = "";
            if( doLog ) {
              String argList = "[]";
              if(args != null) argList = toNiceString(args);
              fragment = name+" "+argList+": " ;
              log.fine(    "before: ->" + fragment   +WRAPPED+sw);
            }
              try{
                Object result = method.invoke(original,args);
                if( doLog ) log.fine("after:  <-" + fragment + METHOD+local  + WRAPPED+sw);
                return wrap == null ? result : wrap.warp(result, sw);
              }catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if( doLog ) log.warning( "fail:   <-" + fragment + METHOD+local  + WRAPPED+sw+" because:"+targetException);
                throw targetException;
              }
              
          }});
  }

  private String toNiceString(Object[] args) {
    ArrayList<String> texts = new ArrayList<String>();
    for (int i = 0; i < args.length; i++) {
      String text = ToStringUtil.toString(args[i]);
      int len = text.length();
      if(len > 400){
        text = text.substring(0,25) + "... " +len+ " chars ..." + text.substring(len-15) ;
      }
      texts.add(text);
    }
    return texts.toString();
  }
  
  
  
  
  
}
