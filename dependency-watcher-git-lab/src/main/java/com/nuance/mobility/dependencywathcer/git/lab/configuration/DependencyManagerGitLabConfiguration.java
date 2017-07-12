package com.nuance.mobility.dependencywathcer.git.lab.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nuance.mobility.dependencywatcher.git.lab.GitLabDependencyLauncher;
import com.nuance.mobility.dependencywatcher.interfaces.IDependencyLauncher;


@Configuration
public class DependencyManagerGitLabConfiguration {

	@Bean
	public IDependencyLauncher buildGitLabDependencyLauncher(){
		return new GitLabDependencyLauncher();
	}
}
