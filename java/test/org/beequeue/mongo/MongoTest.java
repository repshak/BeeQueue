package org.beequeue.mongo;

import org.beequeue.util.ToStringUtil;
import org.junit.Test;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoTest {
	public static class User {
		public String _id;
		public String name;
	}

	@Test
	public void test() throws Exception {

		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "tutorial" );

		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(db.getCollection("users"), User.class,
		        String.class);
		DBCursor<User> cursor = users.find();
		for (User user : cursor) {
			System.out.println(ToStringUtil.toNotPrettyString(user));
		}
//		User u = new User();
//		u.name = "Jones";
//		users.insert(u);
	}
}
