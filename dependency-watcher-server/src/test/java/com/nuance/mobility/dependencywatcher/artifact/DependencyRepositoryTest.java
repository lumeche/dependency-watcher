package com.nuance.mobility.dependencywatcher.artifact;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.data.DependencyRepository;

public class DependencyRepositoryTest {

	private DependencyRepository testee;
	private Artifact a;
	private Artifact b;
	private Artifact c;
	private Artifact d;
	private Artifact e;
	private List<Artifact> dependenciesOfC;
	private List<Artifact> dependenciesOfE;
	private List<Artifact> dependenciesOfD;

	@Before
	public void setUp() throws Exception {
		testee = new DependencyRepository();
	}

	@Test
	public void addAndGetDependencies() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		// DO
		List<Artifact> res = testee.getArtifactDependencies(c);
		// THEN
		assertThat(res.size(), equalTo(2));
		Matchers.arrayContainingInAnyOrder(res,a,b);
	}

	@Test
	public void addDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		dependenciesOfC.add(new Artifact("another"));
		// DO
		testee.updateDependency(a, dependenciesOfC);
		List<Artifact> res = testee.getArtifactDependencies(a);
		// THEN
		assertThat(res.size(), equalTo(3));
	}

	@Test
	public void removeDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		dependenciesOfC.remove(0);
		testee.updateDependency(c, dependenciesOfC);
		// DO
		List<Artifact> res = testee.getArtifactDependencies(c);
		// THEN
		assertThat(res.size(), equalTo(1));
	}

	@Test
	public void addAndRemoveDependency() {
		// WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		dependenciesOfC.remove(a);
		Artifact another = new Artifact("another");
		dependenciesOfC.add(another);
		// THEN
		testee.updateDependency(c, dependenciesOfC);
		List<Artifact> res = testee.getArtifactDependencies(c);
		// THEN
		assertThat(res.size(), equalTo(2));
		assertThat(res,containsInAnyOrder(another,b));
		Matchers.not(Matchers.contains(res,a));
	}
	
	@Test
	public void testCopyList(){
		//WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		//THEN
		dependenciesOfC.remove(0);
		List<Artifact> res = testee.getArtifactDependencies(c);
		//DO
		assertThat(dependenciesOfC.size(), equalTo(1));
		assertThat(res.size(), equalTo(2));		
	}
	
	@Test
	public void whoDependsOnMe(){
		//WHEN
		prepareDependencies();
		testee.updateDependency(c, dependenciesOfC);
		testee.updateDependency(d, dependenciesOfD);
		testee.updateDependency(e, dependenciesOfE);		
		//THEN
		List<Artifact> dependsOnA = testee.getArtifactsThatDependsOn(a);
		List<Artifact> dependsOnB = testee.getArtifactsThatDependsOn(b);
		List<Artifact> dependsOnC = testee.getArtifactsThatDependsOn(c);
		assertThat(dependsOnA,containsInAnyOrder(c,e));
		assertThat(dependsOnB,containsInAnyOrder(c));
		assertThat(dependsOnC,containsInAnyOrder(d));
	}
	

	private void prepareDependencies() {
		a = new Artifact("a");
		b = new Artifact("b");
		c = new Artifact("c");
		d = new Artifact("d");
		e = new Artifact("e");
		dependenciesOfC = new ArrayList<Artifact>(Arrays.asList(a, b));
		dependenciesOfD = new ArrayList<Artifact>(Arrays.asList(c));
		dependenciesOfE = new ArrayList<Artifact>(Arrays.asList(a));
		
	}

}
