package com.nuance.mobility.dependencywatcher.interfaces;

import java.util.ArrayList;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;

public interface IPomRetriever {
	
	ArrayList<String> retrievePomUrls(String property_name, String property_value)
			throws  MavenRepositoryException;
	
	String retrievePomFromUrl(String url) throws MavenRepositoryException;
}
