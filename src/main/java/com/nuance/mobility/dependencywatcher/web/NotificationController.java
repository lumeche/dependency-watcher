package com.nuance.mobility.dependencywatcher.web;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nuance.mobility.dependencywatcher.data.Artifact;
import com.nuance.mobility.dependencywatcher.data.IArtifactFactory;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;
import com.nuance.mobility.dependencywatcher.service.DependencyService;
import com.nuance.mobility.dependencywatcher.service.NotificationService;


@RestController
public class NotificationController {

	Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private DependencyService dependencyService;
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private IArtifactFactory artifactFactory;

	@RequestMapping(value = "/notify/", method = RequestMethod.POST)
	public String updateDependency(@RequestBody String pom) throws PomParsingException{
		logger.info("POM received {}", pom);

		dependencyService.updateDependencies(pom);

		Artifact artifact = artifactFactory.getArtifact(pom);
		notificationService.notifyDependenciesof(artifact);

		return null;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = Exception.class)
	public void handleInvalidRequest(final PomParsingException e) {
		logger.error("Error found trying to parse the pom file ",e);
	}

}
