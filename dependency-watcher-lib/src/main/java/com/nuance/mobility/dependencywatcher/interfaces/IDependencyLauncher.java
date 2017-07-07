package com.nuance.mobility.dependencywatcher.interfaces;

import java.util.Collection;
import java.util.List;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.DependencyLauncherException;

public interface IDependencyLauncher {

	void launchDependency(Collection<Artifact> artifactsToNotify) throws DependencyLauncherException;
}
