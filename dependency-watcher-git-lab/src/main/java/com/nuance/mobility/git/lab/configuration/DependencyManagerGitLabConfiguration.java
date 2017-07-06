package com.nuance.mobility.git.lab.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nuance.mobility.git.lab.GitLabDependencyLauncher.GitLabDependencyLauncher;
import com.nuance.mobility.interfaces.IDependencyLauncher;


@Configuration
public class DependencyManagerGitLabConfiguration {

	
	@Bean
	public IDependencyLauncher pbuildGitLabDependencyLauncher(){
		return new GitLabDependencyLauncher();
	}
}
