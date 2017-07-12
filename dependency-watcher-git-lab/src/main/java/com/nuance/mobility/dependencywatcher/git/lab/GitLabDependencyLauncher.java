package com.nuance.mobility.dependencywatcher.git.lab;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBuild;
import org.gitlab.api.models.GitlabProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.DependencyLauncherException;
import com.nuance.mobility.dependencywatcher.interfaces.IDependencyLauncher;

public class GitLabDependencyLauncher implements IDependencyLauncher {

	private Logger logger = LoggerFactory.getLogger(GitLabDependencyLauncher.class);;
	
	@Value("${git.service.account.token}")
	private String gitServiceAccountToken;
	
	@Value("${git.url}")
	private String gitServiceAccountUrl;
	
	@Value("${git.project.information.regex}")
	private String gitProjectInformationRegex;

	private GitlabAPI api;

	private Pattern pattern;

	public GitLabDependencyLauncher() {
		
	}
	@PostConstruct
	public void init(){
		logger.debug("git account token {}", gitServiceAccountToken);
		logger.debug("git url{}", gitServiceAccountUrl);
		logger.debug("git project regex {}", gitProjectInformationRegex);
		api = GitlabAPI.connect(gitServiceAccountUrl, gitServiceAccountToken);
		pattern = Pattern.compile(gitProjectInformationRegex);
		
	}

	@Override
	public void launchDependency(Collection<Artifact> dependencies) throws DependencyLauncherException {
		for (Artifact artifact : dependencies) {
			launchBuild(artifact);
		}

	}
	//FIXME: The match is failing all the time
	//TODO: The pom file needs to be parsed properly with a library to be able to get the group and version from parents for both the project itself and the dependencies
	
	private void launchBuild(Artifact dependency) throws DependencyLauncherException {
		Matcher matcher = pattern.matcher(dependency.getScm());
		if (!matcher.matches()) {
			String message = String.format("The SCM field [%s] in the artifact %s.%s is not valid", dependency.getScm(),
					dependency.getGroupId(), dependency.getId());
			logger.error(message);
			throw new DependencyLauncherException(message);
		}
		
		String namespace = matcher.group(1);
		String projectName = matcher.group(2);

		try {
			GitlabProject project = api.getProject(namespace, projectName);
			
			//TODO: Find a way to launch on several branches and not only in the default one
			String requestPath = String.format("/projects/%s/pipeline?ref=%s", project.getId(),
					project.getDefaultBranch());
			GitlabBuild build = api.dispatch().to(requestPath, GitlabBuild.class);

			logger.info("Build started with ID: {}", build.getId());
		} catch (IOException e) {
			logger.error("Error while trying to launch build on project with scm {}", dependency.getScm());
			throw new DependencyLauncherException(String.format("Error launching git project %s", dependency.getScm()),
					e);
		}

	}

	public void setGitServiceAccountToken(String gitServiceAccountToken) {
		this.gitServiceAccountToken = gitServiceAccountToken;
	}

	public void setGitServiceAccountUrl(String gitServiceAccountUrl) {
		this.gitServiceAccountUrl = gitServiceAccountUrl;
	}

	public void setGitProjectInformationRegex(String gitProjectInformationRegex) {
		this.gitProjectInformationRegex = gitProjectInformationRegex;
	}

	
	void setApi(GitlabAPI api) {
		this.api = api;
	}
	
	

}
