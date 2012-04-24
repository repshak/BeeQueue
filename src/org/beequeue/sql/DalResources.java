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

import java.sql.Connection;

import org.beequeue.coordinator.DbCoordinator;

public class DalResources {
	private static DbCoordinator config =  null ; 
	
  public static boolean isConfigured(){
    return config != null;
  }
  
	public static void init(DbCoordinator coordinator) {
		config =coordinator;
	}

	public static Connection getConnection() throws DalException{
      String name = "config";
	JdbcResourceTracker tracker = TransactionContext.searchResource(JdbcResourceTracker.class,name);
			if( tracker == null ){
			  tracker = new JdbcResourceTracker(name,config.connection());
        TransactionContext.register(tracker);
      }
			return tracker.getResource();
	}
	
}
