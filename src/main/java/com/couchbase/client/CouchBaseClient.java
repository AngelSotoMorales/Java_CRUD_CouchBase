package com.couchbase.client;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.connection.Connection;

public final class CouchBaseClient {
	private static Connection connection = new Connection();
	
	public void createDocument(String bucket, String scope, String collection, Object document) {
		try {
			Collection cl = getCollection(bucket, scope, collection);
														
			//Upsert: Insert a document if it doesn´t already exist and replaces a document if it already exists with the default key/value
			cl.upsert(String.valueOf(System.currentTimeMillis()), document);
			
			//Insert: Create a new object only if the ID doesn´t exist
			//collection.insert("O3", catalogObject);
						
		} catch (Exception e) {
			System.out.println("Error creating document, "+e.getMessage());
			e.printStackTrace();
		}finally {
			closeConnection();
		}
	}
	
	public JsonObject getDocument(String bucket, String scope, String collection, String idDocument) {
		try {
			if(idDocument != null && !idDocument.isEmpty()) {
				Collection cl = getCollection(bucket, scope, collection);
				GetResult result =  cl.get(idDocument);
				System.out.println("CAS: "+result.cas());
				System.out.println("Content: "+result.contentAsObject());
				return result.contentAsObject();
			}else {
				System.out.println("Error with id, id is: "+idDocument);
			}						
		} catch (Exception e) {
			System.out.println("Error retrieving document with id: "+ idDocument);
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return null;
	}

	public long updateDocument(String bucket, String scope, String collection, String idDocument, Object document) {
		try {
			Collection cl = getCollection(bucket, scope, collection);
			//Used only if the document to be replaced exists
			MutationResult result =  cl.replace(idDocument, document);
			
			//Replaces a document if it exists and adds a document if it does not already exist
			//MutationResult result = cl.upsert(idDocument, document);
			return result.cas();								
		} catch (Exception e) {
			System.out.println("Error updating file, "+e.getMessage());
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return -1;
	}
	
	public long removeDocument(String bucket, String scope, String collection, String idDocument) {
		try {
			Collection cl = getCollection(bucket, scope, collection);
			MutationResult result = cl.remove(idDocument);
			System.out.println("CAS: "+result.cas());
			System.out.println("Document: "+result.toString());
			return result.cas();
		} catch (Exception e) {
			System.out.println("Error eliminando documento, "+e.getMessage());
			e.printStackTrace();			
		}
		return -1;
	}
		
	private static Collection getCollection(String bucket, String scope, String collection) {
		try {
			//Bucket.Scope.Collection
			String DEFAULT = "_default";
			Cluster cluster = connection.getCluster();			
			
			/* 
			 	Perform asynchronous operations on a bucket
				AsyncBucket bucket = cluster.bucket(BUCKET);
			 */	
			
			//Like a database
			Bucket bk = cluster.bucket(bucket);
									
			//Acts as a parent to a collection
			//Like a grouping (Scope default: _default)
			Scope sp = bk.scope((scope != null && !scope.isEmpty()) ? scope : DEFAULT);
			
			//A collection can contain a set of documents
			//Like tables (Collection default: _default)
			Collection cl = sp.collection((collection != null && !collection.isEmpty()) ? collection : DEFAULT);
			
			return cl;
		} catch (Exception e) {
			System.out.println("Error creating collection, "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	private static void closeConnection() {
		try {
			connection.closeConnection();
		} catch (Exception e2) {
			System.out.println("Error closing connection, "+e2.getMessage());
		}
	}
}
