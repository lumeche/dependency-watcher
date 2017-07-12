package com.nuance.mobility.dependencywatcher.artifactory;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ArtifactoryRetriever.class)
public class ArtifactoryRetrieverTest {
	
	@Autowired
	private ArtifactoryRetriever artifactoryRetriever;
	
	@Test
	public void test() throws MavenRepositoryException {
    ArrayList<String> pom_files = new ArrayList<String>();
    
	
    String property_name="trigger";
    String property_value="false";	
	
	pom_files=artifactoryRetriever.retrievePoms(property_name,property_value);
	System.out.printf( "searching for %s  and %s ", property_name,property_value);
	//iterate through list
	for (int x=0; x<pom_files.size(); x++){
	   System.out.println(pom_files.get(x));
	}
	
}
}


