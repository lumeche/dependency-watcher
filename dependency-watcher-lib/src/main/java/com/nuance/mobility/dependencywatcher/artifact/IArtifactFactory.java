package com.nuance.mobility.dependencywatcher.artifact;

import java.util.List;

import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;

public interface IArtifactFactory {

	Artifact getArtifact(String pom) throws PomParsingException;

	List<Artifact> getDependencies(Artifact pom) throws PomParsingException;

}