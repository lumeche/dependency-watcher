package com.nuance.mobility.dependencywatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nuance.mobility.dependencywatcher.data.Artifact;

@Service
public class NotificationService {

	@Autowired
	private DependencyService dependencyService;
	public void notifyDependenciesof(Artifact artifact){
		
	}
}
