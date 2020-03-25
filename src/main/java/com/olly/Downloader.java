package com.olly;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Downloader {
	private final static Logger LOGGER = LogManager.getLogger(Downloader.class);

	public static void main(String[] args) {
		new Downloader().go();
	}
	
	public void go() {
		LOGGER.debug("test");
	}
	
	
	private final String doCall(String url) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(url);
			String ret = null;
			LOGGER.debug("doing call: "+request);
			
			// create json request
			//request.setHeader("Content-type", ContentType.APPLICATION_JSON.getMimeType()); // probably useless
			//request.setHeader("Authorization", "Bearer " + token);
			CloseableHttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() >= 200
					&& response.getStatusLine().getStatusCode() <= 299) {
				ret = IOUtils.toString(response.getEntity().getContent(),
						ContentType.APPLICATION_JSON.getCharset());
			} else if (response.getStatusLine().getStatusCode() == 404) {
				throw new Exception(response.toString());
			} else {
				LOGGER.warn("Unable to call: "+request+". "+response);
				throw new Exception("Unable to call: "+request);
			}
			return ret;
		} catch (IOException t) {
			LOGGER.error("Unable to call: "+url,t);
			return null;
		}
	}
}
