package com.nuance.mobility.dependencywatcher.artifactory.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nuance.mobility.dependencywatcher.artifactory.ArtifactoryRetriever;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;

@Configuration
public class ArtifactoryConfiguration {

	@Bean
	public IPomRetriever buildArtifactPomRetriever(){
		return new ArtifactoryRetriever();
	}
}
