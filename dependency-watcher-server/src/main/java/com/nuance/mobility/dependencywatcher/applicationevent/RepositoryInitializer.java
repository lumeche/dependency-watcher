package com.nuance.mobility.dependencywatcher.applicationevent;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;

@Component
public class RepositoryInitializer implements ApplicationListener<ApplicationStartingEvent>{

	@Autowired
	private IPomRetriever pomRetriever;
	@Value("${artifactory.dependency.propertyname}")
	private String dependencyPropertyName;
	@Value("${artifactory.dependency.propertyvalue}")
	private String dependencyPropertyValue;
	
	@Override
	public void onApplicationEvent(ApplicationStartingEvent arg0) {
		try {
			
			ArrayList<String> retrievePoms = pomRetriever.retrievePoms(dependencyPropertyName, dependencyPropertyValue);	
		
		} catch (MavenRepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
