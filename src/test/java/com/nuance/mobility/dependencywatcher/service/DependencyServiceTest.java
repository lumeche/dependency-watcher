package com.nuance.mobility.dependencywatcher.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.nuance.mobility.dependencywatcher.data.DependencyRepository;
import com.nuance.mobility.dependencywatcher.data.XPathArtifactFactory;

public class DependencyServiceTest {

	private DependencyService testee;
	private DependencyRepository dependencyRepository;
	@Before
	public void setUp() throws Exception {
		testee=new DependencyService();
		dependencyRepository=mock(DependencyRepository.class);
		testee.setArtifactFactory(new XPathArtifactFactory());
		testee.setDependencyRepository(dependencyRepository);
	}

	@Test
	public void testUpdateDependencies() {
		fail("Not yet implemented");
	}

	
	@Test
	public void testGetDependenciesFor() {
		fail("Not yet implemented");
	}

}
