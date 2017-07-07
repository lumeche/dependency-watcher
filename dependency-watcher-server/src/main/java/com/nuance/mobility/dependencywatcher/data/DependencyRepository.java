package com.nuance.mobility.dependencywatcher.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Component;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;


//TODO: Make sure that this class is thread safe
@Component
public class DependencyRepository {

	private ConcurrentMap<Artifact, List<Artifact>> repository;
	
	public DependencyRepository(){
		repository=new ConcurrentHashMap<Artifact,List<Artifact>>();
	}
	
	public List<Artifact> getArtifactDependencies(Artifact artifact){
		return new ArrayList<Artifact>(repository.getOrDefault(artifact, ListUtils.EMPTY_LIST));
	}
	
	public void updateDependency(Artifact artifact,List<Artifact> dependencies){
		repository.put(artifact, new ArrayList<Artifact>(dependencies));
	}
	
	public List<Artifact> getArtifactsThatDependsOn(final Artifact dependency){
		Object[] ret = repository.entrySet().parallelStream().filter(new Predicate<Map.Entry<Artifact, List<Artifact>>>() {
			@Override
			public boolean test(Map.Entry<Artifact, List<Artifact>> t) {
				return t.getValue().contains(dependency);
			}
		}).map(e-> e.getKey()).toArray();
		ArrayList<Artifact> dependencies = new ArrayList<Artifact>();
		for (int i = 0; i < ret.length; i++) {
			dependencies.add((Artifact)ret[i]);
			
		}
		return dependencies;
	}
}
