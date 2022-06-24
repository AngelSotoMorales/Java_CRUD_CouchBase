package com.couchbase.init;

import com.couchbase.client.CouchBaseClient;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.utils.MyProperties;

public class Init {
	private static final CouchBaseClient client = new CouchBaseClient();
	private static final String FILE_PROPERTIES = "application.properties";
	
	public static void main(String[] args) {
		MyProperties properties = new MyProperties(FILE_PROPERTIES);
		try {			
			JsonObject document = JsonObject.create()
									.put("name", "Eduardo")
									.put("lastName", "Morales")
									.put("age", "45");
			
			String BUCKET 		= String.valueOf(properties.get("BUCKET"));
			String SCOPE 		= String.valueOf(properties.get("SCOPE"));
			String COLLECTION 	= String.valueOf(properties.get("COLLECTION"));			
			
			//Create Document
			//client.createDocument(BUCKET, SCOPE, COLLECTION, document);
			
			//Retrieve Document
			client.getDocument(BUCKET, SCOPE, COLLECTION, "1655788260667");
			
			//Update Document
			//client.updateDocument(BUCKET, SCOPE, COLLECTION, "1655788260667", document);
			
			//Delete Document
			//client.removeDocument(BUCKET, SCOPE, COLLECTION, "O5");									
		} catch (Exception e) {
			System.out.println("Error in client, "+e.getMessage());
			e.printStackTrace();
		}

	}

}
