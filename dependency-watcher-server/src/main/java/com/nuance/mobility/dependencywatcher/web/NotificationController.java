package com.nuance.mobility.dependencywatcher.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;
import com.nuance.mobility.dependencywatcher.exceptions.UpdatingDependenciesException;
import com.nuance.mobility.dependencywatcher.service.DependencyService;
import com.nuance.mobility.dependencywatcher.service.NotificationService;


@RestController
public class NotificationController {

	Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private DependencyService dependencyService;
	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "/notify/", method = RequestMethod.POST)
	public String updateDependency(@RequestBody String pom) throws PomParsingException, UpdatingDependenciesException{
		logger.info("POM received {}", pom);
		
		Artifact artifact = dependencyService.updateDependencies(pom);
		
		notificationService.notifyDependenciesOf(artifact);

		return null;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = Exception.class)
	public void handleInvalidRequest(final PomParsingException e) {
		logger.error("Error found trying to parse the pom file ",e);
	}

}
