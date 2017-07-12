package com.nuance.mobility.dependencywatcher.data;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RemoteRepository.Builder;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nuance.mobility.dependencywatcher.artifact.Artifact;
import com.nuance.mobility.dependencywatcher.artifact.IArtifactFactory;
import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;


@Component
public class MavenArtifactFactory implements IArtifactFactory {

	private static Logger logger = LoggerFactory.getLogger(MavenArtifactFactory.class);

	@Autowired
	private RepositorySystem system;

	@Autowired
	private DefaultRepositorySystemSession session;

	@Value("${mvn.repo.protocol}")
	private String mvnRepoProrotocol;

	@Value("${mvn.repo.hostname}")
	private String mvnRepoHostname;

	@Value("${mvn.repo.port}")
	private String mvnRepoPort;

	@Value("${mvn.repo.url}")
	private String mvnRepoUrl;

	@Value("${mvn.repo.username}")
	private String mvnRepoUsername;

	@Value("${mvn.repo.password}")
	private String mvnRepoPassword;
	
	@Override
	public Artifact getArtifact(String pom) throws PomParsingException {

		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = null;
		try {
			model = reader.read(new StringReader(pom));
		} catch (IOException | XmlPullParserException e) {
			throw new PomParsingException("Error converting the string to a pom object", e);
		}
		completeVersionInformation(model);
		String scm = model.getScm() == null ? "" : model.getScm().getConnection();
		Artifact artifact = new Artifact(model.getGroupId(), model.getArtifactId(), model.getVersion(), scm);
		return artifact;
	}

	private void completeVersionInformation(Model model) {
		if (null != model.getParent()) {
			model.setGroupId(StringUtils.defaultIfBlank(model.getGroupId(), model.getParent().getGroupId()));
			model.setVersion(StringUtils.defaultIfBlank(model.getVersion(), model.getParent().getVersion()));
		}

	}

	@Override
	public List<Artifact> getDependencies(Artifact artifact) throws PomParsingException {
		
		String dependencyString = String.format("%s:%s:%s", artifact.getGroupId(), artifact.getId(),
				artifact.getVersion());
		logger.debug("Getting dependencies  for{}", dependencyString);
		DefaultArtifact mvnArtifact = new DefaultArtifact(dependencyString);
		ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
		descriptorRequest.setArtifact(mvnArtifact);
		descriptorRequest.setRepositories(newRepositories());
		

		try {
			ArtifactDescriptorResult descriptorResult = system.readArtifactDescriptor(session, descriptorRequest);
			if(CollectionUtils.isEmpty(descriptorResult.getDependencies())){
				throw new PomParsingException(String.format("No dependencies found in the maven repository for %s",artifact.toString()));
			}
			List<Artifact> dependencies = new ArrayList<Artifact>();
			descriptorResult.getDependencies().parallelStream().map(d -> new Artifact(d.getArtifact().getGroupId(),
					d.getArtifact().getArtifactId(), d.getArtifact().getVersion())).forEach(dependencies::add);

			return dependencies;
		} catch (ArtifactDescriptorException e) {
			throw new PomParsingException(String.format("Errrog getting dependencies from %s", artifact.toString()), e);
		}

	}

	private List<RemoteRepository> newRepositories() {
		Authentication authentication = new AuthenticationBuilder().addUsername(mvnRepoUsername)
				.addPassword(mvnRepoPassword).build();
		String url = String.format("%s://%s:%s/%s", mvnRepoProrotocol, mvnRepoHostname, mvnRepoPort, mvnRepoUrl);
		RemoteRepository repo = new Builder("nuance", "default", url).setAuthentication(authentication).build();
		return singletonList(repo);
	}
	


}
