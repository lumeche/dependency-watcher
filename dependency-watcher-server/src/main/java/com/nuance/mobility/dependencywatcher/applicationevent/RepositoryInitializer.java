package com.nuance.mobility.dependencywatcher.applicationevent;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;
import com.nuance.mobility.dependencywatcher.exceptions.UpdatingDependenciesException;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;
import com.nuance.mobility.dependencywatcher.service.DependencyService;

@Component
public class RepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private static Logger logger = LoggerFactory.getLogger(RepositoryInitializer.class);
	@Autowired
	private IPomRetriever pomRetriever;

	@Autowired
	private DependencyService dependencyService;

	@Value("${artifactory.dependency.propertyname}")
	private String dependencyPropertyName;
	@Value("${artifactory.dependency.propertyvalue}")
	private String dependencyPropertyValue;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		logger.debug("Setting up the dependencies repository");
		try {
			ArrayList<String> dependenciesToUpdate = pomRetriever.retrievePomUrls(dependencyPropertyName,
					dependencyPropertyValue);
			logger.info("{} dependencies will be updated", dependenciesToUpdate.size());
			dependenciesToUpdate.parallelStream().forEach(url->updateDependency(url));
		} catch (MavenRepositoryException | UpdatingDependenciesException e) {
			logger.error("Error updating dependency", e);
		}

	}
	
	private void updateDependency(String url)  {
		String pom = pomRetriever.retrievePomFromUrl(url);
		dependencyService.updateDependencies(pom);
	}

	void setPomRetriever(IPomRetriever pomRetriever) {
		this.pomRetriever = pomRetriever;
	}

	void setDependencyService(DependencyService dependencyService) {
		this.dependencyService = dependencyService;
	}

	void setDependencyPropertyName(String dependencyPropertyName) {
		this.dependencyPropertyName = dependencyPropertyName;
	}

	void setDependencyPropertyValue(String dependencyPropertyValue) {
		this.dependencyPropertyValue = dependencyPropertyValue;
	}

	static void setLogger(Logger logger) {
		RepositoryInitializer.logger = logger;
	}

}
