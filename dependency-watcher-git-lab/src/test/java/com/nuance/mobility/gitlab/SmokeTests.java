package com.nuance.mobility.gitlab;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBuild;
import org.gitlab.api.models.GitlabProject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SmokeTests {

	private GitlabAPI api;

	@Before
	public void setUp() throws Exception {
		api = GitlabAPI.connect("https://git.labs.nuance.com", "pWtB2gbhv72TmHo7fnK1");
	}

	@Test
	@Ignore
	public void test() throws IOException {
		String projectPath = "git@git.labs.nuance.com:mobility-ncs-components/location-language-metadata-service.git";
		Pattern pattern = Pattern.compile("^.*:([\\w_-]*)\\/([\\w_-]*).git$");

		Matcher matcher = pattern.matcher(projectPath);
		matcher.matches();
		String namespace = matcher.group(1);
		String projectName = matcher.group(2);
		
		GitlabProject project = api.getProject(namespace, projectName);
		String requestPath= String.format("/projects/%s/pipeline?ref=%s", project.getId(),project.getDefaultBranch());
		GitlabBuild build = api.dispatch().to(requestPath, GitlabBuild.class);
		
		System.out.println("ID:xxxxxxx" + build.getId());
	}

}
