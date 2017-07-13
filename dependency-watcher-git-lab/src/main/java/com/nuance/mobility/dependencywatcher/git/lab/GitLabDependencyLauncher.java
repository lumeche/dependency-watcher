package com.nuance.mobility.dependencywatcher.git.lab;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBranch;
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

	@Value("${git.project.branches.regex}")
	private String gitProjectBranchesRegex;

	private GitlabAPI api;

	private Pattern pattern;

	public GitLabDependencyLauncher() {

	}

	@PostConstruct
	public void init() {
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
			Stream<String> branches = getTriggerBranch(project);
			branches.forEach(b -> sendTrigger(project, b));
		} catch (IOException | RuntimeException e) {
			logger.error("Error while trying to launch build on project with scm {}", dependency.getScm());
			throw new DependencyLauncherException(String.format("Error launching git project %s", dependency.getScm()),
					e);
		}
	}

	private void sendTrigger(GitlabProject project, String branch) {
		String requestPath = String.format("/projects/%s/pipeline?ref=%s", project.getId(), branch);
		GitlabBuild build;
		try {
			build = api.dispatch().to(requestPath, GitlabBuild.class);
			logger.info("Build started with ID: {}", build.getId());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Stream<String> getTriggerBranch(GitlabProject project) throws IOException {
		List<GitlabBranch> branches = api.getBranches(project);
		return branches.parallelStream().map(b -> b.getName())
				.filter(Pattern.compile(gitProjectBranchesRegex).asPredicate());

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

	public void setGitProjectBranchesRegex(String gitProjectBranchesRegex) {
		this.gitProjectBranchesRegex = gitProjectBranchesRegex;
	}
	void setApi(GitlabAPI api) {
		this.api = api;
	}

}
