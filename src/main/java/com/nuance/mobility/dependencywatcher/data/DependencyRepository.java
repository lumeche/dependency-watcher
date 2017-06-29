package com.nuance.mobility.dependencywatcher.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class DependencyRepository {

	private ConcurrentMap<Artifact, List<Artifact>> repository;
	
	public DependencyRepository(){
		repository=new ConcurrentHashMap<Artifact,List<Artifact>>();
	}
	
	public List<Artifact> getArtifactDependencies(Artifact artifact){
		return new ArrayList<Artifact>(repository.get(artifact));
	}
	
	public void updateDependency(Artifact artifact,List<Artifact> dependencies){
		repository.put(artifact, new ArrayList(dependencies));
	}
	
}
