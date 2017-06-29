package com.nuance.mobility.dependencywatcher.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;

public class XPathArtifactFactoryTest {

	private XPathArtifactFactory testee;
	private static String pom;
	private static List<Artifact> depencencies;

	@BeforeClass
	public static void classSetup() throws Exception {
		ClassPathResource r = new ClassPathResource("/pomTest.xml");
		pom=FileUtils.readFileToString(r.getFile());
		depencencies = new ArrayList<>();
		depencencies.add(new Artifact("org.springframework.boot", "spring-boot-starter-web", "1.0"));
		depencencies.add(new Artifact("org.springframework", "spring-boot-starter-test", "1.1"));
	}

	@Before
	public void setUp() throws Exception {
		testee = new XPathArtifactFactory();
	}

	@Test
	public void testGetId() throws PomParsingException {
		Artifact artifact = testee.getArtifact(pom);
		assertThat(artifact.getGroupId(), equalTo("com.nuance.mobility"));
		assertThat(artifact.getId(), equalTo("dependency-watcher"));
		assertThat(artifact.getVersion(), equalTo("0.0.1-SNAPSHOT"));
		assertThat(artifact.getScm(), equalTo("scm:git:https://github.com/lumeche/dependency-watcher.git"));

	}

	@Test
	public void testGetDependencies() throws PomParsingException {
		List<Artifact> dependencies = testee.getDependencies(pom);
		assertThat(dependencies.size(), equalTo(2));
		Matchers.containsInAnyOrder(dependencies, this.depencencies.toArray());
	}

}
