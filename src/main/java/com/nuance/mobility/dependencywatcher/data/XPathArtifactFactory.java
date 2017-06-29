package com.nuance.mobility.dependencywatcher.data;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.nuance.mobility.dependencywatcher.exceptions.PomParsingException;

@Controller
public class XPathArtifactFactory implements IArtifactFactory {

	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	XPathFactory xapthFactory;
	private XPath xpath;

	public XPathArtifactFactory() throws PomParsingException {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xapthFactory = XPathFactory.newInstance();
			xpath = xapthFactory.newXPath();
		} catch (Exception e) {
			throw new PomParsingException(e);
		}

	}

	@Override
	public Artifact getArtifact(String pom) throws PomParsingException {
		try {
			Document doc = builder.parse(new InputSource(new StringReader(pom)));
			String groupId = xpath.compile("/project/groupId").evaluate(doc);
			String id = xpath.compile("/project/artifactId").evaluate(doc);
			String version = xpath.compile("/project/version").evaluate(doc);
			String scm = xpath.compile("/project/scm/connection").evaluate(doc);
			return new Artifact(groupId, id, version, scm);
		} catch (Exception e) {
			throw new PomParsingException(e);
		}

	}

	@Override
	public List<Artifact> getDependencies(String pom) throws PomParsingException{
		try {
			Document doc = builder.parse(new InputSource(new StringReader(pom)));
			double dependenciesCount = (double) xpath.compile("count(/project/dependencies/dependency)").evaluate(doc,XPathConstants.NUMBER);
			List<Artifact> dependencies=new ArrayList<Artifact>();
			for (int i = 0; i<dependenciesCount; i++) {
				String groupId=xpath.compile("/project/dependencies/dependency["+i+"]").evaluate(doc);
				String id=xpath.compile("/project/dependencies/dependency["+i+"]").evaluate(doc);
				String version=xpath.compile("/project/dependencies/dependency["+i+"]").evaluate(doc);
				dependencies.add(new Artifact(groupId, id, version));
			}
			return dependencies;	
		} catch (Exception e) {
			throw new PomParsingException(e);
		}
		
	}
}
