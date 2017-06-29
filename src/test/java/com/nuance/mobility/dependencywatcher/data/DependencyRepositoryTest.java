package com.nuance.mobility.dependencywatcher.data;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DependencyRepositoryTest {

	private DependencyRepository testee;
	private Artifact a;
	private Artifact ab;
	private Artifact ac;
	private List<Artifact> dependenciesOfA;
	

	@Before
	public void setUp() throws Exception {
		testee = new DependencyRepository();
	}

	@Test
	public void addAndGetDependencies() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(a, dependenciesOfA);
		// DO
		List<Artifact> res = testee.getArtifactDependencies(a);
		// THEN
		assertThat(res.size(), equalTo(dependenciesOfA.size()));
		assertThat(res.get(0), anyOf(equalTo(ac), equalTo(ab)));
		assertThat(res.get(1), anyOf(equalTo(ac), equalTo(ab)));
	}

	@Test
	public void addDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(a, dependenciesOfA);
		dependenciesOfA.add(new Artifact("another"));
		// DO
		testee.updateDependency(a, dependenciesOfA);
		List<Artifact> res = testee.getArtifactDependencies(a);
		// THEN
		assertThat(res.size(), equalTo(3));
	}

	@Test
	public void removeDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(a, dependenciesOfA);
		dependenciesOfA.remove(0);
		testee.updateDependency(a, dependenciesOfA);
		// DO
		List<Artifact> res = testee.getArtifactDependencies(a);
		// THEN
		assertThat(res.size(), equalTo(1));
	}

	@Test
	public void addAndRemoveDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(a, dependenciesOfA);
		dependenciesOfA.remove(ac);
		Artifact another = new Artifact("another");
		dependenciesOfA.add(another);
		// THEN
		testee.updateDependency(a, dependenciesOfA);
		List<Artifact> res = testee.getArtifactDependencies(a);
		// THEN
		assertThat(res.size(), equalTo(2));
		assertThat(res.get(0), anyOf(equalTo(ab),equalTo(another)));
		assertThat(res.get(1), anyOf(equalTo(ab),equalTo(another)));
	}
	
	@Test
	public void testCopyList(){
		//WHEN
		prepareDependencies();
		testee.updateDependency(a, dependenciesOfA);
		//THEN
		dependenciesOfA.remove(0);
		List<Artifact> res = testee.getArtifactDependencies(a);
		//DO
		assertThat(dependenciesOfA.size(), equalTo(1));
		assertThat(res.size(), equalTo(2));
		
	}
	
	

	private void prepareDependencies() {
		a = new Artifact("a");
		ab = new Artifact("ab");
		ac = new Artifact("ac");
		
		dependenciesOfA = new ArrayList<Artifact>(Arrays.asList(ab, ac));
		
	}

}
