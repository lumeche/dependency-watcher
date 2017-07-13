package com.nuance.mobility.dependencywatcher.git.lab;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabBuild;
import org.gitlab.api.models.GitlabProject;
import org.junit.Before;
import org.junit.Test;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.DependencyLauncherException;
import com.nuance.mobility.dependencywatcher.git.lab.GitLabDependencyLauncher;


public class GitLabDependencyLauncherTest {

	private static final String PROJECT_REGEX = "^.*:(.*)/([\\w_-]*).git$";
	private static final String BRANCH_REGEX = "master.*";
	private static final String TOKEN = "sssssss";
	private static final String GIT_URL = "https://localhost:8082";
	private GitLabDependencyLauncher testee;
	private GitlabAPI api;
	private Artifact artifact;
	private GitlabProject project;
	private GitlabHTTPRequestor httpRequester;
	@Before
	public void setUp() throws Exception {
		testee=new GitLabDependencyLauncher();
		api=mock(GitlabAPI.class);
		project=mock(GitlabProject.class);
		httpRequester=mock(GitlabHTTPRequestor.class);
		artifact=new Artifact("1", "1", "1","git@git.labs.nuance.com:mobility-ncs-components/location-language-metadata-service.git");
		GitlabBranch master=new GitlabBranch();
		GitlabBranch masterA=new GitlabBranch();
		GitlabBranch other=new GitlabBranch();
		master.setName("master");
		masterA.setName("masterA");
		other.setName("other");
		
		testee.setGitProjectInformationRegex(PROJECT_REGEX);
		testee.setGitProjectBranchesRegex(BRANCH_REGEX);
		testee.setGitServiceAccountToken(TOKEN);
		testee.setGitServiceAccountUrl(GIT_URL);		
		testee.init();
		testee.setApi(api);
		
		when(api.getProject(anyString(),anyString())).thenReturn(project);
		
		when(api.getBranches(project)).thenReturn(new ArrayList<GitlabBranch>(Arrays.asList(master,masterA,other)));
		when(api.dispatch()).thenReturn(httpRequester);
		when(httpRequester.to(anyString(),anyObject())).thenReturn(mock(GitlabBuild.class));
	}

	@Test
	public void testRegexParsing() throws DependencyLauncherException {		
		testee.launchDependency(Arrays.asList(artifact));
		verify(api,times(2)).dispatch();
	}

}
