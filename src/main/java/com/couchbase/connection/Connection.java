package com.couchbase.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.utils.MyProperties;

public final class Connection {
	private final String FILE_PROPERTIES = "application.properties";
	private static MyProperties props;
	private final String host;
	private final String user;
	private final String pwd;	
	private Cluster cluster;
	private final int timeout;
	
	public Connection() {
		props = new MyProperties(FILE_PROPERTIES);
		host 	= String.valueOf(props.get("HOST"));
		user 	= String.valueOf(props.get("USER"));
		pwd  	= String.valueOf(props.get("PWD"));
		timeout = Integer.parseInt((String)props.get("RESPONSE_TIMEOUT")); 		
		createConnection();
	}
	
	private void createConnection() {
		try {
			//Config cluster environment
			ClusterEnvironment clusterEnv = ClusterEnvironment.builder()					
											.timeoutConfig(TimeoutConfig.kvTimeout(Duration.ofMillis(timeout)))
										    .build();
			cluster = Cluster.connect(host, ClusterOptions.clusterOptions(user, pwd).environment(clusterEnv));			
		} catch (Exception e) {
			System.out.println("Error creating the connection, "+e.getMessage());
		}
	}
	
	public Cluster getCluster() {
		return cluster;
	}
	
	public void closeConnection() {
		System.out.println("## Disconnect ##");
		cluster.disconnect();
	}
}
