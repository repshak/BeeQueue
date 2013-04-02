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

import java.util.ArrayList;
import java.util.List;

public class TransactionContext {
	
    private static ThreadLocal<TransactionContext> txCtxStore = new ThreadLocal<TransactionContext>() ;

    private boolean rollbackOnly = false;
    List<ResourceTracker> registredResources = null;
    TransactionContext parent = null ;

    private boolean isRoot() {
      return parent == null;
    }

    public static void checkTransactionContext() {
	     if( ! isTransactionContextSet() ){
	       throw new RuntimeException("you have to use it inside of transaction context");             
	     }
    }

    public static boolean isTransactionContextSet() {
      return TransactionContext.txCtxStore.get() != null;
    }
    
    public static void push() {
		TransactionContext prevDaoCtx = getContext();
		TransactionContext daoCtx = new TransactionContext() ;
		if( prevDaoCtx != null ){
			daoCtx.parent = prevDaoCtx ;
		}else{
	        daoCtx.registredResources = new ArrayList<ResourceTracker>();
	    }
		txCtxStore.set( daoCtx );
	}

    public static TransactionContext getContext() {
    	return txCtxStore.get();
    }
     
    @SuppressWarnings("unchecked")
    public static <T extends ResourceTracker> T searchResource(Class<T> clazz, String name) {
      checkTransactionContext();
      TransactionContext ctx = getContext();
      for (ResourceTracker tracker : ctx.getRegistredResources()) {
        if(tracker.getClass().isAssignableFrom(clazz) && tracker.getKey().equals(name)){
          return (T) tracker;
        }
      }
      return null;
    }

    public static void setRollbackOnly() {
  	 checkTransactionContext();
     getContext().rollback();
    }

    
	private void rollback() {
      if( isRoot() ){
        rollbackOnly = true ;
      }else{
        parent.rollback();
      }
    }

    public static void pop() {
		TransactionContext ctx = getContext();
		if( ctx.isRoot()){
        for (ResourceTracker tracker : ctx.getRegistredResources()) {
					try {
						tracker.release(ctx.rollbackOnly);
					} catch (Exception ignore) {
					}
				}
        }
		txCtxStore.set(ctx.parent);
	}

    
    private List<ResourceTracker> getRegistredResources() {
      if( isRoot() ){
        return registredResources;
      }else{
        return parent.getRegistredResources();
      }
    }


    public static void register(ResourceTracker resource ) {
			checkTransactionContext();
			getContext().getRegistredResources().add(resource);
    }
}
