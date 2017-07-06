package com.nuance.mobility.dependencywatcher.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.data.DependencyRepository;
import com.nuance.mobility.dependencywatcher.exceptions.DependencyLauncherException;
import com.nuance.mobility.dependencywatcher.interfaces.IDependencyLauncher;

@Service
public class NotificationService {
	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	private DependencyRepository dependencyRepository;

	@Autowired
	private IDependencyLauncher dependencyLauncher;
	
	public void notifyDependenciesOf(Artifact artifact){
		logger.info("Getting artifacts that depends on {} to notify",artifact);
		List<Artifact> artifactsToNotify = dependencyRepository.getArtifactsThatDependsOn(artifact);
		
		logger.info("About to notify the following artifacts {}",artifactsToNotify);
		
		try {
			dependencyLauncher.launchDependency(artifactsToNotify);
		} catch (DependencyLauncherException e) {
			// TODO Send an alarm to somewhere with this problem
			logger.error("The dependencies of project {} were not uploaded",artifact,e);
		} 
		
		
	}
}
