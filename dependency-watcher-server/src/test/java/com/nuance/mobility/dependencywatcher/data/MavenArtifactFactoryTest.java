package com.nuance.mobility.dependencywatcher.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MavenArtifactFactoryTest {

	@Autowired
	private MavenArtifactFactory testee;
	private String pom;
	private Artifact dependency1;
	private Artifact dependency2;

	@Before
	public void setUp() throws Exception {
		ClassPathResource r = new ClassPathResource("/exampleProject.xml");
		pom=FileUtils.readFileToString(r.getFile());
		dependency1=new Artifact("com.nuance.mobility.toolkit", "alarm-spring-boot-starter", "1.0.0-SNAPSHOT");
		dependency2=new Artifact("com.nuance.mobility.toolkit", "graceful-shutdown-spring-boot-autoconfigure", "1.0.0-SNAPSHOT");		
	}
	@Test
	public void pomWithoutSCM()throws Exception{
		ClassPathResource r = new ClassPathResource("/no_scm.xml");
		pom=FileUtils.readFileToString(r.getFile());
		Artifact artifact = testee.getArtifact(pom);
		assertThat(artifact.getId(),is("a") );
		assertThat(artifact.getGroupId(),is("com.nuance.mobility") );
		assertThat(artifact.getVersion(),is("1") );
		assertThat(artifact.getScm(), Matchers.isEmptyString());
	}
	@Test
	public void getIdwithGroupAndVersionFromParent() throws PomParsingException {
		Artifact artifact = testee.getArtifact(pom);
		
		assertThat(artifact.getId(),is("configuration-server") );
		assertThat(artifact.getGroupId(),is("com.nuance.mobility.ncs") );
		assertThat(artifact.getVersion(),is("1.0.0-SNAPSHOT") );
	
	}
	
	@Test(expected=PomParsingException.class)
	public void pomWithBadMvnRepo()throws Exception{
		ClassPathResource r = new ClassPathResource("/projectWithBadMvnRepo.xml");
		pom=FileUtils.readFileToString(r.getFile());
		Artifact artifact = testee.getArtifact(pom);
		List<Artifact> dependencies = testee.getDependencies(artifact);
	}
	@Test
	public void getDependenciesWithGroupAndVersionFromParent() throws PomParsingException {
		Artifact artifact = testee.getArtifact(pom);
		List<Artifact> depencies = testee.getDependencies(artifact);		
		assertThat(depencies, Matchers.hasItems(dependency1, dependency2));
	}

}
