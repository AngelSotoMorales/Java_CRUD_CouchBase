package com.couchbase.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class MyProperties {
	private Properties props;
	
	public MyProperties(String fileName) {
		props = new Properties();
		try(InputStream is = MyProperties.class.getClassLoader().getResourceAsStream(fileName)){
			props.load(is);
		}catch(IOException ex) {
			System.out.println("Error laoding properties file, "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public Object get(String property) {
		return props.get(property);
	}
}
