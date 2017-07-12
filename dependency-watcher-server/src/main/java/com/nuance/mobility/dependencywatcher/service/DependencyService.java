package com.nuance.mobility.dependencywatcher.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.artifact.IArtifactFactory;
import com.nuance.mobility.dependencywatcher.data.DependencyRepository;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;
import com.nuance.mobility.dependencywatcher.exceptions.UpdatingDependenciesException;;

@Service
public class DependencyService {

	private static Logger logger = LoggerFactory.getLogger(DependencyService.class);
	
	@Autowired
	private DependencyRepository dependencyRepository;

	@Autowired
	private IArtifactFactory artifactFactory;

	public Artifact updateDependencies(String pom) throws UpdatingDependenciesException {
		try {
			Artifact artifact = artifactFactory.getArtifact(pom);
			List<Artifact> dependencies = artifactFactory.getDependencies(artifact);
			logger.info("Dependencies  of {}",artifact.toString());
			logger.info("{}",dependencies);
			
			dependencyRepository.updateDependency(artifact, dependencies);
			return artifact;
		} catch (PomParsingException e) {
			throw new UpdatingDependenciesException(e);
		}
	}

	void setDependencyRepository(DependencyRepository dependencyRepository) {
		this.dependencyRepository = dependencyRepository;
	}

	void setArtifactFactory(IArtifactFactory artifactFactory) {
		this.artifactFactory = artifactFactory;
	}

}
