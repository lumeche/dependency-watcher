package com.nuance.mobility.dependencywatcher.service;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nuance.mobility.dependencywatcher.data.DependencyRepository;
import com.nuance.mobility.dependencywatcher.data.IArtifactFactory;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;
import com.nuance.mobility.dependencywatcher.exceptions.UpdatingDependenciesException;
import com.nuance.mobility.dependencywatcher.data.Artifact;;

@Service
public class DependencyService {
	@Autowired
	private DependencyRepository dependencyRepository;

	@Autowired
	private IArtifactFactory artifactFactory;

	public void updateDependencies(String pom) throws UpdatingDependenciesException {
		try {
			Artifact artifact = artifactFactory.getArtifact(pom);
			List<Artifact> dependencies = artifactFactory.getDependencies(pom);
		} catch (PomParsingException e) {
			throw new UpdatingDependenciesException(e);
		}
	}

	public List<Artifact> getsWhoDependsOnArtifact(Artifact artifact) {
		return null;
	}

	void setDependencyRepository(DependencyRepository dependencyRepository) {
		this.dependencyRepository = dependencyRepository;
	}

	void setArtifactFactory(IArtifactFactory artifactFactory) {
		this.artifactFactory = artifactFactory;
	}

}
