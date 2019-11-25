package com.bbe.logFactory;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.*;

public class ClientRest {

	private Client c = Client.create();
	private WebResource resource;
	private ClientResponse response;
	private HttpReq httpReq;
	private String fluxName;
	protected static Logger logger = Logger.getLogger(ClientRest.class);
			
	public ClientRest(String property,String fluxName_) {
		this.fluxName = fluxName_;
		String httpReqType = property.trim().substring(0, property.indexOf(":")).toUpperCase();
		switch (httpReqType) {
		case "GET":
			httpReq = HttpReq.GET;
			break;
		case "PUT":
			httpReq = HttpReq.PUT;
			break;
		case "POST":
			httpReq = HttpReq.POST;
			break;
		case "DELETE":
			httpReq = HttpReq.DELETE;
			break;
		default:
			break;
		}
		resource = c.resource(property.trim().substring(property.indexOf(":")+1));
	}

	public void doHttpReq() {
		switch (httpReq) {
		case GET:
			response = resource.get(ClientResponse.class);
			break;
		case DELETE:
			response = resource.delete(ClientResponse.class);
			break;
		case POST:
			response = resource.post(ClientResponse.class);
			break;
		case PUT:
			response = resource.put(ClientResponse.class);
			break;
		default:
			break;
		}
		logger.info(fluxName+">"+ response.getStatus() + "\n" + response.getEntity(String.class));
	}

}
