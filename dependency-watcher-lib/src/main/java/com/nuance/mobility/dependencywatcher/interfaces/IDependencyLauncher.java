package com.nuance.mobility.dependencywatcher.interfaces;

import java.util.List;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.DependencyLauncherException;

public interface IDependencyLauncher {

	void launchDependency(List<Artifact> artifactsToNotify) throws DependencyLauncherException;
}
