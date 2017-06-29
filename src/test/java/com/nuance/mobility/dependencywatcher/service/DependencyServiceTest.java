package com.nuance.mobility.dependencywatcher.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.nuance.mobility.dependencywatcher.data.Artifact;
import com.nuance.mobility.dependencywatcher.data.DependencyRepository;
import com.nuance.mobility.dependencywatcher.data.IArtifactFactory;
import com.nuance.mobility.dependencywatcher.data.XPathArtifactFactory;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;

public class DependencyServiceTest {

	private DependencyService testee;
	
	private DependencyRepository dependencyRepository;
	static private String a;
	static private String b;
	static private String c;
	static private String d;
	static private String e;
	
	static private Artifact artA;
	static private Artifact artB;
	static private Artifact artC;
	static private Artifact artD;
	static private Artifact artE;
	
	@BeforeClass
	public static void setuClass() throws IOException, PomParsingException{		
		a=FileUtils.readFileToString(new ClassPathResource("/a.xml").getFile());
		b=FileUtils.readFileToString(new ClassPathResource("/b.xml").getFile());
		c=FileUtils.readFileToString(new ClassPathResource("/c.xml").getFile());
		d=FileUtils.readFileToString(new ClassPathResource("/d.xml").getFile());
		a=FileUtils.readFileToString(new ClassPathResource("/e.xml").getFile());
		
		IArtifactFactory af = new XPathArtifactFactory();
		artA=af.getArtifact(a);
		artB=af.getArtifact(b);
		artC=af.getArtifact(c);
		artD=af.getArtifact(d);
		artE=af.getArtifact(e);
		
	}
	
	@Before
	public void setUp() throws Exception {
		testee=new DependencyService();
		dependencyRepository=mock(DependencyRepository.class);
		testee.setArtifactFactory(new XPathArtifactFactory());
		testee.setDependencyRepository(dependencyRepository);
	}

	@Test
	public void testUpdateDependencies() {
		//WHEN
		testee.updateDependencies(c);
		//THEN
		assertThat(testee.getsWhoDependsOnArtifact(artA).size(), equalTo(1));
		assertThat(testee.getsWhoDependsOnArtifact(artA).get(0), equalTo(c));
		assertThat(testee.getsWhoDependsOnArtifact(artB).size(), equalTo(1));
		assertThat(testee.getsWhoDependsOnArtifact(artB).get(0), equalTo(c));
	}

	
	@Test
	public void testDependencies() {
		//WHEN
		testee.updateDependencies(a);
		testee.updateDependencies(b);
		testee.updateDependencies(c);
		testee.updateDependencies(d);
		testee.updateDependencies(e);
		//THEN
		assertThat(testee.getsWhoDependsOnArtifact(artA).size(), equalTo(2));
		containsInAnyOrder(testee.getsWhoDependsOnArtifact(artA),artC,artE);
		
		assertThat(testee.getsWhoDependsOnArtifact(artB).size(), equalTo(1));
		containsInAnyOrder(testee.getsWhoDependsOnArtifact(artB),artC);
		
		assertThat(testee.getsWhoDependsOnArtifact(artC).size(), equalTo(1));
		containsInAnyOrder(testee.getsWhoDependsOnArtifact(artC),artD);		
	}

}
