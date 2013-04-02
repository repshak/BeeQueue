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
/**
 * 
 */
package org.beequeue.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class MethodInterceptor<T> implements InvocationHandler {
  protected T impl;

  public MethodInterceptor(T impl) {
    this.impl = impl;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object result = null;
    Throwable failure = null;
    beforeInvocation(method, args);
    try{
      result = onInvoke(method, args);
      return result;
    }catch (InvocationTargetException e) {
      failure = onFailure(method, args, e);
      throw failure;
    }finally{
      afterInvocation(method, args, result, failure);
    }
  }
  
  protected abstract void beforeInvocation(Method method, Object[] args) ;

  protected Object onInvoke(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    return method.invoke(impl,args);
  }

  protected Throwable onFailure(Method method, Object[] args, InvocationTargetException failure) {
    return failure.getTargetException();
  }
  
  protected abstract void afterInvocation(Method method, Object[] args, Object result, Throwable failure) ;
}
