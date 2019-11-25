package com.bbe.logFactory;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.*;

public class ClientRest {

	private Client c = Client.create();
	private WebResource resource;
	private ClientResponse response;
	private String httpReqType;
	private String fluxName;
	protected static Logger logger = Logger.getLogger(ClientRest.class);
			
	public ClientRest(String property,String fluxName_) {
		this.fluxName = fluxName_;
		httpReqType = property.trim().substring(0, property.indexOf(":")).toUpperCase();
		resource = c.resource(property.trim().substring(property.indexOf(":")+1));
	}

	public void doHttpReq() {
		switch (httpReqType) {
		case "GET":
			response = resource.get(ClientResponse.class);
			break;
		case "PUT":
			response = resource.put(ClientResponse.class);
			break;
		case "POST":
			response = resource.post(ClientResponse.class);
			break;
		case "DELETE":
			response = resource.delete(ClientResponse.class);
			break;
		default:
			logger.error("Impossible de traiter la requete http : " + httpReqType);
			break;
		}
		
		logger.info(fluxName+">" +  response + "\n" + response.getEntity(String.class));
	}

}
