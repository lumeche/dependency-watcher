package com.nuance.mobility.dependencywatcher.interfaces;

import java.util.ArrayList;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;

public interface IPomRetriever {
	
	ArrayList<String> retrievePoms(String property_name, String property_value)
			throws  MavenRepositoryException;
}
