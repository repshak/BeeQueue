package org.beequeue.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.beequeue.time.StopWatch;

public class Select<T,I> extends Operation<I> {
	private static final Logger log = Logger.getLogger(Select.class.getName());

	public final JdbcFactory<T,? super I> factory ;

	public Select(String sql, JdbcFactory<T,? super I> factory, SqlPrepare<I> setParams) {
		this(sql,factory,setParams,null);
	}

	public Select(String sql, JdbcFactory<T,? super I> factory, SqlPrepare<I> setParams, SqlMorph<? super I> sqlTransition) {
		super(sql, setParams, sqlTransition);
		this.factory = factory;
	}


	public T queryOne(Connection conn, I input) {
	  List<T> query = query(conn, input);
	  if(query.size() == 1){
	    return query.get(0);
	  }else{
	    throw new DataNotFoundException("query returned:" + query.size() +" for key:" + input); 
	  }
	}

	public List<T> query(Connection connection, I input)
	throws DalException {
		return query(connection,input,Integer.MAX_VALUE);
	}
	
	public List<T> query(Connection connection, I input, int maxCount )
		throws DalException {
    StopWatch sw = new StopWatch();
		PreparedStatement pstmt = null;
		try {
			pstmt = prepare(connection,input);
			ResultSet rs = pstmt.executeQuery();
			List<T> list = new ArrayList<T>();
			try {
				while(rs.next()) {
					T newInstance = factory.newInstance(rs,input, new Index());
					if(newInstance != null){
						list.add(newInstance);
					}
					if(--maxCount <= 0 ){
						break;
					}
				}
				log.fine("query: size="+list.size());
				return list;
			} finally {
				try { rs.close(); } catch (Exception ignore) { }
			}
		} catch (SQLException e) {
			log.fine("query: ex="+e);
			throw new DalException(e);
		} finally {
			log.fine("query: time=" + sw.getSeconds());
			try { pstmt.close(); } catch (Exception ignore) {}
		}
	}		

}
