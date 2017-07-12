package com.nuance.mobility.dependencywatcher.artifactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;

public class ArtifactoryRetriever implements IPomRetriever {

	Logger logger = LoggerFactory.getLogger(ArtifactoryRetriever.class);

	@Value("${artifactory.host}")
	private String artifactory_host;

	@Value("${artifactory.port}")
	private String artifactory_port;

	@Value("${artifactory.base_url}")
	private String artifactory_base_uri;

	@Value("${artifactory.repo}")
	private String artifactory_repo;

	@Override
	public ArrayList<String> retrievePomUrls(String property_name,
			String property_value) throws MavenRepositoryException {

		String url_string = String.format("http://%s:%s%sprop?%s=%s&repos=%s",
				artifactory_host, artifactory_port, artifactory_base_uri,
				property_name, property_value, artifactory_repo);
		logger.info("Getting poms from {}",url_string);
		
		StringWriter writter = new StringWriter();
		try {
			URL url = new URL(url_string);
			InputStream is = url.openStream();
			IOUtils.copy(is, writter);
			JSONObject obj=new JSONObject(writter.toString());
			JSONArray results = obj.getJSONArray("results");
			ArrayList<String> all_pom_files = new ArrayList<String>();
			for (int i = 0; i < results.length(); i++) {
				all_pom_files.add(results.getJSONObject(i).getString("uri"));
			}
			return all_pom_files;
		} catch (IOException| JSONException e) {
			throw new MavenRepositoryException(e);
		}

	}

	@Override
	public String retrievePomFromUrl(String urlPom) throws MavenRepositoryException {
		try {
			String downloadURL = getPomUrl(urlPom);
			return getPomFromUrl(downloadURL);
		} catch (IOException| JSONException e) {
			throw new MavenRepositoryException(String.format("Error getting the pom file from the url %s",urlPom),e);
		} 
		
	}

	private String getPomFromUrl(String downloadURL) throws MalformedURLException, IOException {
		URL pomUrl=new URL(downloadURL);
		InputStream is=pomUrl.openStream();
		StringWriter sw=new StringWriter();
		IOUtils.copy(is,sw);
		return sw.toString();
	}

	private String getPomUrl(String urlPom) throws MalformedURLException, IOException, JSONException {
		URL metadataUrl=new URL(urlPom);
		InputStream is = metadataUrl.openConnection().getInputStream();
		StringWriter writter=new StringWriter();
		IOUtils.copy(is, writter);
		JSONObject obj=new JSONObject(writter.toString());
		String downloadURL = obj.getString("downloadUri");
		return downloadURL;
	}


}
