package com.bbe.logFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Test {
	private static WebResource resource;
	private static ClientResponse response;
	
	public static void main(String[] args) throws IOException {
		
		Client c = Client.create();
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("global.properties"));
		
		System.out.println(prop.getProperty("1"));
		
		
		resource = c.resource("https://postman-echo.com/get?foo1=bar1&foo2=bar2");
		response = resource.get(ClientResponse.class);
		System.out.println(response.getEntity(String.class));
		
		resource = c.resource("https://postman-echo.com/put?foo1=bar1&foo2=bar2");
		response = resource.put(ClientResponse.class);
		System.out.println(response.getEntity(String.class));
		
		resource = c.resource("https://postman-echo.com/post?foo1=bar1&foo2=bar2");
		response = resource.post(ClientResponse.class);
		System.out.println(response.getEntity(String.class));
		
		resource = c.resource("https://postman-echo.com/delete?foo1=bar1&foo2=bar2");
		response = resource.delete(ClientResponse.class);
		System.out.println(response.getEntity(String.class));
	}
}
