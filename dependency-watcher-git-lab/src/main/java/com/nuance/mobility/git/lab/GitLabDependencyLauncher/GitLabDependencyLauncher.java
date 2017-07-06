package com.nuance.mobility.git.lab.GitLabDependencyLauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuance.mobility.interfaces.IDependencyLauncher;

public class GitLabDependencyLauncher implements IDependencyLauncher{

	private Logger logger= LoggerFactory.getLogger(GitLabDependencyLauncher.class);;

	public boolean triggerBuild(String scm){
		return false;
	}

	@Override
	public boolean launchDependency(String dependency) {
		logger.info("About to notify dependency {}",dependency);
		// TODO Auto-generated method stub
		return false;
	}
	
}
