package com.nuance.mobility.dependencywatcher.artifactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;

@Component
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
	public ArrayList<String> retrievePoms(String property_name,
			String property_value) throws MavenRepositoryException {

		String url_string = String.format("http://%s:%s%sprop?%s=%s&repos=%s",
				artifactory_host, artifactory_port, artifactory_base_uri,
				property_name, property_value, artifactory_repo);

		StringWriter writter = new StringWriter();
		
		try {
			URL url = new URL(url_string);
			InputStream is = url.openConnection().getInputStream();
			
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

}
