package com.olly.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class Downloader {
	private final static Logger LOGGER = LogManager.getLogger(Downloader.class);

	public static void main(String[] args) throws Exception {
		new Downloader().go();
	}
	
	/**
	 * Assuming there's a website with contents:<br>
	 * Magazine First:<br>
	 * GET https://magazine-site/11 -> {"title":"Auto Magazine", "totPages":4}<br>
	 * GET https://magazine-site/11/images/from/0/to/3 -> ["base64-png-page1","base64-png-page2","base64-png-page3","base64-png-page4"]<br>
	 * <br>
	 * Magazine Second:<br>
	 * GET https://magazine-site/12 -> {"title":"Sport Magazine", "totPages":3}<br>
	 * GET https://magazine-site/12/images/from/0/to/2 -> ["base64-png-page1","base64-png-page2","base64-png-page3"]<br>
	 * <br>
	 * creates files like:<br>
	 * - /temp/base/Auto Magazine/0.png -> with page1 content<br>
	 * - /temp/base/Auto Magazine/1.png -> with page2 content<br>
	 * - /temp/base/Auto Magazine/2.png -> with page3 content<br>
	 * - /temp/base/Auto Magazine/3.png -> with page4 content<br>
	 * - /temp/base/Sport Magazine/0.png -> with page1 content<br>
	 * - /temp/base/Sport Magazine/1.png -> with page2 content<br>
	 * - /temp/base/Sport Magazine/2.png -> with page3 content<br>
	 */
	public void go() throws Exception {
		LOGGER.info("magazine img dld");
		int[] ids = new int[] {11, 12}; // id of the magazines to download
		for (int i=0;i<ids.length;i++) {
			int id = ids[i];
			LOGGER.info("doing #"+id+ "("+(i+1)+"/"+ids.length+")");
			JSONObject obj = new JSONObject(doGet("https://magazine-site/"+id));
			int tot = obj.getInt("totPages");
			String name = obj.getString("title");
			File dir = new File("/temp/base/"+name);
			dir.mkdirs();

			int STEP=6;
			int min=0;
			while (min<tot) {
				int max = Math.min(min+STEP-1, tot-1);
				JSONArray ja = new JSONArray(doGet("https://magazine-site/"+id+"/images/from/"+min+"/to/"+max));
				for (int img=0;img<ja.length();img++) {
					String b64 = ja.getString(img);
					byte[] img1 = Base64.getDecoder().decode(b64);
					File f = new File(dir,(img+min)+".png");
					IOUtils.write(img1, new FileOutputStream(f));
					LOGGER.debug("saved: "+f);
				}
				min+=STEP;
				//Thread.sleep((int)(Math.random()*5000));
			}
		}
		LOGGER.info("magazine done");
	}
	
	
	private String doGet(String url) {
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
			} else {
				LOGGER.warn("http-error Unable to call: "+request+". "+response);
				return null;
			}
			return ret;
		} catch (IOException t) {
			LOGGER.error("Unable to call: "+url, t);
			return null;
		}
	}
}
