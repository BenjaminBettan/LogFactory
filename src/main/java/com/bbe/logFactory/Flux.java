package com.bbe.logFactory;

import java.util.*;
import org.apache.log4j.Logger;
import com.sun.jersey.api.client.*;

public class Flux extends Thread {

	private Client c = Client.create();
	private WebResource resource;
	private List<String> idList;
	private ClientResponse response;
	private Properties prop;
	private String fluxName;
	private static Map<String, List<String>> m_flux;
	private long tempo = 0;
	protected static Logger logger = Logger.getLogger(Flux.class);

	public Flux(Properties prop_, HashMap<String, List<String>> m_flux_, String flux) {
		super();
		prop = prop_;
		fluxName = flux;
		m_flux = m_flux_;
	}

	public void run() {
		
		idList = m_flux.get(fluxName);
		
		priseEnComptePeriodeDuFlux();
		priseEnComptePoids();
		
		while (true) {
			for (String id : idList) {
				logger.info(fluxName+">"+prop.getProperty(id+"."+fluxName));
				try {
					Thread.sleep(tempo);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
		}
		
//		resource = c.resource("https://postman-echo.com/get?foo1=bar1&foo2=bar2");
//		response = resource.get(ClientResponse.class);
//		System.out.println(response.getEntity(String.class));
//
//		resource = c.resource("https://postman-echo.com/put?foo1=bar1&foo2=bar2");
//		response = resource.put(ClientResponse.class);
//		System.out.println(response.getEntity(String.class));
//
//		resource = c.resource("https://postman-echo.com/post?foo1=bar1&foo2=bar2");
//		response = resource.post(ClientResponse.class);
//		System.out.println(response.getEntity(String.class));
//
//		resource = c.resource("https://postman-echo.com/delete?foo1=bar1&foo2=bar2");
//		response = resource.delete(ClientResponse.class);
//		System.out.println(response.getEntity(String.class));
	}

	private void priseEnComptePoids() {
		List<String> idList_ = new ArrayList<>(idList);
		
		for (String id : idList_) {
			if (prop.getProperty(id+".poids")!=null) {
				for (int i = 0; i < Integer.parseInt(prop.getProperty(id+".poids")); i++) {
					idList.add(id);
				}
			}
		}
	}

	private void priseEnComptePeriodeDuFlux() {
		if (prop.getProperty(fluxName+".periode")==null) {
			tempo = 1000;
		}
		else {
			tempo = Integer.parseInt(prop.getProperty(fluxName+".periode"));
		}
	}
	
}
