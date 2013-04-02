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
package org.beequeue.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.beequeue.proxy.MethodInterceptor;

public class TransactionMethodInterceptor<T> extends MethodInterceptor<T> {
  private static final Logger log = Logger.getLogger(TransactionMethodInterceptor.class.getName());
  
  public TransactionMethodInterceptor(T impl) {
    super(impl);
  }

  protected void beforeInvocation(Method method, Object[] args) {
    if( log.isLoggable(Level.FINE) ){
      log.fine("beforeInvocation: "+method);
    }
    TransactionContext.push();
  }

  protected void afterInvocation(Method method, Object[] args, Object result, Throwable failure) {
    TransactionContext.pop();
    if( log.isLoggable(Level.FINE) ){
      log.fine("afterInvocation: "+method);
    }
  }

  protected Throwable onFailure(Method method, Object[] args, InvocationTargetException failure) {
    TransactionContext.setRollbackOnly();
    Throwable targetException = failure.getTargetException();
    if(log.isLoggable(Level.WARNING)){
    	log.warning("onFailure: "+method+ " with:"+ targetException);
    }
    return targetException;
  }

}
